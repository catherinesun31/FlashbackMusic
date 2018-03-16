package com.android.flashbackmusicv000;

import java.util.ArrayList;

public class DaySubject implements SongSubject {
    private ArrayList<UserObserver> observers;

    public DaySubject() {
        observers = new ArrayList<>();
    }

    public void registerObserver(UserObserver o) {
        observers.add(o);
    }

    public void removeObserver(UserObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (int i = 0; i < observers.size(); ++i) {
            UserObserver observer = (UserObserver)observers.get(i);
            observer.update();
        }
    }

    public void dayChanged() {
        notifyObservers();
    }
}
