package com.unalignedbyte.words.model;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.Utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rafal on 04/02/2018.
 */

public class WordsImporter {
    private static WordsImporter instance;
    private Context context;

    private WordsImporter(Context context) {
        this.context = context;
    }

    public static WordsImporter get(Context context) {
        if (instance == null)
            instance = new WordsImporter(context);

        return instance;
    }

    public void reloadExternalDirectory() {
        String filesState = Environment.getExternalStorageState();
        if (!filesState.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File fdir = context.getFilesDir();
        File filesDir = context.getExternalFilesDir(null);

        if (!filesDir.exists())
            filesDir.mkdirs();

        // First create a foo file under files/ directory, then scan it, delete, and scan again
        try {
            File dummyFile = new File(filesDir, "foo");
            dummyFile.createNewFile();
            String[] paths = new String[]{dummyFile.getAbsolutePath()};
            MediaScannerConnection.scanFile(context, paths, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) {
                    File fooFile = new File(s);
                    fooFile.delete();
                    String[] paths = new String[]{fooFile.getAbsolutePath()};
                    MediaScannerConnection.scanFile(context, paths, null, null);
                }

            });
        } catch (Exception exception) {
            Log.d("Exception", exception.toString());
        }
    }


    public void importAllWords() {
        List<Group> groups = new LinkedList();
        List<Word> words = new LinkedList();

        String filesState = Environment.getExternalStorageState();
        if (filesState.equals(Environment.MEDIA_MOUNTED)) {
            File filesDir = context.getExternalFilesDir(null);
            File[] allFiles = filesDir.listFiles();

            for (File file : allFiles) {
                try {
                    InputStream stream = new FileInputStream(file);
                    importGroupsFromInputStream(stream, groups, words);
                    stream.close();
                } catch (Exception exception) {
                    Log.d("Exception", exception.toString());
                }

                file.delete();
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, null, null);
            }

            for (Group group : groups)
                WordsDataSource.get().addGroup(group);
            for (Word word : words)
                WordsDataSource.get().addWord(word);
        }

        String groupsPart = Utils.get().translate("groups", groups.size());
        String wordsPart = Utils.get().translate("words", groups.size());
        String toastString = context.getString(R.string.import_toast, groupsPart, wordsPart);

        Toast.makeText(context, toastString, Toast.LENGTH_SHORT).show();
    }

    private void importGroupsFromInputStream(InputStream stream, List<Group> groups, List<Word> words) throws Exception {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName().toLowerCase();
            if (name.equals("dict")) {
                importGroupFromParser(parser, groups, words);
            }
        }
    }

    private void importGroupFromParser(XmlPullParser parser, List<Group> groups, List<Word> words) throws Exception {
        String groupName = null;
        Language groupLanguage = null;

        String key = null;

        parser.require(XmlPullParser.START_TAG, null, "dict");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName().toLowerCase();
            if (name.equals("key")) {
                key = readText(parser).toLowerCase();
            } else if (name.equals("string") && key.equals("group")) {
                groupName = readText(parser);
            } else if (name.equals("string") && key.equals("languagecode")) {
                String value = readText(parser);
                groupLanguage = Language.getLanguage(value);
            } else if (name.equals("array") && key.equals("words") && groupName != null && groupLanguage != null) {
                Group group = new Group(groupName, groupLanguage);
                groups.add(group);
                importWordsFromParser(parser, group, words);
            } else {
                skip(parser);
            }
        }
    }

    private void importWordsFromParser(XmlPullParser parser, Group group, List<Word> words) throws Exception {
        parser.require(XmlPullParser.START_TAG, null, "array");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();
            if (name.toLowerCase().equals("dict")) {
                Word word = readWord(parser, group);
                words.add(word);
            } else {
                skip(parser);
            }
        }
    }

    private Word readWord(XmlPullParser parser, Group group) throws Exception {
        String key = null;

        String word = null;
        String translation = null;
        String pinyin = null;

        parser.require(XmlPullParser.START_TAG, null, "dict");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName().toLowerCase();
            if (name.equals("key")) {
                key = readText(parser).toLowerCase();
            } else if (name.equals("string") && key.equals("word")) {
                word = readText(parser);
            } else if (name.equals("string") && key.equals("translation")) {
                translation = readText(parser);
            } else if (name.equals("string") && key.equals("pinyin")) {
                pinyin = readText(parser);
            } else {
                skip(parser);
            }
        }

        String[] data = new String[group.getLanguage().getWordDataTitles().length];
        if (group.getLanguage().getCode().equals("cn")) {
            data[0] = word;
            data[1] = pinyin;
            data[2] = translation;
        } else {
            data[0] = word;
            data[1] = translation;
        }

        return new Word(group, data);
    }

    private String readText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws Exception {
        if (parser.getEventType() != XmlPullParser.START_TAG)
            throw new IllegalStateException();

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
