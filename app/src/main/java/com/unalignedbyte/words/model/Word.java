package com.unalignedbyte.words.model;

/**
 * Created by rafal on 27/12/2017.
 */

public class Word {
    private int id;
    private String word;
    private String translation;
    private boolean isInReview;
    private Group group;

    public Word(Group group, String word, String translation)
    {
        this(-1, group, false, word, translation);
    }

    public Word(int id, Group group, boolean isInReview, String word, String translation)
    {
        this.id = id;
        this.group = group;
        this.isInReview = isInReview;
        this.word = word;
        this.translation = translation;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Group getGroup()
    {
        return group;
    }

    public boolean getIsInReview()
    {
        return isInReview;
    }

    public void setIsInReview(boolean inReview)
    {
        isInReview = inReview;
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
}
