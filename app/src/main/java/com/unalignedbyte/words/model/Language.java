package com.unalignedbyte.words.model;

import java.util.*;

/**
 * Created by rafal on 11/12/2017.
 */

public abstract class Language
{
    private String code;
    private String name;

    public static List<Language> getLanguages()
    {
        List<Language> languages = new LinkedList();
        languages.add(new LanguageGeneric());
        languages.add(new LanguageChinese());
        return languages;
    }

    public static Language getLanguage(String code)
    {
        Language returnLanguage = null;
        for(Language language: getLanguages())
            if(language.getCode().equals(code)) {
                returnLanguage = language;
                break;
            }
        return returnLanguage;
    }

    protected Language(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public abstract String[] getWordConfigTitles();
    public abstract String[] getWordDataTitles();
}
