package com.inno.course.observer;

/**
 * Interface for objects that can be observed.
 * Provides methods for managing observers and notifying them about changes.
 */
public interface Observable {

    /**
     * Registers an observer to receive notifications.
     *
     * @param observer the observer to register
     */
    void registerObserver(Observer observer);

    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to remove
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers about a change.
     */
    void notifyObservers();
}