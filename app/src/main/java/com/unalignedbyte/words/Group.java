package com.unalignedbyte.words;

/**
 * Created by rafal on 11/12/2017.
 */

public class Group
{
    private String name;
    private String languageCode;

    public Group(String name, String languageCode)
    {
        this.name = name;
        this.languageCode = languageCode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLanguageCode()
    {
        return languageCode;
    }

    public void setLanguageCode(String languageCode)
    {
        this.languageCode = languageCode;
    }
}
