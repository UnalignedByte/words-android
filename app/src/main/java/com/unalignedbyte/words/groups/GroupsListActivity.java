package com.unalignedbyte.words.groups;

import java.util.*;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

import io.github.luizgrp.sectionedrecyclerviewadapter.*;

/**
 * Created by rafal on 10/12/2017.
 */

public class GroupsListActivity extends Activity
    implements GroupsListSection.Listener
{
    private View contentView;
    private SectionedRecyclerViewAdapter adapter;
    private Group selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_list_activity);

        setupToolbar();
        setupGroupsList();
        setupAddButton();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setupToolbar()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.groups_list_activity_toolbar);
        toolbar.setTitle(R.string.app_name);
    }


    private void setupGroupsList()
    {
        RecyclerView groupsListView = (RecyclerView)findViewById(R.id.groups_list_activity_recyclerView);
        adapter = new SectionedRecyclerViewAdapter();

        for(Language language : Language.getLanguages()) {
            GroupsListSection section = new GroupsListSection(this, adapter, groupsListView, language,true);
            section.setListener(this);
            adapter.addSection(section);
        }
        updateSectionHeaders();

        groupsListView.setLayoutManager(new LinearLayoutManager(this));
        groupsListView.setAdapter(adapter);

        registerForContextMenu(groupsListView);
        contentView = groupsListView;
    }

    private void setupAddButton()
    {
        FloatingActionButton addGroupButton = (FloatingActionButton)findViewById(R.id.groups_list_activity_addButton);
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
                updateSectionHeaders();
                adapter.notifyDataSetChanged();
            }
        });
        popup.showAtLocation(contentView, Gravity.TOP, 0, 0);
    }

    private void updateSectionHeaders()
    {
        int languagesWithWords = 0;
        for(Language language : Language.getLanguages()) {
            if(WordsDataSource.get(this).getGroups(language).size() > 0)
                languagesWithWords++;
        }

        boolean hasHeader = languagesWithWords > 1;

        Collection<Section> sections = adapter.getSectionsMap().values();
        for(Section section : sections) {
            ((GroupsListSection)section).setHasHeader(hasHeader);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        if(item.getTitle().equals(getResources().getString(R.string.menu_edit))) {
            showEditGroupPopup(selectedGroup);
            return true;
        } else if(item.getTitle().equals(getResources().getString(R.string.menu_delete))) {
            updateSectionHeaders();
            WordsDataSource.get(this).deleteGroup(selectedGroup);
            updateSectionHeaders();
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    @Override
    public void selectedGroup(Group group)
    {
        this.selectedGroup = group;
    }
}
