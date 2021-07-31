package com.example.minigames;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.content.Intent;

import android.view.View;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TilePuzzleGame extends Activity {
    private final Main main = new Main();
    private int game_length = 0, tile_length = 0;

    private int[][] sorted, initial;
    private int emptyX = 0, emptyY = 0;

    private int steps = 0;
    private final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    private RelativeLayout game_box;
    private Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile_puzzle_game);
    }

    private boolean onWindowFocusChanged_ONCE = false;
    @Override
    public synchronized void onWindowFocusChanged(boolean hasFocus) {
        if (this.onWindowFocusChanged_ONCE) return;
        this.onWindowFocusChanged_ONCE = true;
        super.onWindowFocusChanged(hasFocus);

        this.game_length = getIntent().getIntExtra("length", 3);
        this.sorted = new int[(this.game_length * this.game_length) - 1][3];
        this.initial = new int[(this.game_length * this.game_length) - 1][3];

        this.game_box = findViewById(R.id.game_box);
        this.chronometer = findViewById(R.id.chronometer);

        RelativeLayout background = findViewById(R.id.background);
        int background_height = background.getHeight();
        int background_width = background.getWidth();
        int box_length = (background_width / 100) * 100;
        this.tile_length = box_length / this.game_length;

        this.game_box.setLayoutParams(new RelativeLayout.LayoutParams(box_length, box_length));
        this.game_box.setTranslationX((float) ((background_width / 2) - (box_length / 2)));
        this.game_box.setTranslationY((float) ((background_height / 2) - (box_length / 2)));

        ArrayList<Integer> rand_list = new ArrayList<>();
        for (int x=0; x<this.initial.length; x++) {
            int rand = (int) (Math.random() * (this.initial.length) + 1);
            if (rand_list.contains(rand)) x--;
            else rand_list.add(rand);
        }

        int count = 0;
        for (int y=0; y<this.game_length; y++) {
            for (int x=0; x<this.game_length; x++) {
                if ((y != (this.game_length - 1)) || (x != (this.game_length - 1))) {
                    count++;
                    this.sorted[count - 1][0] = count;
                    this.sorted[count - 1][1] = x;
                    this.sorted[count - 1][2] = y;

                    this.initial[count - 1][0] = rand_list.get(count - 1);
                    this.initial[count - 1][1] = x;
                    this.initial[count - 1][2] = y;

                    Tile t = new Tile(this);
                    t.tile_length = this.tile_length;
                    t.setLayoutParams(new RelativeLayout.LayoutParams(this.tile_length, this.tile_length));
                    t.setTranslationY(y * this.tile_length);
                    t.setTranslationX(x * this.tile_length);
                    t.setTextColor(getColor(R.color.black));
                    t.setText(String.format(Locale.getDefault(),"%d", rand_list.get(count - 1)));
                    t.setOnTouchListener((View v, MotionEvent event) -> {
                        Tile tile = (Tile) v;
                        this.tileMove(tile, event);

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                tile.performClick();
                                return true;
                            case MotionEvent.ACTION_DOWN:
                                return true;
                        }
                        return false;
                    });
                    this.game_box.addView(t);
                } else {
                    this.emptyY = this.tile_length * y;
                    this.emptyX = this.tile_length * x;
                }
            }
        }
        this.chronometer.setBase(SystemClock.elapsedRealtime());
        this.chronometer.start();
    }

    public synchronized void tileMove(Tile t, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                t.startX = event.getX();
                t.startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float changeX = event.getX() - t.startX,
                        changeY = event.getY() - t.startY,
                        currentX = t.getTranslationX(),
                        currentY = t.getTranslationY();

                if (Math.abs(changeX) > Math.abs(changeY)) {
                    if (changeX < 0) {
                        if (((currentX - this.tile_length) == this.emptyX) && (currentY == this.emptyY)) {
                            t.setTranslationX(this.emptyX);
                            this.emptyX += this.tile_length;
                            this.steps++;
                        }
                    } else if (changeX > 0) {
                        if (((currentX + this.tile_length) == this.emptyX) && (currentY == this.emptyY)) {
                            t.setTranslationX(this.emptyX);
                            this.emptyX -= this.tile_length;
                            this.steps++;
                        }
                    }
                } else {
                    if (changeY < 0) {
                        if (((currentY - this.tile_length) == this.emptyY) && (currentX == this.emptyX)) {
                            t.setTranslationY(this.emptyY);
                            this.emptyY += this.tile_length;
                            this.steps++;
                        }
                    } else if (changeY > 0) {
                        if (((currentY + this.tile_length) == this.emptyY) && (currentX == this.emptyX)) {
                            t.setTranslationY(this.emptyY);
                            this.emptyY -= this.tile_length;
                            this.steps++;
                        }
                    }
                }
                if (this.checkGameWon()) {
                    this.chronometer.stop();
                    findViewById(R.id.reset).setVisibility(View.GONE);
                    this.game_box.setBackgroundColor(getColor(R.color.colorAccent));
                    findViewById(R.id.container).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.time)).setText(String.format(Locale.getDefault(),"00:%s", this.chronometer.getText().toString()));
                    ((TextView) findViewById(R.id.steps)).setText(String.format(Locale.getDefault(),"%d", this.steps));
                    ((TextView) findViewById(R.id.date)).setText(this.date);
                }
                break;
        }
    }

    public synchronized boolean checkGameWon() {
        for (int y=0; y<this.sorted.length; y++) {
            for (int x=0; x<this.sorted.length; x++) {
                Tile t = (Tile) this.game_box.getChildAt(x);
                if (t.getText().toString().equals(Integer.toString(this.sorted[y][0]))) {
                    int num_x = (int) (t.getTranslationX() / this.tile_length);
                    int num_y = (int) (t.getTranslationY() / this.tile_length);
                    if ((num_x != this.sorted[y][1]) || (num_y != this.sorted[y][2])) {
                        return (
                            (y == ((this.game_length * this.game_length) - 3)) &&
                            this.checkSecondLastTile() && this.checkLastTile()
                        );
                    }
                }
            }
        }
        return true;
    }

    public synchronized boolean checkSecondLastTile() {
        for (int x=0; x<this.game_box.getChildCount(); x++) {
            Tile b = (Tile) this.game_box.getChildAt(x);
            if (Integer.parseInt(b.getText().toString()) == ((this.game_length * this.game_length) - 1)) {
                int num_x = (int) (b.getTranslationX() / this.tile_length);
                int num_y = (int) (b.getTranslationY() / this.tile_length);
                if (
                    ((this.game_length == 3) && (num_x == 0) && (num_y == 2)) ||
                    ((this.game_length == 4) && (num_x == 1) && (num_y == 3)) ||
                    ((this.game_length == 5) && (num_x == 2) && (num_y == 4))
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean checkLastTile() {
        for (int x=0; x<this.game_box.getChildCount(); x++) {
            Tile b = (Tile) this.game_box.getChildAt(x);
            if (Integer.parseInt(b.getText().toString()) == ((this.game_length * this.game_length) - 2)) {
                int num_x = (int) (b.getTranslationX() / this.tile_length);
                int num_y = (int) (b.getTranslationY() / this.tile_length);
                if (
                    ((this.game_length == 3) && (num_x == 1) && (num_y == 2)) ||
                    ((this.game_length == 4) && (num_x == 2) && (num_y == 3)) ||
                    ((this.game_length == 5) && (num_x == 3) && (num_y == 4))
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean SubmitTilePuzzleGameRecord_ALLOW = true;
    public synchronized void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (!this.SubmitTilePuzzleGameRecord_ALLOW) return;
            this.SubmitTilePuzzleGameRecord_ALLOW = false;

            Main.TilePuzzleGameRecordType data = new Main.TilePuzzleGameRecordType();
            data.name = ((EditText) findViewById(R.id.name)).getText().toString();
            if (data.name.equals("")) {
                findViewById(R.id.name).setBackgroundColor(getColor(R.color.red));
                return;
            }
            data.time = ((TextView) findViewById(R.id.time)).getText().toString();
            data.date = this.date;
            data.steps = this.steps;

            new Thread() {
                @Override
                public void run() {
                    super.run();

                    if (main.SubmitTilePuzzleGameRecord(data)) {
                        startActivity(new Intent(getBaseContext(), Ranking.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    SubmitTilePuzzleGameRecord_ALLOW = true;
                }
            }.start();

        } else if (v.getId() == R.id.reset) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i=0; i<this.game_box.getChildCount(); i++) {
                list.add(i + 1);
            }
            Collections.shuffle(list);

            for (int x=0; x<this.game_box.getChildCount(); x++) {
                Tile b = (Tile) this.game_box.getChildAt(x);
                b.setText(String.format(Locale.getDefault(),"%d", list.get(x)));
                b.setTranslationX(this.initial[x][1] * this.tile_length);
                b.setTranslationY(this.initial[x][2] * this.tile_length);
            }
            this.emptyX = (this.game_length - 1) * this.tile_length;
            this.emptyY = (this.game_length - 1) * this.tile_length;

            this.chronometer.setBase(SystemClock.elapsedRealtime());
            this.chronometer.start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}