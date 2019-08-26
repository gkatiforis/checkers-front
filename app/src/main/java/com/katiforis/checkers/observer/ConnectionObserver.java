package com.katiforis.checkers.observer;

public interface ConnectionObserver {
    void onConnectionStatusChange(boolean isConnected);
}