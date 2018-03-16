package com.android.flashbackmusicv000;

/**
 * Created by cailintreseder on 3/15/18.
 */

public interface SongSubject {
    public void registerObserver(UserObserver o);
    public void removeObserver(UserObserver o);
    public void notifyObservers();
}
