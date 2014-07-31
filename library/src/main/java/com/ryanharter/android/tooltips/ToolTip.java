package com.ryanharter.android.tooltips;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by rharter on 7/24/14.
 */
public class ToolTip {

    private WeakReference<View> mAnchorView;
    private WeakReference<View> mContentView;
    private ToolTipPointerView mPointerView;
    private int mGravity;
    private int mColor;
    private int mPointerSize;
    private boolean mDismissOnTouch;

    private View mView;

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    public View getAnchorView() {
        return mAnchorView != null ? mAnchorView.get() : null;
    }

    public void setAnchorView(View anchorView) {
        mAnchorView = new WeakReference<>(anchorView);
    }

    public View getContentView() {
        return mContentView != null ? mContentView.get() : null;
    }

    public void setContentView(View view) {
        mContentView = new WeakReference<View>(view);
    }

    public ToolTipPointerView getPointerView() {
        return mPointerView;
    }

    public void setPointerView(ToolTipPointerView pointerView) {
        mPointerView = pointerView;
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        mGravity = gravity;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public int getPointerSize() {
        return mPointerSize;
    }

    public void setPointerSize(int pointerSize) {
        mPointerSize = pointerSize;
    }

    public boolean isDismissOnTouch() {
        return mDismissOnTouch;
    }

    public void setDismissOnTouch(boolean dismissOnTouch) {
        mDismissOnTouch = dismissOnTouch;
    }

    /**
     * Builds the tooltip view for display.  The callout arrow is
     * defined by <code>gravity</code>, but defaults to alignment
     * at 0, it's up to the caller to position it appropriately
     * @param context The context used to build the view.
     * @return The constructed view, with added arrow.
     */
    public View makeView(Context context) {
        LinearLayout layout = new LinearLayout(context);

        // If the tooltip is above or below, we make is fill the view.
        if (Gravity.isVertical(mGravity)) {
            layout.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }

        mPointerView = new ToolTipPointerView(context, mColor, mGravity);

        if (Gravity.isHorizontal(mGravity)) {
            mPointerView.setLayoutParams(new LayoutParams(mPointerSize, mPointerSize * 2));
            layout.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            mPointerView.setLayoutParams(new LayoutParams(mPointerSize * 2, mPointerSize));
            layout.setOrientation(LinearLayout.VERTICAL);
        }

        // For right and bottom alignment, the arrow goes at the beginning
        if ((mGravity & Gravity.RIGHT) == Gravity.RIGHT
                || (mGravity & Gravity.BOTTOM) == Gravity.BOTTOM) {
            layout.addView(mPointerView);
        }

        layout.addView(mContentView.get());

        // For left or top alignment, the arrow goes at the end
        if ((mGravity & Gravity.LEFT) == Gravity.LEFT || (mGravity & Gravity.TOP) == Gravity.TOP) {
            layout.addView(mPointerView);
        }

        return layout;
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private View anchor;
        private int gravity;
        private int color;
        private int pointerSize;
        private boolean dismissOnTouch;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder contentView(View v) {
            contentView = v;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder color(int color) {
            this.color = color;
            return this;
        }

        public Builder pointerSize(int size) {
            this.pointerSize = size;
            return this;
        }
        
        public Builder anchor(View anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder dismissOnTouch(boolean dismiss) {
            this.dismissOnTouch = dismiss;
            return this;
        }

        public ToolTip build() {
            return new ToolTip(this);
        }
    }

    private ToolTip(Builder builder) {
        setContentView(builder.contentView);
        setGravity(builder.gravity);
        setColor(builder.color);
        setPointerSize(builder.pointerSize);
        setAnchorView(builder.anchor);
        setView(makeView(builder.context));
        setDismissOnTouch(builder.dismissOnTouch);
    }

}
