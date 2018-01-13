package com.unalignedbyte.words;

import android.content.*;
import android.view.*;
import android.widget.PopupMenu;
import android.support.v7.widget.*;

import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordsListAdapter extends RecyclerView.Adapter<WordViewHolder>
{
    private Context context;
    private Group group;
    private Word selectedWord;
    private int config = 0;
    private PopupMenu.OnMenuItemClickListener menuListener;

    public WordsListAdapter(Context context, Group group, PopupMenu.OnMenuItemClickListener menuListener)
    {
        this.context = context;
        this.group = group;
        this.menuListener = menuListener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.word_view_holder, parent, false);
        WordViewHolder viewHolder = new WordViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WordViewHolder viewHolder, int position)
    {
        final Word word = WordsDataSource.get(context).getWords(group).get(position);
        viewHolder.setWord(word);
        viewHolder.setConfig(config);
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //selectedWord = word;
                return false;
            }
        });
        viewHolder.getMenuButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedWord = word;
                showPopupMenu(viewHolder.getMenuButton());
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

    public void setConfig(int config)
    {
        this.config = config;
    }

    private void showPopupMenu(View view)
    {
        PopupMenu menu = new PopupMenu(context, view, Gravity.BOTTOM);
        menu.inflate(R.menu.edit_delete_menu);
        menu.setOnMenuItemClickListener(menuListener);
        menu.show();
    }
}
