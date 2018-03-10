package com.unalignedbyte.words.groups;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Language;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rafal on 22/01/2018.
 */

public class GroupHeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.group_header_view_holder_nameText)
    TextView nameText;

    public GroupHeaderViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void setLanguage(Language language) {
        nameText.setText(language.toString());
    }
}
