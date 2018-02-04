package com.unalignedbyte.words.model;

import java.io.*;

import android.content.*;
import android.util.Xml;

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

        for(File file : allFiles) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
            } catch(FileNotFoundException exception) {
            }
        }
    }
}
