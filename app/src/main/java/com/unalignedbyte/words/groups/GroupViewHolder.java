package com.unalignedbyte.words.groups;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;
import com.unalignedbyte.words.utils.*;

import butterknife.*;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener
{
    public interface Listener {
        void contextMenuShown();
    }

    private Listener listener;

    @BindView(R.id.group_view_holder_groupViewGroup)
    ViewGroup groupViewGroup;
    @BindView(R.id.group_view_holder_groupNameText)
    TextView nameText;
    @BindView(R.id.group_view_holder_groupWordsCountText)
    TextView wordsCountText;
    @BindView(R.id.group_view_holder_groupReorderText)
    View reorderText;

    @BindView(R.id.group_view_holder_revisionViewGroup)
    ViewGroup revisionViewGroup;
    @BindView(R.id.group_view_holder_revisionWordsCountText)
    TextView revisionWordsCountText;

    public GroupViewHolder(View view)
    {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnCreateContextMenuListener(this);
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    public void setGroup(Group group, int wordsCount)
    {
        String translatedString = Utils.get().translate("words", wordsCount);
        wordsCountText.setText(translatedString);
    }

    public void setRevisionWordsCount(int wordsCount)
    {
        String translatedString = Utils.get().translate("words", wordsCount);
        revisionWordsCountText.setText(translatedString);
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
        if(listener != null)
            listener.contextMenuShown();
    }

    public View getReorderView()
    {
        return reorderText;
    }
}
