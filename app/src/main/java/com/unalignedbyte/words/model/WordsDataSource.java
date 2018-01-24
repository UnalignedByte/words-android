package com.unalignedbyte.words.model;

import java.util.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;

/**
 * Created by rafal on 11/12/2017.
 */

public class WordsDataSource extends SQLiteOpenHelper
{
    private static WordsDataSource instance;
    private static final String DB_NAME = "words.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_GROUPS = "groups";
    private static final String GROUPS_ID = "id";
    private static final String GROUPS_NAME = "group_name";
    private static final String GROUPS_LANGUAGE_CODE = "language_code";
    private static final String GROUPS_ORDER = "position";

    private static final String TABLE_WORDS = "words";
    private static final String WORDS_ID = "id";
    private static final String WORDS_GROUP_ID = "group_id";
    private static final String WORDS_WORD_DATA_ID= "words_data_id";
    private static final String WORDS_IS_IN_REVIEW = "is_in_review";

    private static final String WORDS_DATA_ID = "id";

    private WordsDataSource(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static WordsDataSource get(Context context)
    {
        if(instance == null)
            instance = new WordsDataSource(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createGroupsTable = "create table " + TABLE_GROUPS + " (" +
                GROUPS_ID + " integer primary key," +
                GROUPS_ORDER + " integer," +
                GROUPS_NAME + " text," +
                GROUPS_LANGUAGE_CODE + " text)";
        db.execSQL(createGroupsTable);

        String createWordsTable = "create table " + TABLE_WORDS + " (" +
                WORDS_ID + " integer primary key," +
                WORDS_GROUP_ID + " integer," +
                WORDS_WORD_DATA_ID + " text," +
                WORDS_IS_IN_REVIEW + " integer)";

        db.execSQL(createWordsTable);

        for(Language language : Language.getLanguages()) {
            String createWordsDataTable = "create table " + language.getCode() + " (" +
                    WORDS_DATA_ID + " integer primary key";

            for(String dataTitle : language.getWordDataTitles())
                createWordsDataTable += "," + dataTitle + " text";

            createWordsDataTable += ")";

            db.execSQL(createWordsDataTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        String dropGroupsTable = "drop table if exists " + TABLE_GROUPS;
        db.execSQL(dropGroupsTable);

        String dropWordsTable = "drop table if exists " + TABLE_WORDS;
        db.execSQL(dropWordsTable);

        onCreate(db);
    }

    public void addGroup(Group group)
    {
        int order = maxOrderForGroup(group) + 1;
        group.setOrder(order);

        ContentValues values = new ContentValues();
        values.put(GROUPS_ORDER, group.getOrder());
        values.put(GROUPS_NAME, group.getName());
        values.put(GROUPS_LANGUAGE_CODE, group.getLanguage().getCode());

        SQLiteDatabase db = getWritableDatabase();
        int newId = (int)db.insert(TABLE_GROUPS, null, values);
        db.close();
        group.setId(newId);
    }

    public Group getGroup(int groupId)
    {
        String getGroup = "select * from " + TABLE_GROUPS +
                " where " + GROUPS_ID + "=" + Integer.toString(groupId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroup, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int order = cursor.getInt(1);
            String name = cursor.getString(2);
            String languageCode = cursor.getString(3);
            Group group = new Group(id, order, name, Language.getLanguage(languageCode));
            return group;
        }

        return null;
    }

    public List<Group> getGroups(Language language)
    {
        List<Group> groups = new LinkedList();

        String getGroups = "select * from " + TABLE_GROUPS +
                " where " + GROUPS_LANGUAGE_CODE + "=\"" + language.getCode() + "\"" +
                " order by " + GROUPS_ORDER + " desc";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroups, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int order = cursor.getInt(1);
                String name = cursor.getString(2);
                String languageCode = cursor.getString(3);
                Group group = new Group(id, order, name, Language.getLanguage(languageCode));
                groups.add(group);
            } while(cursor.moveToNext());
        }

        return groups;
    }

    public void updateGroup(Group group)
    {
        ContentValues values = new ContentValues();
        values.put(GROUPS_ORDER, group.getOrder());
        values.put(GROUPS_NAME, group.getName());
        values.put(GROUPS_LANGUAGE_CODE, group.getLanguage().getCode());

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(group.getId());
        db.update(TABLE_GROUPS, values, GROUPS_ID + "=?", new String[] { idString });
        db.close();
    }

    public void deleteGroup(Group group)
    {
        deleteWordsInGroup(group);

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(group.getId());
        db.delete(TABLE_GROUPS, GROUPS_ID + "=?", new String[] {idString});
        db.close();
    }

    public void addWord(Word word)
    {
        addWordData(word);

        ContentValues values = new ContentValues();
        values.put(WORDS_GROUP_ID, word.getGroup().getId());
        values.put(WORDS_WORD_DATA_ID, word.getWordDataId());
        values.put(WORDS_IS_IN_REVIEW, word.getIsInReview());

        SQLiteDatabase db = getWritableDatabase();
        int wordId = (int)db.insert(TABLE_WORDS, null, values);
        db.close();

        word.setId(wordId);
    }

    public List<Word> getWords(Group group)
    {
        List<Word> words = new LinkedList();

        String getWords = "select * from " + TABLE_WORDS +
                " where " + WORDS_GROUP_ID + "=" + Integer.toString(group.getId());
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getWords, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int wordDataId = cursor.getInt(2);
                String[] wordData = getWordData(wordDataId, group.getLanguage());
                boolean isInReview = cursor.getInt(3) != 0;
                Word word = new Word(id, group, wordDataId, wordData, isInReview);
                words.add(word);
            } while(cursor.moveToNext());
        }

        return words;
    }

