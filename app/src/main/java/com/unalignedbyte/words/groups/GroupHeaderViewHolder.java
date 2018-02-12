package com.unalignedbyte.words.groups;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

import butterknife.*;

/**
 * Created by rafal on 22/01/2018.
 */

public class GroupHeaderViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.group_header_view_holder_nameText)
    TextView nameText;

    public GroupHeaderViewHolder(View view)
    {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void setLanguage(Language language)
    {
        nameText.setText(language.getName());
    }
}
