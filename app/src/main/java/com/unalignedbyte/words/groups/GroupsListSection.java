package com.unalignedbyte.words.groups;

import java.util.*;

import android.content.*;
import android.view.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;
import com.unalignedbyte.words.words.*;

import io.github.luizgrp.sectionedrecyclerviewadapter.*;

/**
 * Created by rafal on 22/01/2018.
 */

public class GroupsListSection extends StatelessSection
    implements GroupViewHolder.Listener
{
    public interface Listener {
        void selectedGroup(Group group);
        void selectedSection(Language language);
    }

    private Context context;
    private SectionedRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Language language;
    private boolean isSelected;
    private Listener listener;
    private ItemTouchHelper touchHelper;
    private int firstItemPosition = 0;

    public GroupsListSection(Context context, SectionedRecyclerViewAdapter adapter,
                             RecyclerView recyclerView, Language language, boolean isSelected)
    {
        super(new SectionParameters.Builder(R.layout.group_view_holder)
                .headerResourceId(R.layout.group_header_view_holder).build());
        this.context = context;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
        this.language = language;
        this.isSelected = isSelected;
        setupDragging();
    }

    public Language getLanguage()
    {
        return language;
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

    private void setupDragging()
    {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int sourcePosition = viewHolder.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();

                boolean shouldMove = targetPosition >= getLowerPositionBounds() &&
                        targetPosition < getUpperPositionBounds();

                if(shouldMove) {
                    moveGroup(sourcePosition, targetPosition);
                    adapter.notifyItemMoved(sourcePosition, targetPosition);
                }

                return shouldMove;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }
        };

        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public int getContentItemsTotal()
    {
        int count = 0;
        if(isSelected)
            count += WordsDataSource.get(context).getGroupsCount(language);
        if(isSelected && doesContainRevision())
            count += 1;
        return count;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {
        GroupViewHolder viewHolder = new GroupViewHolder(view);
        viewHolder.setListener(this);

        return viewHolder;
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {
        return new GroupHeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        if(position == 0) {
            firstItemPosition = viewHolder.getAdapterPosition();
        }

        final GroupViewHolder groupViewHolder = (GroupViewHolder) viewHolder;

        if(position == 0 && doesContainRevision()) {
            groupViewHolder.showRevisionView(true);

            int wordsCount = WordsDataSource.get(context).getWordsInRevisionCount(language);
            groupViewHolder.setRevisionWordsCount(wordsCount);

            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, RevisionActivity.class);
                    intent.putExtra("languageCode", language.getCode());
                    context.startActivity(intent);
                }
            });
        } else {
            groupViewHolder.showRevisionView(false);

            if(doesContainRevision())
                position--;

            final Group group = WordsDataSource.get(context).getGroups(language).get(position);
            int wordsCount = WordsDataSource.get(context).getWordsCount(group);
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
                    if (listener != null)
                        listener.selectedGroup(group);
                    return false;
                }
            });
            groupViewHolder.getReorderView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    touchHelper.startDrag(groupViewHolder);
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder)
    {
        GroupHeaderViewHolder headerViewHolder = (GroupHeaderViewHolder)viewHolder;
        headerViewHolder.setLanguage(language);
        headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.selectedSection(isSelected ? null : language);
            }
        });
    }

    private boolean doesContainRevision()
    {
        return WordsDataSource.get(context).getWordsInRevisionCount(language) > 0;
    }

    private int getLowerPositionBounds()
    {
        int position = firstItemPosition;
        if(doesContainRevision())
            position++;
        return position;
    }

    private int getUpperPositionBounds()
    {
        int itemsCount = WordsDataSource.get(context).getGroupsCount(language);
        return getLowerPositionBounds() + itemsCount;
    }

    private void moveGroup(int sourceIndex, int targetIndex)
    {
        if(sourceIndex == targetIndex)
            return;

        sourceIndex -= getLowerPositionBounds();
        targetIndex -= getLowerPositionBounds();

        List<Group> groups = WordsDataSource.get(context).getGroups(language);

        Group updatedGroup = groups.remove(sourceIndex);
        groups.add(targetIndex, updatedGroup);

        for(int order=groups.size(); order>0; order--) {
            int index = groups.size() - order;
            Group group = groups.get(index);
            group.setOrder(order);
            WordsDataSource.get(context).updateGroup(group);
        }
    }

    @Override
    public void contextMenuShown()
    {
        MotionEvent event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0, 0, 0);
        recyclerView.dispatchTouchEvent(event);
    }
}
