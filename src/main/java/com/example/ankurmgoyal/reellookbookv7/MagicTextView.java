package com.example.ankurmgoyal.reellookbookv7;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class MagicTextView extends TextView {

    public MagicTextView(Context context) {
        super(context);
    }


    public MagicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MagicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            super.draw(canvas);
        }
    }
}