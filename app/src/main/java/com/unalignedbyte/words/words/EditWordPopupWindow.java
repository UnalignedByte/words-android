package com.unalignedbyte.words.words;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.text.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

import java.util.*;

/**
 * Created by rafal on 31/12/2017.
 */

public class EditWordPopupWindow extends PopupWindow
{
    private Context context;
    private Group group;
    private Word word;
    private LinearLayout dataEntryLayout;
    private Button addWordButton;
    private List<EditText> dataEdits;

    public EditWordPopupWindow(Context context, Group group, Word word)
    {
        super(LayoutInflater.from(context).inflate(R.layout.edit_word, null),
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);

        this.context = context;
        this.group = group;
        this.word = word;
        dataEdits = new LinkedList();

        View view = getContentView();

        dataEntryLayout = (LinearLayout)view.findViewById(R.id.edit_word_dataEntryLayout);

        addWordButton = (Button)view.findViewById(R.id.edit_word_addButton);
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

        setupDataEntry();

        if(word != null)
            addWordButton.setText(R.string.save);

        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void setupDataEntry()
    {
        for(int i=0; i<group.getLanguage().getWordDataTitles().length; i++)
        {
            // Data Title
            String title = group.getLanguage().getWordConfigTitles()[i+1];
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(title);
            dataEntryLayout.addView(textView);

            // Data Entry
            EditText dataEdit = new EditText(context);
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
            if(word != null) {
                dataEdit.setText(word.getWordData()[i]);
            }
        }
    }

    private void updateAddButtonStatus()
    {
        for(EditText dataEdit : dataEdits) {
            int textLenght = dataEdit.getText().length();
            if(textLenght == 0) {
                addWordButton.setEnabled(false);
                return;
            }
        }

        addWordButton.setEnabled(true);
    }

    private void onAddWord()
    {
        String[] wordData = new String[group.getLanguage().getWordDataTitles().length];
        String[] dataTitles = group.getLanguage().getWordDataTitles();
        for(int i=0; i<dataTitles.length; i++) {
            EditText dataEdit = dataEdits.get(i);
            wordData[i] = dataEdit.getText().toString();
        }

        if(word == null) {
            Word word = new Word(group, wordData);
            WordsDataSource.get(context).addWord(word);
        } else {
            word.setWordData(wordData);
            WordsDataSource.get(context).updateWord(word);
        }
        dismiss();
    }
}
