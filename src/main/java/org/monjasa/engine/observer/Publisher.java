package org.monjasa.engine.observer;

public interface Publisher {

    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}
