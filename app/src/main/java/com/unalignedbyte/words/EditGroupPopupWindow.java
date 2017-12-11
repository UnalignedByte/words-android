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
    private WordsDataSource dataSource;
    private EditText nameEdit;
    private Spinner countryCodeSpinner;

    public EditGroupPopupWindow(Context context, WordsDataSource dataSource)
    {
        super(LayoutInflater.from(context).inflate(R.layout.edit_group, null),
              RecyclerView.LayoutParams.MATCH_PARENT,
              RecyclerView.LayoutParams.MATCH_PARENT);
        setFocusable(true);

        this.dataSource = dataSource;

        View view = getContentView();

        final Button addGroupButton = (Button)view.findViewById(R.id.addGroupButton);
        addGroupButton.setEnabled(false);
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
                dataSource.getLanguages());
        countryCodeSpinner.setAdapter(languageCodesAdapter);
    }

    private void onAddGroup()
    {
        String name = nameEdit.getText().toString();
        Language language = (Language)countryCodeSpinner.getSelectedItem();
        Group group = new Group(name, language.getCode());
        dataSource.addGroup(group);
        dismiss();
    }
}
