package com.unalignedbyte.words.model;

import java.io.*;
import java.util.*;

import org.xmlpull.v1.*;

import android.content.*;
import android.util.*;
import android.widget.*;

/**
 * Created by rafal on 04/02/2018.
 */

public class WordsImporter
{
    private static WordsImporter instance;
    private Context context;

    private  WordsImporter(Context context)
    {
        this.context = context;
    }

    public static WordsImporter get(Context context)
    {
        if(instance == null)
            instance = new WordsImporter(context);

        return instance;
    }

    public void importAllWords()
    {
        File filesDir = context.getFilesDir();
        File[] allFiles = filesDir.listFiles();

        List<Group> groups = new LinkedList();
        List<Word> words = new LinkedList();

        for(File file : allFiles) {
            try {
                InputStream stream = new FileInputStream(file);
                importGroupsFromInputStream(stream, groups, words);
                stream.close();
            } catch(Exception exception) {
                Log.d("Exception", exception.toString());
            }
        }

        for(Group group : groups)
            WordsDataSource.get(context).addGroup(group);
        for(Word word : words)
            WordsDataSource.get(context).addWord(word);

        Toast.makeText(context, "Imported "+groups.size()+" group(s) and "+words.size()+" word(s)", Toast.LENGTH_SHORT).show();
    }

    private void importGroupsFromInputStream(InputStream stream, List<Group> groups, List<Word> words) throws Exception
    {
        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);

        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName().toLowerCase();
            if(name.equals("dict")) {
                importGroupFromParser(parser, groups, words);
            }
        }
    }

    private void importGroupFromParser(XmlPullParser parser, List<Group> groups, List<Word> words) throws Exception
    {
        String groupName = null;
        Language groupLanguage = null;

        String key = null;

        parser.require(XmlPullParser.START_TAG, null, "dict");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName().toLowerCase();
            if(name.equals("key")) {
                key = readText(parser).toLowerCase();
            } else if(name.equals("string") && key.equals("group")) {
                groupName = readText(parser);
            } else if(name.equals("string") && key.equals("languagecode")) {
                String value = readText(parser);
                groupLanguage = Language.getLanguage(value);
            } else if(name.equals("array") && key.equals("words") && groupName != null && groupLanguage != null) {
                Group group = new Group(groupName, groupLanguage);
                groups.add(group);
                importWordsFromParser(parser, group, words);
            } else {
                skip(parser);
            }
        }
    }

    private void importWordsFromParser(XmlPullParser parser, Group group, List<Word> words) throws Exception
    {
        parser.require(XmlPullParser.START_TAG, null, "array");
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();
            if(name.toLowerCase().equals("dict")) {
                Word word = readWord(parser, group);
                words.add(word);
            } else {
                skip(parser);
            }
        }
    }

    private Word readWord(XmlPullParser parser, Group group) throws Exception
    {
        parser.require(XmlPullParser.START_TAG, null, "dict");

        List<String> data = new LinkedList();
        while(parser.next() != XmlPullParser.END_TAG) {
            if(parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();
            if(name.toLowerCase().equals("string")) {
                String stringData = readString(parser);
                data.add(stringData);
            } else {
                skip(parser);
            }
        }

        String[] dataArray = new String[data.size()];
        for(int i = 0; i<dataArray.length; i++) {
            dataArray[i] = data.get(i);
        }
        Word word = new Word(group, dataArray);
        return word;
    }

    private String readString(XmlPullParser parser) throws Exception
    {
        parser.require(XmlPullParser.START_TAG, null, "string");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "string");
        return text;
    }

    private String readText(XmlPullParser parser) throws Exception
    {
        String result = "";
        if(parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws Exception
    {
        if(parser.getEventType() != XmlPullParser.START_TAG)
            throw new IllegalStateException();

        int depth = 1;
        while(depth != 0) {
            switch(parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth ++;
                    break;
            }
        }
    }
}
