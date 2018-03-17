package com.android.flashbackmusicv000.TrackList.model;
import java.util.Date;

/**
 * Created by Sun on 2/9/2018.
 */


/*
 * Class that establishes the Song object. Contains song information such as title, last location,
 * last date, and whether the song is favorited or disliked. Contains getters/setters. Uses a parcel
 * to pass Song information between classes.
 */
public class Song{
    //Janice add in:
    private int songId;

    private String title;
    private String lastLocation;
    private String lastTimeOfDay;
    private String lastTime;
    private String lastDate;
    private String lastDay;
    private Date date;
    private boolean favorite = false;
    private boolean dislike = false;

    // Song Constructor
    public Song (String name, int songId) {
        title = name;
        this.songId = songId;
    }

    // Setters for Song
    public void setLocation(String newLocation) {
        lastLocation = newLocation;
    }

    public void setTime(String newTime) {
        lastTime = newTime;
        String hour = newTime.substring(0, 2);
        if (Integer.parseInt(hour) >= 5 && Integer.parseInt(hour) < 11) {
            lastTimeOfDay = "Morning";
        }
        else if (Integer.parseInt(hour) >= 11 && Integer.parseInt(hour) < 17) {
            lastTimeOfDay = "Afternoon";
        }
        else {
            lastTimeOfDay = "Night";
        }
    }

    public void setFullDate(Date date) { this.date = date; }

    public void setDate(String newDate) {
        lastDate = newDate;
    }

    public void setDay(String newDay) { lastDay = newDay; }

    public void dislike() {
        dislike = true;
    }

    public void favorite() {
        favorite = true;
    }

    public void neutral() {
        favorite = false;
        dislike = false;
    }

    // Getters for Song
    public int getSongId() {return songId;}

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return lastLocation;
    }

    public String getLastTimeOfDay() {
        return lastTimeOfDay;
    }

    public String getLastTime() { return lastTime; }

    public String getLastDate() {
        return lastDate;
    }

    public String getLastDay() { return lastDay; }

    public boolean isNeutral() { return !(favorite || dislike); }

    public boolean isFavorite() { return favorite; }

    public boolean isDislike() { return dislike; }

    public Date getFullDate() { return date; }

    public boolean Undo() {
        try {

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
