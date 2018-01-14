package com.unalignedbyte.words;

import android.view.*;
import android.widget.*;
import android.support.v7.widget.*;

import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordViewHolder extends RecyclerView.ViewHolder
    implements View.OnTouchListener
{
    private TextView wordText;
    private TextView translationText;
    private Button menuButton;
    private View isInRevisionView;
    private int config = 0;

    public WordViewHolder(View view)
    {
        super(view);
        wordText = (TextView)view.findViewById(R.id.word_view_holder_wordText);
        translationText = (TextView)view.findViewById(R.id.word_view_holder_translationText);
        menuButton = (Button)itemView.findViewById(R.id.word_view_holder_menuButton);
        isInRevisionView = itemView.findViewById(R.id.word_view_holder_isInRevisionView);

        itemView.setOnTouchListener(this);
    }

    public Button getMenuButton()
    {
        return menuButton;
    }

    public void setWord(Word word)
    {
        wordText.setText(word.getWord());
        translationText.setText(word.getTranslation());
        isInRevisionView.setVisibility(word.getIsInReview() ? View.VISIBLE : View.INVISIBLE);
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
