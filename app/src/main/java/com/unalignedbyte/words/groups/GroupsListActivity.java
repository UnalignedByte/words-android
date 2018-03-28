package com.unalignedbyte.words.groups;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Language;
import com.unalignedbyte.words.model.WordsDataSource;
import com.unalignedbyte.words.model.WordsImporter;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
        implements GroupsListSection.Listener, EditGroupDialog.Listener {
    private final static String PREFS_NAME = "Words";
    private final static String PREFS_SELECTED_LANGUAGE = "SelectedLanguage";

    private SectionedRecyclerViewAdapter adapter;
    private Group selectedGroup;

    @BindView(R.id.groups_list_activity_recyclerView)
    RecyclerView groupsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
        setupToolbar();
        setupGroupsList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setupView() {
        setContentView(R.layout.groups_list_activity);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.groups_list_activity_toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.groups_list_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.groups_list_toolbar_menu_import) {
                    WordsImporter.get(GroupsListActivity.this).importAllWords();
                    updateSectionHeaders();
                    adapter.notifyDataSetChanged();
                    return true;
                }/* else if(item.getItemId() == R.id.groups_list_toolbar_menu_export) {
                    return true;
                }*/
                return false;
            }
        });
    }

    private void setupGroupsList() {
        adapter = new SectionedRecyclerViewAdapter();

        for (Language language : Language.getLanguages()) {
            GroupsListSection section = new GroupsListSection(adapter, groupsRecyclerView, language, false);
            section.setListener(this);
            adapter.addSection(section);
        }
        updateSectionHeaders();

        groupsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        groupsRecyclerView.setAdapter(adapter);

        registerForContextMenu(groupsRecyclerView);
    }

    @OnClick(R.id.groups_list_activity_addButton)
    void onAddButtonPressed(View view) {
        showEditGroup(null);
    }

    private void showEditGroup(Group group) {
        EditGroupDialog dialog = EditGroupDialog.dialog(group);
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, null);
    }

    @Override
    public void dialogDismissed() {
        updateSectionHeaders();
        adapter.notifyDataSetChanged();
    }

    private void updateSectionHeaders() {
        int languagesWithWords = 0;
        for (Language language : Language.getLanguages()) {
            if (WordsDataSource.get(this).getGroupsCount(language) > 0)
                languagesWithWords++;
        }

        boolean hasHeader = languagesWithWords > 1;

        Collection<Section> sections = adapter.getSectionsMap().values();
        for (Section section : sections) {
            GroupsListSection groupsListSection = (GroupsListSection) section;
            groupsListSection.setHasHeader(hasHeader);

            boolean isSelected = true;
            if (languagesWithWords > 1)
                isSelected = groupsListSection.getLanguage().equals(getSelectedLanguage());
            groupsListSection.setIsSelected(isSelected);
        }
    }

    private Language getSelectedLanguage() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String selectedLanguageCode = preferences.getString(PREFS_SELECTED_LANGUAGE, null);
        return Language.getLanguage(selectedLanguageCode);
    }

    private void setSelectedLanguage(Language language) {
        String code = language != null ? language.getCode() : null;
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(PREFS_SELECTED_LANGUAGE, code);
        preferencesEditor.apply();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getTitle().equals(getResources().getString(R.string.menu_edit))) {
            showEditGroup(selectedGroup);
            return true;
        } else if (item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            updateSectionHeaders();
            WordsDataSource.get(this).deleteGroup(selectedGroup);
            updateSectionHeaders();
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public void selectedGroup(Group group) {
        this.selectedGroup = group;
    }

    @Override
    public void selectedSection(Language language) {
        setSelectedLanguage(language);

        Collection<Section> sections = adapter.getSectionsMap().values();
        for (Section section : sections) {
            GroupsListSection groupsListSection = (GroupsListSection) section;
            boolean isSelected = groupsListSection.getLanguage() == language;
            groupsListSection.setIsSelected(isSelected);
        }
    }
}
