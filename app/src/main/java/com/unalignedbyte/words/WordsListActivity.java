package com.unalignedbyte.words;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.support.v7.widget.Toolbar;

/**
 * Created by rafal on 20/12/2017.
 */

public class WordsListActivity extends Activity
    implements PopupMenu.OnMenuItemClickListener
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
        setupToolbar();
        setupTab();
    }

    private void setupWordsList()
    {
        adapter = new WordsListAdapter(this, group, this);

        RecyclerView wordsListView = (RecyclerView)findViewById(R.id.words_list_wordsRecyclerView);
        wordsListView.setLayoutManager(new LinearLayoutManager(this));
        wordsListView.setAdapter(adapter);

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

    private void setupToolbar()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.words_list_toolbar);
        toolbar.setTitle(group.getName() + " (" + WordsDataSource.get(this).getWords(group).size() + ")");
    }

    private void setupTab()
    {
        TabLayout tabBar = (TabLayout)findViewById(R.id.words_list_tabBar);
        tabBar.addTab(tabBar.newTab().setText(R.string.both));
        tabBar.addTab(tabBar.newTab().setText(R.string.word));
        tabBar.addTab(tabBar.newTab().setText(R.string.translation));
        tabBar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setConfig(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void showEditWordPopup(Word word)
    {
        EditWordPopupWindow popup = new EditWordPopupWindow(this, group, word);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss() {
                adapter.notifyDataSetChanged();
                setupToolbar();
            }
        });
        popup.showAtLocation(contentView, Gravity.TOP, 0, 0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem)
    {
        switch(menuItem.getItemId()) {
            case R.id.menu_edit:
                showEditWordPopup(adapter.getSelectedWord());
                return true;
            case R.id.menu_delete:
                WordsDataSource.get(this).deleteWord(adapter.getSelectedWord());
                adapter.notifyDataSetChanged();
                setupToolbar();
                return true;
        }
        return false;
    }
}
