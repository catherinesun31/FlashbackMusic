package com.android.flashbackmusicv000;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by cailintreseder on 2/18/18.
 */

public class FlashBackMode {
    ArrayList<Song> flashQueue;

    ArrayList<Song> unsorted;

    public void FlashBackMode(ArrayList<Song> songList) {
        unsorted = songList;
        createQueue();
    }

    public void turnFlashBackModeOn() {

    }

    public void createQueue() {
        flashQueue.add(unsorted.get(0));
        for (int i = 1; i < unsorted.size(); ++i) {
            for (int j = 0; j < flashQueue.size(); ++j) {

            }
        }
    }

    public int getScore(Song song) {
        int total = 0;
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //return dateFormat.format(currentTime);

        return total;
    }
}