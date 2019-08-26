package com.katiforis.checkers.observer;

public interface Observable {
    void registerObserver(ConnectionObserver repositoryObserver);
    void notifyObservers(boolean isConnected);
}