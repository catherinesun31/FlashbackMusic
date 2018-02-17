package com.android.flashbackmusicv000; /**
 * Created by janic on 2/10/18.
 */
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* com.android.flashbackmusicv000.Album is a class to store each of our songs by their corresponding album.
 * It is iterable so it can hold an array of songs.
 */

public class Album implements /*Iterable<T>8*/ Parcelable {
    ArrayList<Song> songs;  // this contains the actual elements of the album

    String name;

    // Constructor that takes a "raw" array and stores it
    public Album(String name, ArrayList<Song> songs) {
        this.name = name;

        this.songs = songs;

    }

    /**
     * Overloaded the constructor, which allows for single song add when a new Album is made.
     * There seem to be a couple of issues with generics, so I hope the implementation is fine.
     * @param name
     * @param song
     */
    public Album(String name, Song song){

        this.name = name;
        songs = new ArrayList<Song>();
        songs.add(song);

    }

    public void addSong(Song song){

        songs.add(song);

    }

    public void addSongs(Collection<Song> songsToAdd){

        this.songs.addAll(songsToAdd);

    }

    //Inner class, the iterator.
    class MyIterator implements Iterator<Song> {

        int current = 0;  // the current element we are looking at

        // return whether or not there are more songs in the album that
        // have not been iterated over.
        @Override
        public boolean hasNext() {
            if (current < Album.this.songs.size()) {
                return true;
            } else {
                return false;
            }
        }

        // return the next element of the iteration and move the current
        // index to the element after that.
        @Override
        public Song next() {


            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            //return songs[current++];

            return songs.get(current++);
        }

    }

    public Iterator<Song> iterator() {
        return new MyIterator();
    }

    public String getName(){
        return name;
    }

    // Return the value at a given index
    public Song get(int index) { return (Song)songs.get(index); }

    // Set the value at a given index
    public void set(int index, Song value) { songs.set(index, value); }

    // Return the length of the array
    public int length() { return songs.size(); }

    // Parcel uses this
    @Override
    public int describeContents() {
        return 0;
    }

    // write object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        //Bundle b = new Bundle();
        //b.putParcelableArrayList("songs", songs);
        //out.writeBundle(b);
        Bundle b = new Bundle();
        b.putParcelableArrayList("songs", songs);
        out.writeBundle(b);
        out.writeString(name);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private Album(Parcel in) {
        Bundle b = in.readBundle(Song.class.getClassLoader());

        songs = b.getParcelableArrayList("songs");
        name = in.readString();
    }
}


