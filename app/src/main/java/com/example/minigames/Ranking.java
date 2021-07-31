package com.example.minigames;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.text.DateFormatSymbols;
import java.util.Locale;

import android.view.Gravity;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.graphics.Typeface;

import org.json.JSONArray;

public class Ranking extends Activity {
    private boolean GetRankings_ALLOW = true;

    private LinearLayout names, steps, times, dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        this.names = findViewById(R.id.names);
        this.steps = findViewById(R.id.steps);
        this.times = findViewById(R.id.times);
        this.dates = findViewById(R.id.dates);

        if (!this.GetRankings_ALLOW) return;
        this.GetRankings_ALLOW = false;

        new Thread() {
            public void run() {
                JSONArray output = GetRankings();
                if (output != null) InsertRankings(output);
                GetRankings_ALLOW = true;
            }
        }.start();
    }

    private void InsertRankings(JSONArray output) {
        for (int x=0;x<output.length();x++) {
            try {
                JSONArray out = new JSONArray(output.getString(x));

                TextView text = new TextView(this);
                text.setText(String.format(Locale.getDefault(),"      %d %s", (x + 1), out.getString(0)));
                text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
                text.setGravity(Gravity.CENTER_VERTICAL);
                text.setTextSize(20);
                text.setTypeface(Typeface.SERIF);
                this.names.addView(text);

                text = new TextView(this);
                text.setText(out.getString(2));
                text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
                text.setGravity(Gravity.CENTER);
                text.setTextSize(20);
                text.setTypeface(Typeface.SERIF);
                this.steps.addView(text);

                text = new TextView(this);
                String[] time = out.getString(1).split(":");
                text.setText(String.format(Locale.getDefault(),"%s%s", (Integer.parseInt(time[1]) > 0 ? (time[1] + " Min") : ""), (Integer.parseInt(time[2]) > 0 ? (" " + time[2] + " Sec") : "")));
                text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
                text.setGravity(Gravity.CENTER_VERTICAL);
                text.setTextSize(11);
                text.setTypeface(Typeface.SERIF);
                this.times.addView(text);

                text = new TextView(this);
                String[] date = out.getString(3).split("-");
                text.setText(String.format(Locale.getDefault(),"%s %s %s", date[2], new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1], date[0]));
                text.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 100));
                text.setGravity(Gravity.CENTER);
                text.setTextSize(11);
                text.setTypeface(Typeface.SERIF);
                this.dates.addView(text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized JSONArray GetRankings() {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL("http://localhost:4040/rankings.inc.php")).openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String string = reader.readLine();
            reader.close();
            conn.disconnect();

            return new JSONArray(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}