package com.unalignedbyte.words;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener
{
    private TextView nameText;
    private TextView wordsCountText;

    public GroupViewHolder(View view)
    {
        super(view);
        nameText = (TextView)view.findViewById(R.id.groupViewHolderName);
        wordsCountText = (TextView)view.findViewById(R.id.groupViewHolderWordsCountText);
        view.setOnCreateContextMenuListener(this);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
    {
        menu.add(R.string.menu_edit);
        menu.add(R.string.menu_delete);
    }
}
