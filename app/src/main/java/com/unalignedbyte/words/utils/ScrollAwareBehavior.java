package com.unalignedbyte.words.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rafal on 22/03/2018.
 */

public class ScrollAwareBehavior extends FloatingActionButton.Behavior
{
    public ScrollAwareBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int axes, int type)
    {
        boolean isVertical = (axes == ViewCompat.SCROLL_AXIS_VERTICAL);
        boolean isHandledBySuper = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);

        return isVertical | isHandledBySuper;
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type)
    {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if(dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide(new FloatingActionButton.OnVisibilityChangedListener()
            {
                @Override
                public void onHidden(FloatingActionButton fab)
                {
                    fab.setVisibility(View.INVISIBLE);
                }
            });
        } else if(dyConsumed < -10 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}
