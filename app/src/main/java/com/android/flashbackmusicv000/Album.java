package com.android.flashbackmusicv000; /**
 * Created by janic on 2/10/18.
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

/* com.android.flashbackmusicv000.Album is a class to store each of our songs by their corresponding album.
 * It is iterable so it can hold an array of songs.
 */
public class Album<T> implements Iterable<T> {
    T[] songs;  // this contains the actual elements of the album
    String name;

    // Constructor that takes a "raw" array and stores it
    public Album(String name, T[] songs) {
        this.name = name;
        this.songs = songs;
    }

    class MyIterator implements Iterator<T> {

        int current = 0;  // the current element we are looking at

        // return whether or not there are more songs in the album that
        // have not been iterated over.
        public boolean hasNext() {
            if (current < Album.this.songs.length) {
                return true;
            } else {
                return false;
            }
        }

        // return the next element of the iteration and move the current
        // index to the element after that.
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return songs[current++];
        }

    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public String getName(){
        return name;
    }

    // Return the value at a given index
    public T get(int index) { return songs[index]; }

    // Set the value at a given index
    public void set(int index, T value) { songs[index] = value; }

    // Return the length of the array
    public int length() { return songs.length; }
}


