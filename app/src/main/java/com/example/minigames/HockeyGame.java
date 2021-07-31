package com.example.minigames;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

import java.util.Locale;

import android.view.View;
import android.widget.TextView;
import android.media.MediaPlayer;

public class HockeyGame extends Activity {
    private HockeyPlayer winner = null;

    private MediaPlayer background_music = null;

    private HockeyBall ball;
    private HockeyPlayer player1;
    private HockeyPlayer player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hockey_game);

        this.ball = findViewById(R.id.ball);
        this.player1 = findViewById(R.id.player1);
        this.player2 = findViewById(R.id.player2);

        this.background_music = MediaPlayer.create(this, R.raw.background_music);
        this.background_music.setLooping(true);
        this.background_music.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (runGame()) handler.postDelayed(this, 0);
            }
        }, 0);
    }

    private boolean onWindowFocusChanged_ONCE = false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.onWindowFocusChanged_ONCE) return;
        this.onWindowFocusChanged_ONCE = true;

        super.onWindowFocusChanged(hasFocus);

        this.ball.resetPosition();
        this.player1.resetPosition();
        this.player2.resetPosition();
    }

    public synchronized boolean runGame() {
        if (this.scored()) {
            if (this.player1 == this.winner) {
                ((TextView) findViewById(R.id.winner)).setText(this.getString(R.string.hockey_game_player_2_won));
                findViewById(R.id.container).setVisibility(View.VISIBLE);
                findViewById(R.id.goals).setVisibility(View.GONE);
                this.ball.setVisibility(View.GONE);
                this.player1.setVisibility(View.GONE);
                this.player2.setVisibility(View.GONE);
                return false;
            } else if (this.player2 == this.winner) {
                ((TextView) findViewById(R.id.winner)).setText(this.getString(R.string.hockey_game_player_2_won));
                findViewById(R.id.container).setVisibility(View.VISIBLE);
                findViewById(R.id.goals).setVisibility(View.GONE);
                this.ball.setVisibility(View.GONE);
                this.player1.setVisibility(View.GONE);
                this.player2.setVisibility(View.GONE);
                return false;
            }
            this.ball.resetPosition();
            this.player1.resetPosition();
            this.player2.resetPosition();
        }
        this.ball.checkTouchPlayer(this.player1);
        this.ball.checkTouchPlayer(this.player2);
        this.ball.updatePosition();
        return true;
    }

    public synchronized boolean scored() {
        if (this.winner != null) return false;

        int WiningStreak = 5;

        float ball_x = this.ball.getTranslationX(),
              ball_y = this.ball.getTranslationY();
        if (
            (ball_x >= (this.ball.X_Max_Limit() / 3.0)) &&
            (ball_x <= ((this.ball.X_Max_Limit() / 3.0) * 2)) &&
            ((ball_y + this.ball.getDiameter()) >= (this.ball.Y_Max_Limit() - 10))
        ) {
            this.player1.wins++;
            if (this.player1.wins == WiningStreak) this.winner = this.player1;
            ((TextView) findViewById(R.id.player_1_wins)).setText(String.format(Locale.getDefault(),"%d", this.player1.wins));
            return true;
        } else if (
            (ball_x >= (this.ball.X_Max_Limit() / 3.0)) &&
            (ball_x <= ((this.ball.X_Max_Limit() / 3.0) * 2)) &&
            (ball_y <= 10)
        ) {
            this.player2.wins++;
            if (this.player2.wins == WiningStreak) this.winner = this.player2;
            ((TextView) findViewById(R.id.player_2_wins)).setText(String.format(Locale.getDefault(),"%d", this.player2.wins));
            return true;
        }
        return false;
    }

    public synchronized void onClick(View v) {
        if (v.getId() == R.id.play_again) {
            startActivity(new Intent(getBaseContext(), HockeyGame.class));
            this.finish();
        } else if (v.getId() == R.id.main_menu) {
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            this.finish();
        }
    }

    @Override
    public void finish() {
        this.background_music.stop();
        this.background_music = null;
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}