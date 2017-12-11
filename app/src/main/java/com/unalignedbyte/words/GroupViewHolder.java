package com.unalignedbyte.words;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
{
    private TextView nameText;
    private TextView wordsCountText;

    public GroupViewHolder(View view)
    {
        super(view);
        nameText = (TextView)view.findViewById(R.id.groupViewHolderName);
        wordsCountText = (TextView)view.findViewById(R.id.groupViewHolderWordsCountText);
    }

    public void setGroup(Group group)
    {
        nameText.setText(group.getName());
    }
}
