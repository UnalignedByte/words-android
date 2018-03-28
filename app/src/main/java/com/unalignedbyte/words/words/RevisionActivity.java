package com.unalignedbyte.words.words;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Language;
import com.unalignedbyte.words.model.WordsDataSource;
import com.unalignedbyte.words.Utils;

/**
 * Created by rafal on 17/01/2018.
 */

public class RevisionActivity extends Activity {
    private RevisionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revision_activity);

        String languageCode = getIntent().getStringExtra("languageCode");
        Language language = Language.getLanguage(languageCode);

        setupToolbar(language);
        setupList(language);
        setupTabBar(language);
    }

    private void setupToolbar(Language language) {
        String text = language.getName() + " - " + getString(R.string.revision) +
                " (" + WordsDataSource.get().getWordsInRevisionCount(language) + ")";
        Toolbar toolbar = (Toolbar) findViewById(R.id.revision_activity_toolbar);
        toolbar.setTitle(text);
    }

    private void setupList(Language language) {
        RecyclerView listView = (RecyclerView) findViewById(R.id.revision_activity_recylerView);
        adapter = new RevisionAdapter(this, listView, language);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
    }

    private void setupTabBar(Language language) {
        TabLayout tabBar = (TabLayout) findViewById(R.id.revision_activity_tabBar);
        for (String title : language.getWordConfigTitles()) {
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
}
