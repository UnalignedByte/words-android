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
    private WordsDataSource dataSource;
    private Group selectedGroup;

    public GroupsListAdapter(WordsDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_view_holder, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder viewHolder, int position)
    {
        final Group group = dataSource.getGroups().get(position);
        viewHolder.setGroup(group);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedGroup = group;
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return dataSource.getGroups().size();
    }

    public Group getSelectedGroup()
    {
        return selectedGroup;
    }
}
