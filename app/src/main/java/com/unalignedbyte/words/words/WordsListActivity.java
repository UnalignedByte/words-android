package com.unalignedbyte.words.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Word;
import com.unalignedbyte.words.model.WordsDataSource;
import com.unalignedbyte.words.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rafal on 20/12/2017.
 */

public class WordsListActivity extends Activity
        implements PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.words_list_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.words_list_activity_recyclerView)
    RecyclerView wordsRecyclerView;
    private WordsListAdapter adapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list_activity);
        ButterKnife.bind(this);

        int groupId = getIntent().getIntExtra("groupId", -1);
        group = WordsDataSource.get(this).getGroup(groupId);

        setupWordsList();
        setupToolbar();
        updateToolbarTitle();
        setupTab();
    }

    private void setupWordsList() {
        adapter = new WordsListAdapter(this, wordsRecyclerView, group, this);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordsRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.words_list_activity_addWordButton)
    void onAddButtonClick(View view) {
        showEditWordPopup(null);
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.words_list_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_shuffle) {
                    shuffleWords();
                    return true;
                }
                return false;
            }
        });
    }

    private void updateToolbarTitle() {
        toolbar.setTitle(group.getName() + " (" + WordsDataSource.get(this).getWordsCount(group) + ")");
    }

    private void setupTab() {
        TabLayout tabBar = (TabLayout) findViewById(R.id.words_list_activity_tabBar);
        for (String title : group.getLanguage().getWordConfigTitles()) {
            String translatedTitle = Utils.get().translate(title);
            tabBar.addTab(tabBar.newTab().setText(translatedTitle));
        }
        tabBar.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.setConfig(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void showEditWordPopup(Word word) {
        EditWordPopupWindow popup = new EditWordPopupWindow(this, group, word);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                adapter.notifyDataSetChanged();
                updateToolbarTitle();
            }
        });
        popup.showAtLocation(wordsRecyclerView, Gravity.TOP, 0, 0);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_edit:
                showEditWordPopup(adapter.getSelectedWord());
                return true;
            case R.id.menu_delete:
                WordsDataSource.get(this).deleteWord(adapter.getSelectedWord());
                adapter.notifyDataSetChanged();
                updateToolbarTitle();
                return true;
        }
        return false;
    }

    private void shuffleWords() {
        List<Word> words = WordsDataSource.get(this).getWords(group);

        for (Word word : words) {
            int order = (int) (Math.random() * 1000 + 1);
            word.setOrder(order);
            WordsDataSource.get(this).updateWord(word);
        }

        adapter.notifyDataSetChanged();
    }
}
