package com.example.minigames;

import android.content.Context;
import android.util.AttributeSet;

public class Tile extends androidx.appcompat.widget.AppCompatButton {
    public int tile_length = 0;

    public float startX = 0, startY = 0;

    public Tile(Context context) {
        super(context);
        this.initial();
    }
    public Tile(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initial();
    }
    public Tile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initial();
    }

    private boolean initial_DONE = false;
    public synchronized void initial() {
        if (this.initial_DONE) return;
        this.initial_DONE = true;
        this.setBackgroundResource(R.drawable.tile);
        this.setTextSize(30);
    }

    @Override
    public synchronized boolean performClick() {
        super.performClick();
        return true;
    }
}
