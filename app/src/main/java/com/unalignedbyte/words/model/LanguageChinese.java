package com.unalignedbyte.words.model;

/**
 * Created by rafal on 22/01/2018.
 */

public class LanguageChinese extends Language
{
    public LanguageChinese()
    {
        super("cn", "中文");
    }

    @Override
    public String[] getWordConfigTitles()
    {
        return new String[] {"所有", "汉字", "拼音", "Translation"};
    }

    @Override
    public String[] getWordDataTitles()
    {
        return new String[] {"hanzi", "pinyin", "translation"};
    }
}
