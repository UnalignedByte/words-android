package com.unalignedbyte.words.words;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.MainApplication;
import com.unalignedbyte.words.model.Group;
import com.unalignedbyte.words.model.Language;
import com.unalignedbyte.words.model.Word;
import com.unalignedbyte.words.model.WordsDataSource;

/**
 * Created by rafal on 27/12/2017.
 */

public class WordsListAdapter extends RecyclerView.Adapter<WordViewHolder> {
    private RecyclerView recyclerView;
    private Group group;
    private Word selectedWord;
    private int config = 0;
    private PopupMenu.OnMenuItemClickListener menuListener;

    public WordsListAdapter(RecyclerView recyclerView, Group group, PopupMenu.OnMenuItemClickListener menuListener) {
        this.recyclerView = recyclerView;
        this.group = group;
        this.menuListener = menuListener;
        setupDragging();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id = viewHolderIdForLanguage(group.getLanguage());
        View view = LayoutInflater.from(MainApplication.getContext()).inflate(id, parent, false);
        WordViewHolder viewHolder = new WordViewHolder(view, false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WordViewHolder viewHolder, int position) {
        final Word word = WordsDataSource.get(MainApplication.getContext()).getWords(group).get(position);
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
    public int getItemCount() {
        return WordsDataSource.get(MainApplication.getContext()).getWordsCount(group);
    }

    private void setupDragging() {
        final Context context = MainApplication.getContext();

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.START);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word word = WordsDataSource.get(context).getWords(group).get(position);
                word.setIsInReview(!word.getIsInReview());
                WordsDataSource.get(context).updateWord(word);
                WordsListAdapter.this.notifyItemChanged(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (isCurrentlyActive) {
                    int position = viewHolder.getAdapterPosition();
                    Word word = WordsDataSource.get(context).getWords(group).get(position);

                    Paint backgroundColor = new Paint();
                    String text;
                    if (word.getIsInReview()) {
                        backgroundColor.setARGB(255, 252, 70, 74);
                        text = context.getString(R.string.remove_from_revision);
                    } else {
                        backgroundColor.setARGB(255, 154, 202, 39);
                        text = context.getString(R.string.add_to_revision);
                    }

                    // Background
                    float left = viewHolder.itemView.getRight() + dX;
                    float right = viewHolder.itemView.getRight();
                    float top = viewHolder.itemView.getTop();
                    float bottom = viewHolder.itemView.getBottom();
                    c.drawRect(left, top, right, bottom, backgroundColor);

                    // Foreground
                    Paint textPaint = new Paint();
                    textPaint.setARGB(255, 255, 255, 255);
                    textPaint.setFlags(Paint.HINTING_ON | Paint.FAKE_BOLD_TEXT_FLAG);
                    float fontScale = context.getResources().getDisplayMetrics().density;
                    textPaint.setTextSize(18 * fontScale);

                    Rect textBounds = new Rect();
                    textPaint.getTextBounds(text, 0, text.length(), textBounds);
                    float textX = right - textBounds.width() - 8.0f * fontScale;
                    float textY = top + (Math.abs(top - bottom) + textBounds.height()) / 2.0f;

                    c.drawText(text, textX, textY, textPaint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public Word getSelectedWord() {
        return selectedWord;
    }

    public void setConfig(int config) {
        this.config = config;
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view) {
        PopupMenu menu = new PopupMenu(MainApplication.getContext(), view, Gravity.BOTTOM);
        menu.inflate(R.menu.edit_delete_menu);
        menu.setOnMenuItemClickListener(menuListener);
        menu.show();
    }

    private int viewHolderIdForLanguage(Language language) {
        if (language.equals(Language.getLanguage("cn"))) {
            return R.layout.word_view_holder_cn;
        } else {
            return R.layout.word_view_holder_gn;
        }
    }
}