    public List<Word> getWordsInRevision(Language language)
    {
        List<Word> words = new LinkedList();

        String getWords = "select * from " + TABLE_WORDS +
                " join " + TABLE_GROUPS + " on " +
                WORDS_GROUP_ID + "=" + TABLE_GROUPS + "." + GROUPS_ID +
                " where " + GROUPS_LANGUAGE_CODE + "=\"" + language.getCode() + "\" and " +
                WORDS_IS_IN_REVIEW + "=1";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getWords, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int groupId = cursor.getInt(1);
                Group group = getGroup(groupId);
                int wordDataId = cursor.getInt(2);
                String[] wordData = getWordData(wordDataId, language);
                boolean isInReview = cursor.getInt(3) != 0;
                Word word = new Word(id, group, wordDataId, wordData, isInReview);
                words.add(word);
            } while(cursor.moveToNext());
        }

        return words;
    }

    public void updateWord(Word word)
    {
        updateWordData(word);

        ContentValues values = new ContentValues();
        values.put(WORDS_IS_IN_REVIEW, word.getIsInReview());

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(word.getId());
        db.update(TABLE_WORDS, values, WORDS_ID + "=?", new String[] { idString });
        db.close();;
    }

    public void deleteWord(Word word)
    {
        deleteWordData(word);

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(word.getId());
        db.delete(TABLE_WORDS, WORDS_ID + "=?", new String[] { idString });
        db.close();;
    }

    private void deleteWordsInGroup(Group group)
    {
        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(group.getId());
        db.delete(TABLE_WORDS, WORDS_GROUP_ID + "=?", new String[] { idString });
        db.close();
    }

    private void addWordData(Word word)
    {
        ContentValues values = new ContentValues();
        for(int i=0; i<word.getGroup().getLanguage().getWordDataTitles().length; i++) {
            String field = word.getGroup().getLanguage().getWordDataTitles()[i];
            values.put(field, word.getWordData()[i]);
        }

        SQLiteDatabase db = getWritableDatabase();
        int dataId = (int)db.insert(word.getGroup().getLanguage().getCode(), null, values);
        db.close();

        word.setWordDataId(dataId);
    }

    private String[] getWordData(int wordDataId, Language language)
    {
        String getWordData = "select * from " + language.getCode() +
                " where " + WORDS_DATA_ID + "=" + Integer.toString(wordDataId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getWordData, null);

        if(cursor.moveToFirst()) {
            String[] wordData = new String[language.getWordDataTitles().length];

            for(int i=0; i<language.getWordDataTitles().length; i++) {
                String data = cursor.getString(i+1);
                wordData[i] = data;
            }

            return wordData;
        }

        return null;
    }

    private void updateWordData(Word word)
    {
        ContentValues values = new ContentValues();
        for(int i=0; i<word.getGroup().getLanguage().getWordDataTitles().length; i++) {
            String wordDataTitle = word.getGroup().getLanguage().getWordDataTitles()[i];
            values.put(wordDataTitle, word.getWordData()[i]);
        }

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(word.getWordDataId());
        db.update(word.getGroup().getLanguage().getCode(), values,
                WORDS_DATA_ID + "=?", new String[] { idString });
        db.close();
    }

    private void deleteWordData(Word word)
    {
        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(word.getWordDataId());
        db.delete(word.getGroup().getLanguage().getCode(), WORDS_DATA_ID + "=?",
                new String[] {idString});
        db.close();
    }

    private int maxOrderForGroup(Group group)
    {
        int maxOrder = 0;

        SQLiteDatabase db = getReadableDatabase();
        String getMaxOrder = "select max(" + GROUPS_ORDER + ") from " + TABLE_GROUPS;
        Cursor cursor = db.rawQuery(getMaxOrder, null);
        if(cursor.moveToFirst()) {
            maxOrder = cursor.getInt(0);
        }

        return maxOrder;
    }
}
