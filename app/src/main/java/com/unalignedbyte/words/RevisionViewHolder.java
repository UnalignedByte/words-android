package com.unalignedbyte.words;

import android.view.*;
import android.support.v7.widget.*;
import android.widget.TextView;

/**
 * Created by rafal on 16/01/2018.
 */

public class RevisionViewHolder extends RecyclerView.ViewHolder
{
    private TextView wordsCountText;

    public RevisionViewHolder(View view)
    {
        super(view);
        wordsCountText = (TextView)view.findViewById(R.id.revision_view_holder_wordsCountText);
    }

    public void setWordsCount(int wordsCount)
    {
        if(wordsCount == 0) {
            wordsCountText.setText("No Words");
        } else if(wordsCount == 1) {
            wordsCountText.setText("1 Word");
        } else {
            wordsCountText.setText(Integer.toString(wordsCount) + " Words");
        }
    }
}
