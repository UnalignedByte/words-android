package com.unalignedbyte.words;

import android.content.*;
import android.view.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListAdapter extends RecyclerView.Adapter<GroupViewHolder>
{
    private Context context;
    private RecyclerView recyclerView;
    private ItemTouchHelper touchHelper;
    private Group selectedGroup;

    public GroupsListAdapter(Context context, RecyclerView recyclerView)
    {
        this.context = context;
        this.recyclerView = recyclerView;
        setupDragging();
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
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedGroup = group;
                return false;
            }
        });
        viewHolder.getReorderView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                touchHelper.startDrag(viewHolder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return WordsDataSource.get(context).getGroups().size();
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
                GroupsListAdapter.this.notifyItemMoved(viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());
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
}
