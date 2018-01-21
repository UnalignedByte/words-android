package com.unalignedbyte.words.words;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.text.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 31/12/2017.
 */

public class EditWordPopupWindow extends PopupWindow
{
    private Context context;
    private Group group;
    private Word word;
    private EditText wordEdit;
    private EditText translationEdit;

    public EditWordPopupWindow(Context context, Group group, Word word)
    {
        super(LayoutInflater.from(context).inflate(R.layout.edit_word, null),
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);

        this.context = context;
        this.group = group;
        this.word = word;

        View view = getContentView();

        final Button addWordButton = (Button)view.findViewById(R.id.edit_word_addButton);
        addWordButton.setEnabled(word != null);
        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddWord();
            }
        });

        Button cancelButton = (Button)view.findViewById(R.id.edit_word_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        wordEdit = (EditText)view.findViewById(R.id.edit_word_wordEdit);
        wordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean shouldEnable = charSequence.length() > 0 && translationEdit.getText().toString().length() > 0;
                addWordButton.setEnabled(shouldEnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        translationEdit = (EditText)view.findViewById(R.id.edit_word_translationEdit);
        translationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean shouldEnable = charSequence.length() > 0 && wordEdit.getText().toString().length() > 0;
                addWordButton.setEnabled(shouldEnable);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        if(word != null) {
            wordEdit.setText(word.getWordData()[0]);
            translationEdit.setText(word.getWordData()[1]);
            addWordButton.setText(R.string.save);
        }

        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void onAddWord()
    {
        if(word == null) {
            String wordString = wordEdit.getText().toString();
            String translation = translationEdit.getText().toString();
            Word word = new Word(group, new String[] {wordString, translation});
            WordsDataSource.get(context).addWord(word);
        } else {
            word.setWordData(new String[] {wordEdit.getText().toString(),
                translationEdit.getText().toString()});
            WordsDataSource.get(context).updateWord(word);
        }
        dismiss();
    }
}
