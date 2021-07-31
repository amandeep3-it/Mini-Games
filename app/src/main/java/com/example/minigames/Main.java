package com.example.minigames;

import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static class TilePuzzleGameRecordType {
        String name = "", time = "", date = "";
        int steps = 0;
    }

    public synchronized Boolean SubmitTilePuzzleGameRecord(TilePuzzleGameRecordType data) {
        try {
            String url = "http://localhost:4040/submit_tile_puzzle_game_record.inc.php?name=" + data.name + "&time=" + data.time + "&date=" + data.date + "&steps=" + data.steps;

            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.disconnect();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}