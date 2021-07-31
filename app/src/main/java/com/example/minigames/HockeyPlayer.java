package com.example.minigames;

import android.content.Context;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.VelocityTracker;

import android.widget.RelativeLayout;

public class HockeyPlayer extends androidx.appcompat.widget.AppCompatImageView {

    private RelativeLayout parent = null;
    private VelocityTracker velocity_tracker = null;
    public float velocityX = 0, velocityY = 0;
    public int wins = 0;

    public HockeyPlayer(Context context) {
        super(context);
    }
    public HockeyPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HockeyPlayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.parent = (RelativeLayout) this.getParent();
    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float new_x = event.getX(), new_y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.velocity_tracker.addMovement(event);
                this.moveCenterPositions(new_x, new_y);

                this.velocity_tracker.computeCurrentVelocity(1000, 25);
                this.setVelocity(
                    this.velocity_tracker.getXVelocity(),
                    this.velocity_tracker.getYVelocity()
                );
                break;

            case MotionEvent.ACTION_UP:
                this.velocityX = 5;
                this.velocityY = 5;
                this.performClick();
                return true;

            case MotionEvent.ACTION_DOWN:
                if (this.velocity_tracker == null) this.velocity_tracker = VelocityTracker.obtain();
                else this.velocity_tracker.clear();
                this.velocity_tracker.addMovement(event);
                return true;

            case MotionEvent.ACTION_CANCEL:
                this.velocity_tracker.recycle();
                break;
        }
        return false;
    }

    @Override
    public synchronized boolean performClick() {
        super.performClick();
        return true;
    }

    public synchronized void moveCenterPositions(float x, float y) {
        this.setX(this.getNewCenterX(x));
        this.setY(this.getNewCenterY(y));
    }

    public synchronized void setCenterPositions(float x, float y) {
        this.setX(x - this.getRadius());
        this.setY(y - this.getRadius());
    }

    public synchronized void setX(float x) {
        if ((x >= this.X_Min_Limit()) && ((x + this.getDiameter()) <= this.X_Max_Limit())) {
            this.setTranslationX(x);
        }
    }

    public synchronized void setY(float y) {
        if ((y >= this.Y_Min_Limit()) && ((y + this.getDiameter()) <= this.Y_Max_Limit())) {
            this.setTranslationY(y);
        }
    }

    public synchronized void resetPosition() {
        this.velocityX = 0;
        this.velocityY = 0;
        this.setCenterPositions((float) (this.X_Max_Limit() / 2), (float) (this.Y_Max_Limit() / 2));
    }

    public synchronized void setVelocity(float x, float y) { this.set_velocityX(x); this.set_velocityY(y); }
    public synchronized void set_velocityX(float x) { this.velocityX = Math.max(x, 5); }
    public synchronized void set_velocityY(float y) { this.velocityY = Math.max(y, 5); }

    public float getNewCenterX(float x) { return this.getCenterX() + x; }
    public float getNewCenterY(float y) { return this.getCenterY() + y; }

    public float getRadius() { return (float) (this.getWidth() / 2); }
    public float getDiameter() { return this.getWidth(); }
    public int getAbsoluteX() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[0];
    }
    public int getAbsoluteY() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[1];
    }
    public float getCenterX() { return this.getTranslationX() - this.getRadius(); }
    public float getCenterY() { return this.getTranslationY() - this.getRadius(); }

    public float X_Min_Limit() { return (this.parent != null) ? this.parent.getTranslationX() : 0; }
    public int X_Max_Limit() { return (this.parent != null) ? this.parent.getWidth() : 0; }
    public float Y_Min_Limit() { return (this.parent != null) ? this.parent.getTranslationY() : 0; }
    public int Y_Max_Limit() { return (this.parent != null) ? this.parent.getHeight() : 0; }
}