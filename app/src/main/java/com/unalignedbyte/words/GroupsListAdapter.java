package com.unalignedbyte.words;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<GroupViewHolder>
{
    private Context context;
    private Group selectedGroup;

    public GroupsListAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.group_view_holder, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder viewHolder, int position)
    {
        final Group group = WordsDataSource.get(context).getGroups().get(position);
        viewHolder.setGroup(group);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WordsListActivity.class);
                intent.putExtra("groupId", group.getId());
                context.startActivity(intent);
            }
        });
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
        return WordsDataSource.get(context).getGroups().size();
    }

    public Group getSelectedGroup()
    {
        return selectedGroup;
    }
}
