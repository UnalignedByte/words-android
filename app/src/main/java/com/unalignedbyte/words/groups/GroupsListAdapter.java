package com.unalignedbyte.words.groups;

import java.util.*;

import android.content.*;
import android.view.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;
import com.unalignedbyte.words.words.RevisionActivity;
import com.unalignedbyte.words.words.WordsListActivity;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;
    private Group selectedGroup;

    private static final int CELL_TYPE_NORMAL = 1;
    private static final int CELL_TYPE_REVISION= 2;

    public GroupsListAdapter(Context context, RecyclerView recyclerView)
    {
        this.context = context;
        this.recyclerView = recyclerView;
        setupDragging();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == CELL_TYPE_REVISION) {
            View view = LayoutInflater.from(context).inflate(R.layout.revision_view_holder, parent, false);
            viewHolder = new RevisionViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.group_view_holder, parent, false);
            viewHolder = new GroupViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position)
    {
        if(viewHolder.getItemViewType() == CELL_TYPE_REVISION) {
            final RevisionViewHolder revisionViewHolder = (RevisionViewHolder)viewHolder;
            int wordsCount = WordsDataSource.get(context).getWordsInRevision(getLanguage()).size();
            revisionViewHolder.setWordsCount(wordsCount);
            revisionViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, RevisionActivity.class);
                    intent.putExtra("languageCode", getLanguage().getCode());
                    context.startActivity(intent);
                }
            });
        } else {
            if (doesContainRevision())
                position--;

            final GroupViewHolder groupViewHolder = (GroupViewHolder)viewHolder;

            final Group group = WordsDataSource.get(context).getGroups().get(position);
            int wordsCount = WordsDataSource.get(context).getWords(group).size();
            groupViewHolder.setGroup(group, wordsCount);
            groupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, WordsListActivity.class);
                    intent.putExtra("groupId", group.getId());
                    context.startActivity(intent);
                }
            });
            groupViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    selectedGroup = group;
                    return false;
                }
            });
            groupViewHolder.getReorderView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    touchHelper.startDrag(groupViewHolder);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == 0 && doesContainRevision())
            return CELL_TYPE_REVISION;

        return CELL_TYPE_NORMAL;
    }

    @Override
    public int getItemCount()
    {
        int count = WordsDataSource.get(context).getGroups().size();
        if(doesContainRevision())
            count++;
        return count;
    }

    private void setupDragging()
    {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                int sourceIndex = viewHolder.getAdapterPosition();
                int targetIndex = target.getAdapterPosition();
                moveGroup(sourceIndex, targetIndex);
                GroupsListAdapter.this.notifyItemMoved(sourceIndex, targetIndex);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public boolean isLongPressDragEnabled()
            {
                return false;
            }
        };

        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public Group getSelectedGroup()
    {
        return selectedGroup;
    }

    private void moveGroup(int sourceIndex, int targetIndex)
    {
        if(sourceIndex == targetIndex)
            return;

        List<Group> groups = WordsDataSource.get(context).getGroups();

        Group updatedGroup = groups.remove(sourceIndex);
        groups.add(targetIndex, updatedGroup);

        for(int order=groups.size(); order>0; order--) {
            int index = groups.size() - order;
            Group group = groups.get(index);
            group.setOrder(order);
            WordsDataSource.get(context).updateGroup(group);
        }
    }

    private boolean doesContainRevision()
    {
        Language language = Language.getLanguages().get(0);
        return WordsDataSource.get(context).getWordsInRevision(language).size() > 0;
    }

    private Language getLanguage()
    {
        return Language.getLanguages().get(0);
    }
}
