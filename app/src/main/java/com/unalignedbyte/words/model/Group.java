package com.unalignedbyte.words.model;

/**
 * Created by rafal on 11/12/2017.
 */

public class Group
{
    private int id;
    private String name;
    private Language language;

    public Group(String name, Language language)
    {
        this(-1, name, language);
    }

    public Group(int id, String name, Language language)
    {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Language getLanguage()
    {
        return language;
    }

    public void setLanguageCode(Language language)
    {
        this.language = language;
    }
}
