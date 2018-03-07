package com.android.flashbackmusicv000;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Sun on 2/9/2018.
 */


/*
 * Class that establishes the Song object. Contains song information such as title, last location,
 * last date, and whether the song is favorited or disliked. Contains getters/setters. Uses a parcel
 * to pass Song information between classes.
 */
public class Song implements Parcelable{
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

    // Parcel uses this
    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeInt(songId);
        out.writeString(title);
        out.writeString(lastLocation);
        out.writeString(lastTime);
        out.writeString(lastDate);
        out.writeByte((byte) (favorite ? 1 : 0));
        out.writeByte((byte) (dislike ? 1 : 0));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Song(Parcel in) {

        songId = in.readInt();
        title = in.readString();
        lastLocation = in.readString();
        lastTime = in.readString();
        lastDate = in.readString();
        favorite = in.readByte() != 0;
        dislike = in.readByte() != 0;
    }

    public boolean Undo() {
        try {

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}
