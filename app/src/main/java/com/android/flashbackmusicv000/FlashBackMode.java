package com.android.flashbackmusicv000;

import java.util.ArrayList;
import java.util.List;

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

    }
}
