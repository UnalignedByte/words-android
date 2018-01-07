package com.unalignedbyte.words;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
{
    private TextView nameText;
    private TextView wordsCountText;
    private Button menuButton;

    public GroupViewHolder(View view)
    {
        super(view);
        nameText = (TextView)view.findViewById(R.id.groupViewHolderName);
        wordsCountText = (TextView)view.findViewById(R.id.groupViewHolderWordsCountText);
        menuButton = (Button)itemView.findViewById(R.id.group_view_holder_menuButton);
    }

    public Button getMenuButton()
    {
        return menuButton;
    }

    public void setGroup(Group group, int wordsCount)
    {
        nameText.setText(group.getName());
        if(wordsCount == 0) {
            wordsCountText.setText("No Words");
        } else if(wordsCount == 1) {
            wordsCountText.setText("1 Word");
        } else {
            wordsCountText.setText(Integer.toString(wordsCount) + " Words");
        }
    }
}
