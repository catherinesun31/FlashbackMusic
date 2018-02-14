package com.android.flashbackmusicv000;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sun on 2/9/2018.
 */

public class Song implements Parcelable{
    private String title;
    private String lastLocation;
    private String lastTime;
    private String lastDate;
    private boolean favorite = false;
    private boolean dislike = false;

    public Song (String name) {
        title = name;
    }

    public void setLocation(String newLocation) {
        lastLocation = newLocation;
    }

    public void setTime(String newTime) {
        lastTime = newTime;
    }

    public void setDate(String newDate) {
        lastDate = newDate;
    }

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

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return lastLocation;
    }

    public String getLastTime() {
        return lastTime;
    }

    public String getLastDate() {
        return lastDate;
    }

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
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

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Song(Parcel in) {
        title = in.readString();
        lastLocation = in.readString();
        lastTime = in.readString();
        lastDate = in.readString();
        favorite = in.readByte() != 0;
        dislike = in.readByte() != 0;
    }

}
