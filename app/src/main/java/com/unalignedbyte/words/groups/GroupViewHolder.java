package com.unalignedbyte.words.groups;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupViewHolder extends RecyclerView.ViewHolder
        implements View.OnCreateContextMenuListener {
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
    private Listener listener;
    public GroupViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnCreateContextMenuListener(this);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setGroup(Group group, int wordsCount) {
        String translatedString = Utils.get().translate("words", wordsCount);
        wordsCountText.setText(translatedString);
        nameText.setText(group.getName());
    }

    public void setRevisionWordsCount(int wordsCount) {
        String translatedString = Utils.get().translate("words", wordsCount);
        revisionWordsCountText.setText(translatedString);
    }

    public void showRevisionView(boolean isShowingRevision) {
        groupViewGroup.setVisibility(isShowingRevision ? View.GONE : View.VISIBLE);
        revisionViewGroup.setVisibility(isShowingRevision ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.add(R.string.menu_edit);
        menu.add(R.string.menu_delete);
        if (listener != null)
            listener.contextMenuShown();
    }

    public View getReorderView() {
        return reorderText;
    }

    public interface Listener {
        void contextMenuShown();
    }
}
