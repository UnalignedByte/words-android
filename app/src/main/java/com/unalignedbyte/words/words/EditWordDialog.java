package com.unalignedbyte.words.words;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();

        setupArguments();
        setupView(dialog);
        setupDataEntry();
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
        int wordId = getArguments().getInt("wordId", -1);
        if(wordId >= 0) {
            this.word = WordsDataSource.get().getWord(wordId, this.group);
        }

        dataEdits = new LinkedList();
    }

    private void setupView(AlertDialog dialog)
    {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_word, null);
        dialog.setView(view);
        ButterKnife.bind(this, view);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void setupDataEntry()
    {
        int titlesCount = group.getLanguage().getWordDataTitles().length;
        for (int i = 0; i < titlesCount; i++) {
            boolean isLast = (i == titlesCount-1);

            // Data Title
            String title = group.getLanguage().getWordConfigTitles()[i + 1];
            String translatedTitle = Utils.get().translate(title);
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(translatedTitle);
            dataEntryLayout.addView(textView);

            // Data Entry
            EditText dataEdit = new EditText(getActivity());
            dataEdits.add(dataEdit);
            dataEntryLayout.addView(dataEdit);
            dataEdit.setTextSize(18.0f);
            dataEdit.setSingleLine(true);
            dataEdit.setHint(translatedTitle);
            dataEdit.setImeOptions(isLast ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
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

            if(isLast) {
                dataEdit.setOnEditorActionListener(new TextView.OnEditorActionListener()
                {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
                    {
                        if(isAddButtonEnabled()) {
                            addWord();
                            return true;
                        }

                        return false;
                    }
                });
            }

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

    private void restoreState(Bundle savedInstanceState)
    {
        if(savedInstanceState == null)
            return;

        int titlesCount = group.getLanguage().getWordDataTitles().length;
        for(int i=0; i< titlesCount; i++) {
            String data = savedInstanceState.getString("data"+i);
            dataEdits.get(i).setText(data);
        }
        int focusIndex = savedInstanceState.getInt("focusIndex", -1);
        if(focusIndex >= 0) {
            dataEdits.get(focusIndex).requestFocus();
        } else {
            //getView().clearFocus();
            dataEntryLayout.clearFocus();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        int titlesCount = group.getLanguage().getWordDataTitles().length;
        for(int i=0; i< titlesCount; i++) {
            outState.putString("data"+i, dataEdits.get(i).getText().toString());
            if(dataEdits.get(i).hasFocus()) {
                outState.putInt("focusIndex", i);
            }
        }
    }

    private boolean isAddButtonEnabled()
    {
        boolean isEnabled = true;

        for (EditText dataEdit : dataEdits) {
            int textLenght = dataEdit.getText().length();
            if (textLenght == 0) {
                isEnabled = false;
                break;
            }
        }

        return isEnabled;
    }

    private void updateAddButtonState()
    {
        boolean isEnabled = isAddButtonEnabled();
        AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(isEnabled);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i)
    {
        addWord();
    }

    private void addWord()
    {
        String[] wordData = new String[group.getLanguage().getWordDataTitles().length];
        String[] dataTitles = group.getLanguage().getWordDataTitles();
        for (int i = 0; i < dataTitles.length; i++) {
            EditText dataEdit = dataEdits.get(i);
            wordData[i] = dataEdit.getText().toString();
        }

        if (word == null) {
            Word word = new Word(group, wordData);
            WordsDataSource.get().addWord(word);
        } else {
            word.setWordData(wordData);
            WordsDataSource.get().updateWord(word);
        }

        dismiss();
        if(getActivity() instanceof Listener) {
            ((Listener)getActivity()).dialogDismissed();
        }
    }
}
