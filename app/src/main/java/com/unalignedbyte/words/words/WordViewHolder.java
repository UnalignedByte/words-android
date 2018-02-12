package com.unalignedbyte.words.words;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

import butterknife.*;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordViewHolder extends RecyclerView.ViewHolder
    implements View.OnTouchListener
{
    @BindView(R.id.word_view_holder_wordText)
    TextView wordText;
    @BindView(R.id.word_view_holder_translationText)
    TextView translationText;
    @BindView(R.id.word_view_holder_menuButton)
    Button menuButton;
    @BindView(R.id.word_view_holder_isInRevisionView)
    View isInRevisionView;
    private int config = 0;
    private boolean isInRevision;

    public WordViewHolder(View view, boolean isInRevision)
    {
        super(view);
        this.isInRevision = isInRevision;

        if(isInRevision)
            menuButton.setVisibility(View.GONE);

        itemView.setOnTouchListener(this);
    }

    public Button getMenuButton()
    {
        return menuButton;
    }

    public void setWord(Word word)
    {
        wordText.setText(word.getWordData()[0]);
        translationText.setText(word.getWordData()[1]);
        isInRevisionView.setVisibility(word.getIsInReview() && !this.isInRevision ? View.VISIBLE : View.INVISIBLE);
    }

    public void setConfig(int config)
    {
        this.config = config;
        updateConfig(config);
    }

    private void updateConfig(int config)
    {
        switch(config) {
            case 0:
                wordText.setVisibility(View.VISIBLE);
                translationText.setVisibility(View.VISIBLE);
                break;
            case 1:
                wordText.setVisibility(View.VISIBLE);
                translationText.setVisibility(View.GONE);
                break;
            case 2:
                wordText.setVisibility(View.GONE);
                translationText.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        switch(motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateConfig(0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                updateConfig(this.config);
                break;
        }
        return false;
    }
}
