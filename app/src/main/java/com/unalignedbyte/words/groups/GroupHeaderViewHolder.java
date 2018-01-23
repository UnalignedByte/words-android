package com.unalignedbyte.words.groups;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 22/01/2018.
 */

public class GroupHeaderViewHolder extends RecyclerView.ViewHolder
{
    private TextView nameText;

    public GroupHeaderViewHolder(View view)
    {
        super(view);
        nameText = (TextView)view.findViewById(R.id.group_header_view_holder_nameText);
    }

    public void setLanguage(Language language)
    {
        nameText.setText(language.getName());
    }
}
