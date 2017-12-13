package com.unalignedbyte.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
{
    private WordsDataSource dataSource;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataSource = new WordsDataSource(this);

        setContentView(R.layout.activity_groups_list);
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groupsListView);
        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        groupsListView.setAdapter(new GroupsListAdapter(dataSource));
        registerForContextMenu(groupsListView);
        contentView = groupsListView;

        FloatingActionButton addGroupButton = (FloatingActionButton)findViewById(R.id.addGroupButton);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showEditGroupActivity();
            }
        });
    }

    private void showEditGroupActivity()
    {
        EditGroupPopupWindow popup = new EditGroupPopupWindow(this, dataSource);
        popup.showAtLocation(contentView, Gravity.TOP, 0, 0);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        if(item.getTitle().equals(getResources().getString(R.string.menu_edit))) {
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            return true;
        } else if(item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
