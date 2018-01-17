package com.unalignedbyte.words.words;

import android.content.*;
import android.view.*;
import android.graphics.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;

import com.unalignedbyte.words.R;
import com.unalignedbyte.words.model.*;

/**
 * Created by rafal on 17/01/2018.
 */

public class RevisionAdapter extends RecyclerView.Adapter<WordViewHolder>
{
    Context context;
    private RecyclerView recyclerView;
    Language language;
    private int config = 0;

    public RevisionAdapter(Context context, RecyclerView recyclerView, Language language)
    {
        this.context = context;
        this.recyclerView = recyclerView;
        this.language = language;
        setupDragging();
    }

    private void setupDragging()
    {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback()
        {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.START);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word word = WordsDataSource.get(context).getWordsInRevision(language).get(position);
                word.setIsInReview(false);
                WordsDataSource.get(context).updateWord(word);
                RevisionAdapter.this.notifyItemRemoved(position);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Paint backgroundColor = new Paint();
                backgroundColor.setARGB(255, 252, 70, 74);
                String text = context.getString(R.string.remove_from_revision);

                // Background
                float left = viewHolder.itemView.getRight() + dX;
                float right = viewHolder.itemView.getRight();
                float top = viewHolder.itemView.getTop();
                float bottom = viewHolder.itemView.getBottom();
                c.drawRect(left, top, right, bottom, backgroundColor);

                // Foreground
                Paint textPaint = new Paint();
                textPaint.setARGB(255, 255, 255, 255);
                textPaint.setFlags(Paint.HINTING_ON|Paint.FAKE_BOLD_TEXT_FLAG);
                float fontScale = context.getResources().getDisplayMetrics().density;
                textPaint.setTextSize(18 * fontScale);

                Rect textBounds = new Rect();
                textPaint.getTextBounds(text, 0, text.length(), textBounds);
                float textX = right - textBounds.width() - 8.0f * fontScale;
                float textY = top + (Math.abs(top - bottom) + textBounds.height())/2.0f;

                c.drawText(text, textX, textY, textPaint);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.word_view_holder, parent, false);
        WordViewHolder viewHolder = new WordViewHolder(view, true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WordViewHolder viewHolder, int position)
    {
        Word word = WordsDataSource.get(context).getWordsInRevision(language).get(position);
        viewHolder.setWord(word);
        viewHolder.setConfig(config);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount()
    {
        int count = WordsDataSource.get(context).getWordsInRevision(language).size();
        return count;
    }

    public void setConfig(int config)
    {
        this.config = config;
        notifyDataSetChanged();
    }
}
