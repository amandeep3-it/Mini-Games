package com.example.minigames;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

import android.view.View;

public class TilePuzzleGameMenu extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile_puzzle_game_menu);
    }

    public synchronized void onClick(View v) {
        if (v.getId() == R.id.x3) {
            Intent intent = new Intent(getBaseContext(), TilePuzzleGame.class);
            intent.putExtra("length", 3);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (v.getId() == R.id.x4) {
            Intent intent = new Intent(getBaseContext(), TilePuzzleGame.class);
            intent.putExtra("length", 4);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (v.getId() == R.id.x5) {
            Intent intent = new Intent(getBaseContext(), TilePuzzleGame.class);
            intent.putExtra("length", 5);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (v.getId() == R.id.ranking) {
            startActivity(new Intent(getBaseContext(), Ranking.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
