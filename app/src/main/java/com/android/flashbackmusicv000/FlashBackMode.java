package com.android.flashbackmusicv000;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by cailintreseder on 2/18/18.
 */

public class FlashBackMode {
    LinkedList<Song> flashQueue;
    LinkedList<Song> unsorted;

    public void FlashBackMode(LinkedList<Song> songList) {
        unsorted = songList;
        createQueue();
    }
    public void turnFlashBackModeOn() {

    }
    public void createQueue() {
        flashQueue.add(unsorted.get(0));
        for (int i = 1; i < unsorted.size(); ++i) {
            for (int j = 0; j < flashQueue.size(); ++j) {
                Song compare = flashQueue.get(j);
                Song current = unsorted.get(i);
                int compareTotal = getScore(compare);
                int currentTotal = getScore(current);
                if (compareTotal == currentTotal) {
                    if (compare.isFavorite() && !current.isFavorite()) {
                        flashQueue.add(j+1, current);
                    }
                    else if (!compare.isFavorite() && current.isFavorite()) {
                        flashQueue.add(j, current);
                    }
                    else if (compare.isFavorite() && current.isFavorite()) {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            flashQueue.add(j+1, current);
                        }
                        else {
                            flashQueue.add(j, current);
                        }
                    }
                    else {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            flashQueue.add(j+1, current);
                        }
                        else {
                            flashQueue.add(j, current);
                        }
                    }
                }
                else {
                    if (compareTotal > currentTotal) {
                        flashQueue.add(j+1, current);
                    }
                    else {
                        flashQueue.add(j, current);
                    }
                }
            }
        }
        //Make sure that no song that wasn't played at a previous time/day/place, or is disliked,
        //will be played in flashback mode
        for (Song song: flashQueue) {
            if (getScore(song) == 0) flashQueue.remove(song);
            if (song.isDislike()) flashQueue.remove(song);
        }
    }

    public int getScore(Song song) {
        int total = 0;

        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        if (dateFormat.format(currentTime).equals(song.getLastDay())) ++total;

        int time = 0;
        dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String hour = dateFormat.format(currentTime).substring(0, 2);
        String lastTime = "";
        if (Integer.parseInt(hour) >= 5 && Integer.parseInt(hour) < 11) {
            lastTime = "Morning";
        }
        else if (Integer.parseInt(hour) >= 11 && Integer.parseInt(hour) < 17) {
            lastTime = "Afternoon";
        }
        else {
            lastTime = "Night";
        }
        if (lastTime.equals(song.getLastTime())) ++total;

        //if the location is within 1000 feet

        return total;
    }
}
