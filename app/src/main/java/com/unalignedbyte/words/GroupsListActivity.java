package com.unalignedbyte.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
{
    private WordsDataSource dataSource;
    private View contentView;
    private GroupsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataSource = new WordsDataSource(this);

        setContentView(R.layout.activity_groups_list);
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groupsListView);
        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupsListAdapter(dataSource);
        groupsListView.setAdapter(adapter);
        registerForContextMenu(groupsListView);
        contentView = groupsListView;

        FloatingActionButton addGroupButton = (FloatingActionButton)findViewById(R.id.addGroupButton);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showEditGroupActivity(null);
            }
        });
    }

    private void showEditGroupActivity(Group group)
    {
        EditGroupPopupWindow popup = new EditGroupPopupWindow(this, dataSource, group);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                adapter.notifyDataSetChanged();
            }
        });
        popup.showAtLocation(contentView, Gravity.TOP, 0, 0);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        if(item.getTitle().equals(getResources().getString(R.string.menu_edit))) {
            showEditGroupActivity(adapter.getSelectedGroup());
            return true;
        } else if(item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            dataSource.deleteGroup(adapter.getSelectedGroup());
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }
}
