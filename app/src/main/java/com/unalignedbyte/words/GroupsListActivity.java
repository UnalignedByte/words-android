package com.unalignedbyte.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
{
    private WordsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataSource = new WordsDataSource(this);

        Group tst1 = new Group("Test 1", "en");
        Group tst2 = new Group("Test 2", "en");
        Group tst3 = new Group("Test 3", "en");
        dataSource.addGroup(tst1);
        dataSource.addGroup(tst2);
        dataSource.addGroup(tst3);

        setContentView(R.layout.activity_groups_list);
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groupsListView);
        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        groupsListView.setAdapter(new GroupsListAdapter(dataSource));

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
    }
}
