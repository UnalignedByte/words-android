package com.unalignedbyte.words.groups;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.text.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

import butterknife.*;

/**
 * Created by rafal on 11/12/2017.
 */

public class EditGroupPopupWindow extends PopupWindow
{
    private final static String PREFS_NAME = "Words";
    private final static String PREFS_ADD_LANGUAGE = "AddLanguage";

    private Context context;
    private Group group;
    @BindView(R.id.edit_group_nameEdit)
    EditText nameEdit;
    @BindView(R.id.edit_group_languageTitleText)
    TextView countryCodeTitleText;
    @BindView(R.id.edit_group_languageSpinner)
    Spinner countryCodeSpinner;

    public EditGroupPopupWindow(Context context, Group group)
    {
        super(LayoutInflater.from(context).inflate(R.layout.edit_group, null),
              RecyclerView.LayoutParams.MATCH_PARENT,
              RecyclerView.LayoutParams.MATCH_PARENT);

        this.context = context;
        this.group = group;

        View view = getContentView();
        ButterKnife.bind(this, view);

        final Button addGroupButton = (Button)view.findViewById(R.id.edit_group_addButton);
        addGroupButton.setEnabled(group != null);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onAddGroup();
            }
        });


        Button cancelButton = (Button)view.findViewById(R.id.edit_group_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });

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

        SpinnerAdapter languageCodesAdapter = new ArrayAdapter<Language>(context,
                R.layout.language_spinner,
                Language.getLanguages());
        countryCodeSpinner.setAdapter(languageCodesAdapter);

        Language selectedLanguage = getAddLanguage();
        if(selectedLanguage != null) {
            int index = Language.getLanguages().indexOf(selectedLanguage);
            countryCodeSpinner.setSelection(index);
        }

        if(group != null) {
            nameEdit.setText(group.getName());
            countryCodeTitleText.setVisibility(View.GONE);
            countryCodeSpinner.setVisibility(View.GONE);
            addGroupButton.setText(R.string.save);
        }

        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void onAddGroup()
    {
        if(group == null) {
            String name = nameEdit.getText().toString();
            Language language = (Language) countryCodeSpinner.getSelectedItem();
            setAddLanguage(language);
            Group group = new Group(name, language);
            WordsDataSource.get(context).addGroup(group);
        } else {
            group.setName(nameEdit.getText().toString());
            WordsDataSource.get(context).updateGroup(group);
        }
        dismiss();
    }

    private Language getAddLanguage()
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String selectedLanguageCode = preferences.getString(PREFS_ADD_LANGUAGE, null);
        return Language.getLanguage(selectedLanguageCode);
    }

    private void setAddLanguage(Language language)
    {
        String code = language != null ? language.getCode() : null;
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(PREFS_ADD_LANGUAGE, code);
        preferencesEditor.apply();
    }
}
