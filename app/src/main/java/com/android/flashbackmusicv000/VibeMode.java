package com.android.flashbackmusicv000;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

public class VibeMode implements UserObserver {
    LinkedList<Song> vibeQueue;
    LinkedList<Song> unsorted;
    private SongSubject locationSubject;
    private SongSubject timeSubject;
    private SongSubject daySubject;

    public VibeMode(LinkedList<Song> songList) {
        unsorted = songList;
        vibeQueue = new LinkedList<Song>();
        //createQueue();
    }
    public void turnFlashBackModeOn() {

    }
    public LinkedList<Song> createQueue() {
        vibeQueue.add(unsorted.get(0));
        for (int i = 1; i < unsorted.size(); ++i) {
            for (int j = 0; j < vibeQueue.size(); ++j) {
                Song compare = vibeQueue.get(j);
                Song current = unsorted.get(i);
                int compareTotal = getScore(compare);
                int currentTotal = getScore(current);
                if (compareTotal == currentTotal) {
                    if (compare.isFavorite() && !current.isFavorite()) {
                        vibeQueue.add(j+1, current);
                    }
                    else if (!compare.isFavorite() && current.isFavorite()) {
                        vibeQueue.add(j, current);
                    }
                    else if (compare.isFavorite() && current.isFavorite()) {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            vibeQueue.add(j+1, current);
                        }
                        else {
                            vibeQueue.add(j, current);
                        }
                    }
                    else {
                        if (compare.getFullDate().after(current.getFullDate())) {
                            vibeQueue.add(j+1, current);
                        }
                        else {
                            vibeQueue.add(j, current);
                        }
                    }
                }
                else {
                    if (compareTotal > currentTotal) {
                        vibeQueue.add(j+1, current);
                    }
                    else {
                        vibeQueue.add(j, current);
                    }
                }
            }
        }
        //Make sure that no song that wasn't played at a previous time/day/place, or is disliked,
        //will be played in flashback mode
        for (Song song: vibeQueue) {
            if (getScore(song) == 0) vibeQueue.remove(song);
            if (song.isDislike()) vibeQueue.remove(song);
        }
        return vibeQueue;
    }

    public void update() {

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
        if (lastTime.equals(song.getLastTimeOfDay())) ++total;

        //if the location is within 1000 feet

        return total;
    }
}
