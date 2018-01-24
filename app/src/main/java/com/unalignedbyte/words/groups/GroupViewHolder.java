package com.unalignedbyte.words.groups;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener
{
    private ViewGroup groupViewGroup;
    private TextView nameText;
    private TextView wordsCountText;
    private View reorderText;

    private ViewGroup revisionViewGroup;
    private TextView revisionWordsCountText;

    public GroupViewHolder(View view)
    {
        super(view);
        groupViewGroup = (ViewGroup)view.findViewById(R.id.group_view_holder_groupViewGroup);
        nameText = (TextView)view.findViewById(R.id.group_view_holder_groupNameText);
        wordsCountText = (TextView)view.findViewById(R.id.group_view_holder_groupWordsCountText);
        reorderText= view.findViewById(R.id.group_view_holder_groupReorderText);
        view.setOnCreateContextMenuListener(this);

        revisionViewGroup = (ViewGroup)view.findViewById(R.id.group_view_holder_revisionViewGroup);
        revisionWordsCountText = (TextView)view.findViewById(R.id.group_view_holder_revisionWordsCountText);
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

    public void setRevisionWordsCount(int wordsCount)
    {
        if(wordsCount == 0) {
            revisionWordsCountText.setText("No Words");
        } else if(wordsCount == 1) {
            revisionWordsCountText.setText("1 Word");
        } else {
            revisionWordsCountText.setText(Integer.toString(wordsCount) + " Words");
        }
    }

    public void showRevisionView(boolean isShowingRevision)
    {
        groupViewGroup.setVisibility(isShowingRevision ? View.GONE : View.VISIBLE);
        revisionViewGroup.setVisibility(isShowingRevision ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
    {
        menu.add(R.string.menu_edit);
        menu.add(R.string.menu_delete);
    }

    public View getReorderView()
    {
        return reorderText;
    }
}
