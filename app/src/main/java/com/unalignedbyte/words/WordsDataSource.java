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
    private static final String DB_NAME = "words.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_GROUPS = "groups";
    private static final String GROUP_ID = "id";
    private static final String GROUP_NAME = "group_name";
    private static final String LANGUAGE_CODE = "language_code";

    public WordsDataSource(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTable = "CREATE TABLE " + TABLE_GROUPS + " ( " +
                GROUP_ID + " INTEGER PRIMARY KEY," +
                GROUP_NAME + " TEXT, " +
                LANGUAGE_CODE + " TEXT )";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_GROUPS;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public List<Language> getLanguages()
    {
        List<Language> languageCodes = new LinkedList<Language>();
        languageCodes.add(new Language("cn", "Chinese"));
        languageCodes.add(new Language("gn", "Generic"));

        return languageCodes;
    }



    public void addGroup(Group group)
    {
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, group.getName());
        values.put(LANGUAGE_CODE, group.getLanguage().getCode());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_GROUPS, null, values);
        db.close();
    }

    public void updateGroup(Group group)
    {
        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, group.getName());
        values.put(LANGUAGE_CODE, group.getLanguage().getCode());

        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(group.getId());
        db.update(TABLE_GROUPS, values, GROUP_ID + " = ?", new String[] { idString });
        db.close();
    }

    public void deleteGroup(Group group)
    {
        SQLiteDatabase db = getWritableDatabase();
        String idString = Integer.toString(group.getId());
        db.delete(TABLE_GROUPS, GROUP_ID + " = ?", new String[] { idString });
        db.close();
    }

    public List<Group> getGroups()
    {
        List<Group> groups = new LinkedList();

        String getGroups = "SELECT * FROM " + TABLE_GROUPS;
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
}
