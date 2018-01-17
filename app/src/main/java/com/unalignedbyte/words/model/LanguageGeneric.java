package com.unalignedbyte.words.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rafal on 17/01/2018.
 */

public class LanguageGeneric extends Language
{
    public LanguageGeneric()
    {
        super("gn", "Generic");
    }

    @Override
    public List<String> getWordConfigTitles()
    {
        List<String> titles = new LinkedList();
        titles.add("Both");
        titles.add("Word");
        titles.add("Translation");
        return titles;
    }
}
