package com.unalignedbyte.words;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<GroupViewHolder>
{
    private Context context;
    private Group selectedGroup;
    private PopupMenu.OnMenuItemClickListener menuListener;

    public GroupsListAdapter(Context context, PopupMenu.OnMenuItemClickListener menuListener)
    {
        this.context = context;
        this.menuListener = menuListener;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.group_view_holder, parent, false);
        GroupViewHolder viewHolder = new GroupViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final GroupViewHolder viewHolder, int position)
    {
        final Group group = WordsDataSource.get(context).getGroups().get(position);
        int wordsCount = WordsDataSource.get(context).getWords(group).size();
        viewHolder.setGroup(group, wordsCount);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WordsListActivity.class);
                intent.putExtra("groupId", group.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.getMenuButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedGroup = group;
                showPopupMenu(viewHolder.getMenuButton());
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

    private void showPopupMenu(View view)
    {
        PopupMenu menu = new PopupMenu(context, view);
        menu.inflate(R.menu.edit_delete_menu);
        menu.setOnMenuItemClickListener(menuListener);
        menu.show();
    }
}
