package com.example.minigames;

import android.content.Context;
import android.util.AttributeSet;

import android.widget.RelativeLayout;

public class HockeyBall extends androidx.appcompat.widget.AppCompatImageView {

    private RelativeLayout parent = null;

    public boolean insidePlayer = false;
    public int speedX = 0, speedY = 0;

    public HockeyBall(Context context) {
        super(context);
    }
    public HockeyBall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public HockeyBall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.parent = (RelativeLayout) this.getParent();
    }

    public synchronized void setCenterPositions(float x, float y) {
        x -= this.getRadius();
        if ((x >= this.X_Min_Limit()) && ((x + this.getDiameter()) <= this.X_Max_Limit())) {
            this.setTranslationX(x);
        }
        y -= this.getRadius();
        if ((y >= this.Y_Min_Limit()) && ((y + this.getDiameter()) <= this.Y_Max_Limit())) {
            this.setTranslationY(y);
        }
    }

    public synchronized void checkTouchPlayer(HockeyPlayer player) {
        float both_radius = this.getRadius() + player.getRadius(),
              player_ball_dist = (float) Math.sqrt(
                      Math.pow((player.getAbsoluteX() + player.getRadius()) - (this.getGoingAbsoluteX() + this.getRadius()), 2) +
                      Math.pow((player.getAbsoluteY() + player.getRadius()) - (this.getGoingAbsoluteY() + this.getRadius()), 2)
              );
        if ((player_ball_dist <= both_radius) && !this.insidePlayer) {
            this.insidePlayer = true;
            this.speedX = (int) player.velocityX * ((this.getAbsoluteX() < player.getAbsoluteX()) ? -1 : 1);
            this.speedY = (int) player.velocityY * ((this.getAbsoluteY() < player.getAbsoluteY()) ? -1 : 1);
        } else if ((player_ball_dist > both_radius) && this.insidePlayer) this.insidePlayer = false;
    }

    public synchronized void updatePosition() {
        float x = this.getGoingX(), y = this.getGoingY();

        if ((x >= this.X_Min_Limit()) && ((x + this.getDiameter()) <= this.X_Max_Limit())) this.setTranslationX(x);
        else this.speedX *= -1;

        if ((y >= this.Y_Min_Limit()) && ((y + this.getDiameter()) <= this.Y_Max_Limit())) this.setTranslationY(y);
        else this.speedY *= -1;
    }

    public synchronized void resetPosition() {
        this.insidePlayer = false;
        this.speedX = 0;
        this.speedY = 0;
        this.setCenterPositions((float) (this.X_Max_Limit() / 2), (float) (this.Y_Max_Limit() / 2));
    }

    public float getRadius() { return (float) (this.getWidth() / 2); }
    public float getDiameter() { return this.getWidth(); }
    public int getAbsoluteX() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[0];
    }
    public float getCenterX() { return this.getTranslationX() - this.getRadius(); }
    public int getAbsoluteY() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[1];
    }
    public float getCenterY() { return this.getTranslationY() - this.getRadius(); }

    public float getGoingX() { return this.getTranslationX() + this.speedX; }
    public float getGoingAbsoluteX() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[0] + this.speedX;
    }
    public float getGoingY() { return this.getTranslationY() + this.speedY; }
    public float getGoingAbsoluteY() {
        int[] l = new int[2];
        this.getLocationOnScreen(l);
        return l[1] + this.speedY;
    }

    public float X_Min_Limit() { return (this.parent != null) ? this.parent.getTranslationX() : 0; }
    public int X_Max_Limit() { return (this.parent != null) ? this.parent.getWidth() : 0; }
    public float Y_Min_Limit() { return (this.parent != null) ? this.parent.getTranslationY() : 0; }
    public int Y_Max_Limit() { return (this.parent != null) ? this.parent.getHeight() : 0; }
}