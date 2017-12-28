package com.unalignedbyte.words;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import javax.sql.DataSource;

/**
 * Created by rafal on 20/12/2017.
 */

public class WordsListActivity extends Activity
{
    private View contentView;
    private WordsListAdapter adapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        int groupId = getIntent().getIntExtra("groupId", -1);
        group = WordsDataSource.get(this).getGroup(groupId);

        setupWordsList();
        setupAddButton();
    }

    private void setupWordsList()
    {
        adapter = new WordsListAdapter(this, group);

        RecyclerView wordsListView = (RecyclerView)findViewById(R.id.words_list_wordsRecyclerView);
        wordsListView.setLayoutManager(new LinearLayoutManager(this));
        wordsListView.setAdapter(adapter);

        registerForContextMenu(wordsListView);
        contentView = wordsListView;
    }

    private void setupAddButton()
    {
        FloatingActionButton addWordButton = (FloatingActionButton)findViewById(R.id.words_list_addWordButton);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditWordPopup(null);
            }
        });
    }

    private void showEditWordPopup(Word word)
    {
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        if(item.getTitle().equals(getResources().getString(R.string.menu_edit))) {
            showEditWordPopup(adapter.getSelectedWord());
            return true;
        } else if(item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            return true;
        }

        return false;
    }
}
