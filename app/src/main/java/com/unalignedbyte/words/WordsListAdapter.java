package com.unalignedbyte.words;

import android.content.Context;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordsListAdapter extends RecyclerView.Adapter<WordViewHolder>
{
    private Context context;
    private Group group;
    private Word selectedWord;

    public WordsListAdapter(Context context, Group group)
    {
        this.context = context;
        this.group = group;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.word_view_holder, parent, false);
        WordViewHolder viewHolder = new WordViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WordViewHolder viewHolder, int position)
    {
        final Word word = WordsDataSource.get(context).getWords(group).get(position);
        viewHolder.setWord(word);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedWord = word;
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return WordsDataSource.get(context).getWords(group).size();
    }

    public Word getSelectedWord()
    {
        return selectedWord;
    }
}
