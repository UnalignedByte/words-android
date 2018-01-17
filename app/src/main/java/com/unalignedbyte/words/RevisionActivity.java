package com.unalignedbyte.words;

import android.app.*;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;

import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 17/01/2018.
 */

public class RevisionActivity extends Activity
{
    private RevisionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revision_activity);

        String languageCode = getIntent().getStringExtra("languageCode");
        Language language = WordsDataSource.get(this).getLanguage(languageCode);

        setupToolbar(language);
        setupList(language);
        setupTabBar();
    }

    private void setupToolbar(Language language)
    {
        String text = language.getName() + " - " + getString(R.string.revision) +
                "(" + WordsDataSource.get(this).getWordsInRevision(language).size() + ")";
        Toolbar toolbar = (Toolbar)findViewById(R.id.revision_activity_toolbar);
        toolbar.setTitle(text);
    }

    private void setupList(Language language)
    {
        RecyclerView listView = (RecyclerView)findViewById(R.id.revision_activity_recylerView);
        adapter = new RevisionAdapter(this, listView, language);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
    }

    private void setupTabBar()
    {
        TabLayout tabBar = (TabLayout)findViewById(R.id.revision_activity_tabBar);
        tabBar.addTab(tabBar.newTab().setText(R.string.both));
        tabBar.addTab(tabBar.newTab().setText(R.string.word));
        tabBar.addTab(tabBar.newTab().setText(R.string.translation));
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
}
