package com.unalignedbyte.words;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

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

    private static final String TABLE_WORDS = "words";
    private static final String WORDS_ID = "id";
    private static final String WORDS_GROUP_ID = "group_id";
    private static final String WORDS_WORD = "word";
    private static final String WORDS_TRANSLATION = "translation";

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
                GROUPS_NAME + " text," +
                GROUPS_LANGUAGE_CODE + " text)";
        db.execSQL(createGroupsTable);

        String createWordsTable = "create table " + TABLE_WORDS + " (" +
                WORDS_ID + " integer primary key," +
                WORDS_GROUP_ID + " integer," +
                WORDS_WORD + " text," +
                WORDS_TRANSLATION + " text)";
        db.execSQL(createWordsTable);

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

    public List<Language> getLanguages()
    {
        List<Language> languageCodes = new LinkedList<Language>();
        languageCodes.add(new Language("gn", "Generic"));

        return languageCodes;
    }

    public void addGroup(Group group)
    {
        ContentValues values = new ContentValues();
        values.put(GROUPS_NAME, group.getName());
        values.put(GROUPS_LANGUAGE_CODE, group.getLanguage().getCode());

        SQLiteDatabase db = getWritableDatabase();
        int newId = (int)db.insert(TABLE_GROUPS, null, values);
        db.close();
        group.setId(newId);
    }

    public void updateGroup(Group group)
    {
        ContentValues values = new ContentValues();
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
        db.delete(TABLE_GROUPS, GROUPS_ID + " = ?", new String[] { idString });
        db.close();
    }

    public List<Group> getGroups()
    {
        List<Group> groups = new LinkedList();

        String getGroups = "select * from " + TABLE_GROUPS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroups, null);

        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String languageCode = cursor.getString(2);
                Group group = new Group(id, name, new Language(languageCode, languageCode));
                groups.add(group);
            } while(cursor.moveToNext());
        }

        return groups;
    }

    public Group getGroup(int groupId)
    {
        String getGroup = "select * from " + TABLE_GROUPS +
                " where " + GROUPS_ID + "=" + Integer.toString(groupId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroup, null);

        if(cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String languageCode = cursor.getString(2);
            Group group = new Group(id, name, new Language(languageCode, languageCode));
            return group;
        }

        return null;
    }

    public void addWord(Word word)
    {
        ContentValues values = new ContentValues();
        values.put(WORDS_GROUP_ID, word.getGroup().getId());
        values.put(WORDS_WORD, word.getWord());
        values.put(WORDS_TRANSLATION, word.getTranslation());

        SQLiteDatabase db = getWritableDatabase();
        int newId = (int)db.insert(TABLE_WORDS, null, values);
        db.close();

        word.setId(newId);
    }

    public void updateWord(Word word)
    {
        ContentValues values = new ContentValues();
        values.put(WORDS_WORD, word.getWord());
        values.put(WORDS_TRANSLATION, word.getTranslation());

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(word.getId());
        db.update(TABLE_WORDS, values, WORDS_ID + "=?", new String[] { idString });
        db.close();;
    }

    public void deleteWord(Word word)
    {
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
                String wordString = cursor.getString(2);
                String translation = cursor.getString(3);
                Word word = new Word(id, wordString, translation, group);
                words.add(word);
            } while(cursor.moveToNext());
        }

        return words;
    }
}
