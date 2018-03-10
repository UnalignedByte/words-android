package com.unalignedbyte.words.model;

/**
 * Created by rafal on 27/12/2017.
 */

public class Word {
    private int id;
    private Group group;
    private int wordDataId;
    private String[] wordData;
    private boolean isInReview;
    private int order;

    public Word(Group group, String[] wordData) {
        this(-1, group, -1, wordData, false, -1);
    }

    public Word(int id, Group group, int wordDataId, String[] wordData, boolean isInReview, int order) {
        this.id = id;
        this.group = group;
        this.wordDataId = wordDataId;
        this.wordData = wordData;
        this.isInReview = isInReview;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public int getWordDataId() {
        return wordDataId;
    }

    public void setWordDataId(int wordDataId) {
        this.wordDataId = wordDataId;
    }

    public String[] getWordData() {
        return wordData;
    }

    public void setWordData(String[] wordData) {
        this.wordData = wordData;
    }

    public boolean getIsInReview() {
        return isInReview;
    }

    public void setIsInReview(boolean inReview) {
        isInReview = inReview;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
