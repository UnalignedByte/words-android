package com.unalignedbyte.words.words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unalignedbyte.words.MainApplication;
import com.unalignedbyte.words.R;
import com.unalignedbyte.words.Utils;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Word;
import com.unalignedbyte.words.model.WordsDataSource;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rafal on 27/03/2018.
 */

public class EditWordDialog extends DialogFragment
    implements DialogInterface.OnClickListener
{
    public interface Listener
    {
        void dialogDismissed();
    }

    @BindView(R.id.edit_word_dataEntryLayout)
    LinearLayout dataEntryLayout;
    private Group group;
    private Word word;
    private List<EditText> dataEdits;

    public static EditWordDialog dialog(Group group, Word word)
    {
        EditWordDialog dialog = new EditWordDialog();

        Bundle arguments = new Bundle();
        arguments.putInt("groupId", group.getId());
        if(word != null) {
            arguments.putInt("wordId", word.getId());
        }
        dialog.setArguments(arguments);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        int groupId = getArguments().getInt("groupId", -1);
        if (groupId >= 0) {
            this.group = WordsDataSource.get(getActivity()).getGroup(groupId);
        }
        int wordId = getArguments().getInt("wordId", -1);
        if(wordId >= 0) {
            this.word = WordsDataSource.get(getActivity()).getWord(wordId, this.group);
        }

        dataEdits = new LinkedList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_word, null);
        AlertDialog dialog = builder.setView(view).create();
        ButterKnife.bind(this, view);

        setupDataEntry();
        setupButtons(dialog);

        return dialog;
    }

    private void setupDataEntry()
    {
        for (int i = 0; i < group.getLanguage().getWordDataTitles().length; i++) {
            // Data Title
            String title = group.getLanguage().getWordConfigTitles()[i + 1];
            String translatedTitle = Utils.get().translate(title);
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(20.0f);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setText(translatedTitle);
            dataEntryLayout.addView(textView);

            // Data Entry
            EditText dataEdit = new EditText(getActivity());
            dataEdits.add(dataEdit);
            dataEntryLayout.addView(dataEdit);
            dataEdit.setTextSize(18.0f);
            dataEdit.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    updateAddButtonState();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                }
            });
            if (word != null) {
                dataEdit.setText(word.getWordData()[i]);
            }
        }
    }

    private void setupButtons(AlertDialog dialog)
    {
        if (word != null) {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.save), this);
        } else {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.add_word), this);
        }

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (DialogInterface.OnClickListener) null);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateAddButtonState();
    }

    private void updateAddButtonState()
    {
        boolean isEnabled = true;

        for (EditText dataEdit : dataEdits) {
            int textLenght = dataEdit.getText().length();
            if (textLenght == 0) {
                isEnabled = false;
                break;
            }
        }

        AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isEnabled);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {
        onAddWord();

        if(getActivity() instanceof Listener) {
            ((Listener)getActivity()).dialogDismissed();
        }
    }

    private void onAddWord()
    {
        String[] wordData = new String[group.getLanguage().getWordDataTitles().length];
        String[] dataTitles = group.getLanguage().getWordDataTitles();
        for (int i = 0; i < dataTitles.length; i++) {
            EditText dataEdit = dataEdits.get(i);
            wordData[i] = dataEdit.getText().toString();
        }

        if (word == null) {
            Word word = new Word(group, wordData);
            WordsDataSource.get(MainApplication.getContext()).addWord(word);
        } else {
            word.setWordData(wordData);
            WordsDataSource.get(MainApplication.getContext()).updateWord(word);
        }
        dismiss();
    }
}
