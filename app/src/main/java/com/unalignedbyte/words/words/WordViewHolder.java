package com.unalignedbyte.words.words;

import android.content.Context;
import android.support.annotation.Nullable;
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
    private int config = 0;
    private boolean isInRevision;

    private View view;
    private TextView[] dataTexts;

    @BindView(R.id.word_view_holder_menuButton)
    Button menuButton;
    @BindView(R.id.word_view_holder_isInRevisionView)
    View isInRevisionView;
    @BindView(R.id.word_view_holder_hack)
    @Nullable View hackView;


    public WordViewHolder(View view, boolean isInRevision)
    {
        super(view);
        ButterKnife.bind(this, view);

        this.view = view;
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
        dataTexts = new TextView[word.getWordData().length];

        for(int i=0; i<dataTexts.length; i++) {
            int id = R.id.word_view_holder_text_0 + i;
            dataTexts[i] = view.findViewById(id);

            dataTexts[i].setText(word.getWordData()[i]);
        }

        isInRevisionView.setVisibility(word.getIsInReview() && !this.isInRevision ? View.VISIBLE : View.INVISIBLE);
    }

    public void setConfig(int config)
    {
        this.config = config;
        updateConfig(config);
    }

    private void updateConfig(int config)
    {
        for(int i=0; i<dataTexts.length; i++) {
            int visibility = View.VISIBLE;
            if(config > 0)
                visibility = (i == config-1) ? View.VISIBLE : View.GONE;
            dataTexts[i].setVisibility(visibility);
        }

        if(hackView != null) {
            hackView.setVisibility(config == 1 ? View.GONE : View.VISIBLE);
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
