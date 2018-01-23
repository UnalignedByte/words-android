package com.unalignedbyte.words.groups;

import android.content.*;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;
import com.unalignedbyte.words.words.WordsListActivity;

import io.github.luizgrp.sectionedrecyclerviewadapter.*;

/**
 * Created by rafal on 22/01/2018.
 */

public class GroupsListSection extends StatelessSection
{
    public interface Listener {
        void selectedGroup(Group group);
    }

    private Context context;
    private SectionedRecyclerViewAdapter adapter;
    private Language language;
    private boolean isSelected;
    private Listener listener;

    public GroupsListSection(Context context, SectionedRecyclerViewAdapter adapter,
                             Language language, boolean isSelected)
    {
        super(new SectionParameters.Builder(R.layout.group_view_holder)
                .headerResourceId(R.layout.group_header_view_holder).build());
        this.context = context;
        this.adapter = adapter;
        this.language = language;
        this.isSelected = isSelected;
    }

    public boolean getIsSelected()
    {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
        adapter.notifyDataSetChanged();
    }

    public Listener getListener()
    {
        return listener;
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }

    @Override
    public int getContentItemsTotal()
    {
        int count = 0;
        if(isSelected)
            count += WordsDataSource.get(context).getGroups(language).size();
        return count;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {
        return new GroupViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {
        return new GroupHeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        final GroupViewHolder groupViewHolder = (GroupViewHolder)viewHolder;

        final Group group = WordsDataSource.get(context).getGroups(language).get(position);
        int wordsCount = WordsDataSource.get(context).getWords(group).size();
        groupViewHolder.setGroup(group, wordsCount);
        groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WordsListActivity.class);
                intent.putExtra("groupId", group.getId());
                context.startActivity(intent);
            }
        });
        groupViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener != null)
                    listener.selectedGroup(group);
                return false;
            }
        });
        /*groupViewHolder.getReorderView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                touchHelper.startDrag(groupViewHolder);
                return false;
            }
        });*/
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder)
    {
        GroupHeaderViewHolder headerViewHolder = (GroupHeaderViewHolder)viewHolder;
        headerViewHolder.setLanguage(language);
    }
}
