package com.unalignedbyte.words.utils;

import android.content.*;

/**
 * Created by rafal on 21/02/2018.
 */

public class Utils
{
    private static Utils instance;
    private Context context;

    private Utils()
    {
    }

    public static Utils get()
    {
        if(instance == null)
            instance = new Utils();

        return instance;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public String translate(String string)
    {
        int stringId = context.getResources().getIdentifier(string.toLowerCase(), "string",
            context.getPackageName());
        if(stringId > 0)
            return context.getResources().getString(stringId);

        return string;
    }
}
