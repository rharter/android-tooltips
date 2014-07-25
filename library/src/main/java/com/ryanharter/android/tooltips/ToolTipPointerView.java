package com.ryanharter.android.tooltips;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.Gravity;
import android.view.View;

/**
 * Created by rharter on 7/24/14.
 */
public class ToolTipPointerView extends View {

    private Paint mPaint;
    private int mGravity;

    public ToolTipPointerView(Context context, int color, int gravity) {
        super(context);
        mGravity = gravity;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Style.FILL);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path p = new Path();
        if ((Gravity.VERTICAL_GRAVITY_MASK & mGravity) == Gravity.TOP) {
            // Top
            p.moveTo(0, 0);
            p.lineTo(getWidth() / 2, getHeight());
            p.lineTo(getWidth(), 0);
            p.lineTo(0, 0);
        } else if ((Gravity.VERTICAL_GRAVITY_MASK & mGravity) == Gravity.BOTTOM) {
            // Bottom
            p.moveTo(0, getHeight());
            p.lineTo(getWidth() / 2, 0);
            p.lineTo(getWidth(), getHeight());
            p.lineTo(0, getHeight());
        } else if ((Gravity.LEFT & mGravity) == Gravity.LEFT) {
            // Left
            p.moveTo(0, 0);
            p.lineTo(getWidth(), getHeight() / 2);
            p.lineTo(0, getHeight());
            p.lineTo(0, 0);
        } else if ((Gravity.RIGHT & mGravity) == Gravity.RIGHT) {
            // Right
            p.moveTo(getWidth(), 0);
            p.lineTo(0, getHeight() / 2);
            p.lineTo(getWidth(), getHeight());
            p.lineTo(getWidth(), 0);
        }
        canvas.drawPath(p, mPaint);
    }

}
