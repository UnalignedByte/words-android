package com.unalignedbyte.words;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Created by rafal on 11/12/2017.
 */

public class EditGroupPopupWindow extends PopupWindow
{
    private Context context;
    private Group group;
    private EditText nameEdit;
    private Spinner countryCodeSpinner;

    public EditGroupPopupWindow(Context context, Group group)
    {
        super(LayoutInflater.from(context).inflate(R.layout.edit_group, null),
              RecyclerView.LayoutParams.MATCH_PARENT,
              RecyclerView.LayoutParams.MATCH_PARENT);
        setFocusable(true);

        this.context = context;
        this.group = group;

        View view = getContentView();

        final Button addGroupButton = (Button)view.findViewById(R.id.addGroupButton);
        addGroupButton.setEnabled(group != null);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onAddGroup();
            }
        });


        Button cancelButton = (Button)view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });

        nameEdit = (EditText)view.findViewById(R.id.groupNameEdit);
        nameEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                addGroupButton.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        countryCodeSpinner = (Spinner)view.findViewById(R.id.groupLanguageSpinner);
        SpinnerAdapter languageCodesAdapter = new ArrayAdapter<Language>(context,
                R.layout.language_name_layout,
                WordsDataSource.get(context).getLanguages());
        countryCodeSpinner.setAdapter(languageCodesAdapter);

        if(group != null) {
            nameEdit.setText(group.getName());
            countryCodeSpinner.setVisibility(View.INVISIBLE);

            addGroupButton.setText(R.string.save);
        }
    }

    private void onAddGroup()
    {
        if(group == null) {
            String name = nameEdit.getText().toString();
            Language language = (Language) countryCodeSpinner.getSelectedItem();
            Group group = new Group(name, language);
            WordsDataSource.get(context).addGroup(group);
        } else {
            group.setName(nameEdit.getText().toString());
            WordsDataSource.get(context).updateGroup(group);
        }
        dismiss();
    }
}
