package com.unalignedbyte.words.model;

/**
 * Created by rafal on 11/12/2017.
 */

public class Group
{
    private int id;
    private String name;
    private Language language;
    private int order;

    public Group(String name, Language language)
    {
        this(-1, -1, name, language);
    }

    public Group(int id, int order, String name, Language language)
    {
        this.id = id;
        this.order = order;
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

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
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
