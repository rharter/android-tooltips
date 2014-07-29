package com.ryanharter.android.tooltips;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rharter on 7/24/14.
 */
public class ToolTipLayout extends RelativeLayout {

    private ToolTip mToolTip;
    private View mToolTipView;

    private List<ToolTip> mToolTips = new ArrayList<>();

    private boolean mDimensionsKnown;

    private int mTargetX;
    private int mTargetY;
    private WeakReference<View> mAnchorView;

    public ToolTipLayout(Context context) {
        this(context, null);
    }

    public ToolTipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolTipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Dismisses all tooltips currently being displayed using
     * the default animation.
     */
    public void dismiss() {
        dismiss(true);
    }

    /**
     * Dismisses all tooltips currently being displayed.
     *
     * @param animate True to animate the transition.
     */
    public void dismiss(boolean animate) {
        mToolTips.clear();

        if (animate) {

            final List<View> viewsToRemove = new ArrayList<>();
            List<Animator> a = new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) {
                a.add(ObjectAnimator.ofFloat(getChildAt(i), View.ALPHA, 0));
                viewsToRemove.add(getChildAt(i));
            }

            AnimatorSet s = new AnimatorSet();
            s.playTogether(a);
            s.addListener(new AnimatorListener() {
                @Override public void onAnimationStart(Animator animation) {
                }

                @Override public void onAnimationEnd(Animator animation) {
                    for (View v : viewsToRemove) {
                        removeView(v);
                    }
                }

                @Override public void onAnimationCancel(Animator animation) {
                }

                @Override public void onAnimationRepeat(Animator animation) {
                }
            });
            s.start();
        } else {
            removeAllViews();
        }
    }

    public void addTooltip(ToolTip tooltip) {
        addTooltip(tooltip, true);
    }

    private Map<ToolTip, Float> mFinalPositions = new HashMap<>();
    private boolean mShouldRemoveObserver;

    public void addTooltip(ToolTip tooltip, boolean animate) {
        mToolTips.add(tooltip);
        addView(tooltip.getView());
        requestLayout();

        if (animate) {
            // We use a two pass preDrawListener so we can get the post animation
            // position of the item and then animate it.
            getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                @Override public boolean onPreDraw() {

                    if (!mShouldRemoveObserver) {
                        mShouldRemoveObserver = true;

                        mFinalPositions.clear();

                        for (ToolTip t : mToolTips) {
                            float position = 0;
                            if (Gravity.isVertical(t.getGravity())) {
                                position = t.getView().getY();
                            } else {
                                position = t.getView().getX();
                            }

                            mFinalPositions.put(t, position);
                        }

                        return false;
                    }

                    mShouldRemoveObserver = false;
                    getViewTreeObserver().removeOnPreDrawListener(this);

                    List<Animator> animators = new ArrayList<>();

                    for (ToolTip t : mToolTips) {
                        t.getView().setAlpha(0);
                        animators.add(ObjectAnimator.ofFloat(t.getView(), View.ALPHA, 0, 1));

                        // TODO Restore this once I can fix the flicker
//                        final float finalPosition = mFinalPositions.get(t);
//                        if ((Gravity.VERTICAL_GRAVITY_MASK & t.getGravity()) == Gravity.TOP) {
//                            float start = finalPosition - t.getView().getHeight() / 2;
//                            t.getView().setY(start);
//
//                            animators.add(ObjectAnimator
//                                    .ofFloat(t.getView(), View.TRANSLATION_Y,
//                                            start,
//                                            finalPosition));
//                        } else if ((Gravity.VERTICAL_GRAVITY_MASK & t.getGravity())
//                                == Gravity.BOTTOM) {
//                            float start = finalPosition + t.getView().getHeight() / 2;
//                            t.getView().setY(start);
//
//                            animators.add(ObjectAnimator
//                                    .ofFloat(t.getView(), View.TRANSLATION_Y,
//                                            start,
//                                            finalPosition));
//                        } else if ((Gravity.HORIZONTAL_GRAVITY_MASK & t.getGravity())
//                                == Gravity.LEFT) {
//                            float start = finalPosition - t.getView().getWidth() / 2;
//                            t.getView().setX(start);
//
//                            animators.add(ObjectAnimator
//                                    .ofFloat(t.getView(), View.TRANSLATION_X,
//                                            start,
//                                            finalPosition));
//                        } else if ((Gravity.HORIZONTAL_GRAVITY_MASK & t.getGravity())
//                                == Gravity.RIGHT) {
//                            float start = finalPosition + t.getView().getWidth() / 2;
//                            t.getView().setX(start);
//
//                            animators.add(ObjectAnimator
//                                    .ofFloat(t.getView(), View.TRANSLATION_X,
//                                            start,
//                                            finalPosition));
//                        }
                    }

                    AnimatorSet s = new AnimatorSet();
                    s.playTogether(animators);
                    s.start();

                    return true;
                }
            });
        }
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int[] containerLocation = new int[2];
        getLocationOnScreen(containerLocation);

        for (ToolTip tip : mToolTips) {
            int[] anchorLocation = new int[2];
            if (tip.getAnchorView() != null) {
                tip.getAnchorView().getLocationOnScreen(anchorLocation);
            } else {
                // Default to a centered view
                anchorLocation[0] = containerLocation[0] / 2;
                anchorLocation[1] = containerLocation[1] / 2;
            }

            int x = anchorLocation[0] - containerLocation[0];
            int y = anchorLocation[1] - containerLocation[1];

            if ((tip.getGravity() & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
                y -= tip.getView().getHeight();
            } else if ((tip.getGravity() & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
                if (tip.getAnchorView() != null) {
                    y += tip.getAnchorView().getHeight();
                }
            } else if ((tip.getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT) {
                x -= tip.getView().getWidth();
            } else if ((tip.getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT) {
                if (tip.getAnchorView() != null) {
                    x += tip.getAnchorView().getWidth();
                }
            }

            // The tooltip is above or below the anchor view, so
            // center the location on the X axis, otherwise center
            // on the Y axis.
            ToolTipPointerView pointer = tip.getPointerView();
            if (Gravity.isVertical(tip.getGravity())) {
                x += tip.getAnchorView().getWidth() / 2;
                pointer.setX(x - containerLocation[0] - pointer.getWidth() / 2);
                tip.getView().setY(y);
            } else {
                y += tip.getAnchorView().getHeight() / 2 - tip.getView().getHeight() / 2;
                tip.getView().setY(y);
                tip.getView().setX(x);
                tip.getPointerView().setY(tip.getView().getHeight() / 2 - tip.getPointerView().getHeight() / 2);
            }
        }
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return super.onTouchEvent(event);
    }

    public void alignPointer(View pointer, int position, int gravity) {
        View parent = (View) pointer.getParent();
        int[] parentLocation = new int[2];
        parent.getLocationOnScreen(parentLocation);

        if (Gravity.isVertical(gravity)) {
            pointer.setX(position - parentLocation[0] - pointer.getWidth() / 2);
        } else {
            pointer.setY(position - parentLocation[1] - pointer.getHeight() / 2);
        }
    }
}
