package com.unalignedbyte.words.groups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.MainApplication;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Language;
import com.unalignedbyte.words.model.WordsDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rafal on 27/03/2018.
 */

public class EditGroupDialog extends DialogFragment
        implements DialogInterface.OnClickListener
{
    public interface Listener
    {
        void dialogDismissed();
    }

    private final static String PREFS_NAME = "Words";
    private final static String PREFS_ADD_LANGUAGE = "AddLanguage";
    @BindView(R.id.edit_group_nameEdit)
    EditText nameEdit;
    @BindView(R.id.edit_group_languageTitleText)
    TextView languageTitleText;
    @BindView(R.id.edit_group_languageSpinner)
    Spinner languageSpinner;
    private Group group;

    public static EditGroupDialog dialog(Group group)
    {
        EditGroupDialog dialog = new EditGroupDialog();

        Bundle arguments = new Bundle();
        if (group != null) {
            arguments.putInt("groupId", group.getId());
        }
        dialog.setArguments(arguments);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();

        setupArguments();
        setupView(dialog);
        setupButtons(dialog);
        restoreState(savedInstanceState);

        return dialog;
    }

    private void setupArguments()
    {
        int groupId = getArguments().getInt("groupId", -1);
        if (groupId >= 0) {
            this.group = WordsDataSource.get().getGroup(groupId);
        }
    }

    private void setupView(AlertDialog dialog)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_group, null);
        dialog.setView(view);
        ButterKnife.bind(this, view);

        nameEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                updateAddButtonState();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                if(isAddButtonEnabled()) {
                    onClick(getDialog(), 0);
                    return true;
                }

                return false;
            }
        });

        SpinnerAdapter languageCodesAdapter = new ArrayAdapter<Language>(getActivity(),
                R.layout.language_spinner,
                Language.getLanguages());
        languageSpinner.setAdapter(languageCodesAdapter);

        Language selectedLanguage = getAddLanguage();
        if (selectedLanguage != null) {
            int index = Language.getLanguages().indexOf(selectedLanguage);
            languageSpinner.setSelection(index);
        }

        if (this.group != null) {
            nameEdit.setText(group.getName());
            languageTitleText.setVisibility(View.GONE);
            languageSpinner.setVisibility(View.GONE);
        }

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void restoreState(Bundle savedInstanceState)
    {
        if(savedInstanceState == null)
            return;

        String groupName = savedInstanceState.getString("groupName", "");
        nameEdit.setText(groupName);
        String languageCode = savedInstanceState.getString("languageCode", null);
        Language language = Language.getLanguage(languageCode);
        if(language != null) {
            int index = Language.getLanguages().indexOf(language);
            languageSpinner.setSelection(index);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("groupName", nameEdit.getText().toString());
        Language language = (Language)languageSpinner.getSelectedItem();
        outState.putString("languageCode", language.getCode());
    }

    private boolean isAddButtonEnabled()
    {
        return nameEdit.getText().length() > 0;
    }

    private void updateAddButtonState()
    {
        boolean isEnabled = isAddButtonEnabled();
        AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isEnabled);
        }
    }

    private void setupButtons(AlertDialog dialog)
    {
        if (group != null) {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.save), this);
        } else {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.add_group), this);
        }

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener) null);
    }

    private Language getAddLanguage()
    {
        SharedPreferences preferences = MainApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String selectedLanguageCode = preferences.getString(PREFS_ADD_LANGUAGE, null);
        return Language.getLanguage(selectedLanguageCode);
    }

    private void setAddLanguage(Language language)
    {
        String code = language != null ? language.getCode() : null;
        SharedPreferences preferences = MainApplication.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(PREFS_ADD_LANGUAGE, code);
        preferencesEditor.apply();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {
        addGroup();
    }

    private void addGroup()
    {
        if (group == null) {
            String name = nameEdit.getText().toString();
            Language language = (Language) languageSpinner.getSelectedItem();
            setAddLanguage(language);
            Group group = new Group(name, language);
            WordsDataSource.get().addGroup(group);
        } else {
            group.setName(nameEdit.getText().toString());
            WordsDataSource.get().updateGroup(group);
        }

        dismiss();
        if (getActivity() instanceof Listener) {
            ((Listener) getActivity()).dialogDismissed();
        }
    }
}
