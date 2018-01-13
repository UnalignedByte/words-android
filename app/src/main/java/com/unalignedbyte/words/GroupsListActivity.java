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

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
{
    private View contentView;
    private GroupsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        setupGroupsList();
        setupAddButton();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    private void setupGroupsList()
    {
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groupsListView);
        adapter = new GroupsListAdapter(this, groupsListView);

        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        groupsListView.setAdapter(adapter);

        registerForContextMenu(groupsListView);
        contentView = groupsListView;
    }

    private void setupAddButton()
    {
        FloatingActionButton addGroupButton = (FloatingActionButton)findViewById(R.id.addGroupButton);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showEditGroupPopup(null);
            }
        });
    }

    private void showEditGroupPopup(Group group)
    {
        EditGroupPopupWindow popup = new EditGroupPopupWindow(this, group);
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
            showEditGroupPopup(adapter.getSelectedGroup());
            return true;
        } else if(item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            WordsDataSource.get(this).deleteGroup(adapter.getSelectedGroup());
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }
}
