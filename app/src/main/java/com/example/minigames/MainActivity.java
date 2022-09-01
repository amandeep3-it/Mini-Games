package com.example.minigames;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public synchronized void onClick(View v) {
        if (v.getId() == R.id.hockey) {
            startActivity(new Intent(getBaseContext(), HockeyGame.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (v.getId() == R.id.tile_puzzle) {
            startActivity(new Intent(getBaseContext(), TilePuzzleGameMenu.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}