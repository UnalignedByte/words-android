package com.unalignedbyte.words;

/**
 * Created by rafal on 27/12/2017.
 */

public class Word {
    private int id;
    private String word;
    private String translation;
    private Group group;

    public Word(String word, String translation, Group group)
    {
        this(-1, word, translation, group);
    }

    public Word(int id, String word, String translation, Group group)
    {
        this.id = id;
        this.word = word;
        this.translation = translation;
        this.group = group;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public String getTranslation()
    {
        return translation;
    }

    public void setTranslation(String translation)
    {
        this.translation = translation;
    }

    public Group getGroup()
    {
        return group;
    }
}
