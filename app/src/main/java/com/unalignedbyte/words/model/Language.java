package com.unalignedbyte.words.model;

import com.unalignedbyte.words.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rafal on 11/12/2017.
 */

public abstract class Language {
    private String code;
    private String name;

    protected Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<Language> getLanguages() {
        List<Language> languages = new LinkedList();
        languages.add(new LanguageGeneric());
        languages.add(new LanguageChinese());
        return languages;
    }

    public static Language getLanguage(String code) {
        Language returnLanguage = null;
        for (Language language : getLanguages())
            if (language.getCode().equals(code)) {
                returnLanguage = language;
                break;
            }
        return returnLanguage;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String tranlatedName = Utils.get().translate(name);
        return tranlatedName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Language) {
            return getCode() == ((Language) obj).getCode();
        }

        return false;
    }

    public abstract String[] getWordConfigTitles();

    public abstract String[] getWordDataTitles();
}
