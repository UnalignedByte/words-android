package com.unalignedbyte.words;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<GroupViewHolder>
{
    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_view_holder, parent, false);
        //View view = new View(parent.getContext());
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position)
    {
    }

    @Override
    public int getItemCount()
    {
        return 5;
    }
}
