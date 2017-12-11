package com.unalignedbyte.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groupsListView);
        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        groupsListView.setAdapter(new GroupsListAdapter());
    }
}
