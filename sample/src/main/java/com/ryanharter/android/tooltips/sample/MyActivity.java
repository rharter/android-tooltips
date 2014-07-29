package com.ryanharter.android.tooltips.sample;

import com.ryanharter.android.tooltips.ToolTip;
import com.ryanharter.android.tooltips.ToolTip.Builder;
import com.ryanharter.android.tooltips.ToolTipLayout;

import org.w3c.dom.Text;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {

    private static final int POINTER_SIZE = 15;

    private ToolTipLayout mToolTipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mToolTipLayout = (ToolTipLayout) findViewById(R.id.tooltip_container);

        final Button below = (Button) findViewById(R.id.tip_below);
        below.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                View contentView = createToolTipView("Tooltip below the button",
                        Color.WHITE, getResources().getColor(android.R.color.holo_red_light));
                contentView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                ));

                ToolTip t = new Builder(MyActivity.this)
                        .anchor(below)
                        .color(getResources().getColor(android.R.color.holo_red_light))
                        .gravity(Gravity.BOTTOM)
                        .pointerSize(POINTER_SIZE)
                        .contentView(contentView)
                        .build();
                mToolTipLayout.addTooltip(t);
            }
        });

        final Button left = (Button) findViewById(R.id.tip_left);
        left.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                View contentView = createToolTipView("Tooltip left of the button",
                        Color.WHITE, getResources().getColor(android.R.color.holo_blue_light));
                contentView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                ));

                // This one has a custom background on the tooltip that
                // adds rounded corners
                contentView.setBackgroundResource(R.drawable.bg_tooltip_blue);

                ToolTip t = new Builder(MyActivity.this)
                        .anchor(left)
                        .color(getResources().getColor(android.R.color.holo_blue_light))
                        .gravity(Gravity.LEFT)
                        .pointerSize(POINTER_SIZE)
                        .contentView(contentView)
                        .build();
                mToolTipLayout.addTooltip(t);
            }
        });

        final Button above = (Button) findViewById(R.id.tip_above);
        above.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                View contentView = createToolTipView("Tooltip above the button",
                        Color.BLACK, getResources().getColor(android.R.color.holo_green_light));
                contentView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                ));

                ToolTip t = new Builder(MyActivity.this)
                        .anchor(above)
                        .color(getResources().getColor(android.R.color.holo_green_light))
                        .gravity(Gravity.TOP)
                        .pointerSize(POINTER_SIZE)
                        .contentView(contentView)
                        .build();
                mToolTipLayout.addTooltip(t);
            }
        });

        final Button right = (Button) findViewById(R.id.tip_right);
        right.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                View contentView = createToolTipView("Tooltip right of the button",
                        Color.BLACK, getResources().getColor(android.R.color.holo_orange_light));
                contentView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                ));

                ToolTip t = new Builder(MyActivity.this)
                        .anchor(right)
                        .color(getResources().getColor(android.R.color.holo_orange_light))
                        .gravity(Gravity.RIGHT)
                        .pointerSize(POINTER_SIZE)
                        .contentView(contentView)
                        .build();
                mToolTipLayout.addTooltip(t);
            }
        });
    }

    public View createToolTipView(String text, int textColor, int bgColor) {
        float density = getResources().getDisplayMetrics().density;
        int padding = (int) (8 * density);

        TextView contentView = new TextView(MyActivity.this);
        contentView.setPadding(padding, padding, padding, padding);
        contentView.setText(text);
        contentView.setTextColor(textColor);
        contentView.setBackgroundColor(bgColor);
        return contentView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
