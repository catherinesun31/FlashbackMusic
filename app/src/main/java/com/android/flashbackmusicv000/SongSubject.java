package com.android.flashbackmusicv000;

public interface SongSubject {
    public void registerObserver(UserObserver o);
    public void removeObserver(UserObserver o);
    public void notifyObservers();
}
