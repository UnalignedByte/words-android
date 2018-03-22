package com.unalignedbyte.words;

import android.content.Context;

/**
 * Created by rafal on 21/02/2018.
 */

public class Utils {
    private static Utils instance;

    private Utils() {
    }

    public static Utils get() {
        if (instance == null)
            instance = new Utils();

        return instance;
    }

    public String translate(String string) {
        Context context = MainApplication.getContext();
        int stringId = context.getResources().getIdentifier(string.toLowerCase(), "string",
                context.getPackageName());
        if (stringId > 0)
            return context.getResources().getString(stringId);

        return string;
    }

    public String translate(String string, int count) {
        Context context = MainApplication.getContext();
        int stringId = context.getResources().getIdentifier(string.toLowerCase(), "plurals",
                context.getPackageName());
        if (stringId > 0)
            return context.getResources().getQuantityString(stringId, count, count);

        return Integer.toString(count) + " " + string;
    }
}
