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
    public String[] getWordConfigTitles()
    {
        return new String[] {"Both", "Word", "Translation"};
    }

    @Override
    public String[] getWordDataTitles()
    {
        return new String[] {"word", "translation"};
    }
}
