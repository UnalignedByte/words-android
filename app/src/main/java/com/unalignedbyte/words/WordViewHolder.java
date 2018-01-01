package com.unalignedbyte.words;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordViewHolder extends RecyclerView.ViewHolder
    implements View.OnCreateContextMenuListener
{
    private TextView wordText;
    private TextView translationText;

    public WordViewHolder(View view)
    {
        super(view);
        wordText = (TextView)view.findViewById(R.id.word_view_holder_wordText);
        translationText = (TextView)view.findViewById(R.id.word_view_holder_translationText);
        view.setOnCreateContextMenuListener(this);
    }

    public void setWord(Word word)
    {
        wordText.setText(word.getWord());
        translationText.setText(word.getTranslation());
    }

    public void setConfig(int config)
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
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo)
    {
        menu.add(R.string.menu_edit);
        menu.add(R.string.menu_delete);
    }
}
