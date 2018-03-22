package com.unalignedbyte.words.words;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.MainApplication;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Word;
import com.unalignedbyte.words.model.WordsDataSource;
import com.unalignedbyte.words.Utils;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rafal on 31/12/2017.
 */

public class EditWordPopupWindow extends PopupWindow {
    @BindView(R.id.edit_word_dataEntryLayout)
    LinearLayout dataEntryLayout;
    @BindView(R.id.edit_word_addButton)
    Button addWordButton;
    private Group group;
    private Word word;
    private List<EditText> dataEdits;

    public EditWordPopupWindow(Context context, Group group, Word word) {
        super(LayoutInflater.from(context).inflate(R.layout.edit_word, null),
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);

        this.group = group;
        this.word = word;
        dataEdits = new LinkedList();

        View view = getContentView();
        ButterKnife.bind(this, view);

        addWordButton.setEnabled(word != null);

        setupDataEntry();

        if (word != null)
            addWordButton.setText(R.string.save);

        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @OnClick(R.id.edit_word_addButton)
    void onAddButtonPressed(View view) {
        onAddWord();
    }

    @OnClick(R.id.edit_word_cancelButton)
    void onCancelButtonPressed(View view) {
        dismiss();
    }

    private void setupDataEntry() {
        for (int i = 0; i < group.getLanguage().getWordDataTitles().length; i++) {
            // Data Title
            String title = group.getLanguage().getWordConfigTitles()[i + 1];
            String translatedTitle = Utils.get().translate(title);
            TextView textView = new TextView(MainApplication.getContext());
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(translatedTitle);
            dataEntryLayout.addView(textView);

            // Data Entry
            EditText dataEdit = new EditText(MainApplication.getContext());
            dataEdit.setTextSize(12);
            dataEdits.add(dataEdit);
            dataEntryLayout.addView(dataEdit);
            dataEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    updateAddButtonStatus();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            if (word != null) {
                dataEdit.setText(word.getWordData()[i]);
            }
        }
    }

    private void updateAddButtonStatus() {
        for (EditText dataEdit : dataEdits) {
            int textLenght = dataEdit.getText().length();
            if (textLenght == 0) {
                addWordButton.setEnabled(false);
                return;
            }
        }

        addWordButton.setEnabled(true);
    }

    private void onAddWord() {
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
