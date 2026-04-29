package com.inno.course.entity;

import com.inno.course.observer.Observable;
import com.inno.course.observer.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract base class for numeric collections.
 * Provides common functionality for storing and manipulating arrays of numbers.
 * Implements Observable pattern to notify observers about changes.
 *
 * @param <T> the type of numbers in the collection (must extend Number)
 */
public abstract class AbstractNumericArray<T extends Number> implements Observable {

    /** Atomic counter for generating unique IDs across all instances */
    private static final AtomicLong idCounter = new AtomicLong(1);

    /** Unique identifier for the collection */
    protected final Long id;

    /** Array of elements stored in the collection */
    protected T[] elements;

    /** List of registered observers */
    protected List<Observer> observers;

    /**
     * Constructs a new numeric collection with the specified elements.
     *
     * @param elements the array of elements to store
     * @throws IllegalArgumentException if elements array is null
     */
    protected AbstractNumericArray(T[] elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements array cannot be null");
        }
        this.id = idCounter.getAndIncrement();
        this.elements = elements.clone();
        this.observers = new ArrayList<>();
    }

    /**
     * Returns the unique identifier of the collection.
     *
     * @return the collection ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns a copy of the elements array.
     *
     * @return a cloned array of elements
     */
    public T[] getElements() {
        return elements.clone();
    }

    /**
     * Returns the element at the specified position.
     *
     * @param index the index of the element to return
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public T getElement(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + elements.length);
        }
        return elements[index];
    }

    /**
     * Replaces the element at the specified position with the new value.
     * Notifies all registered observers about the change.
     *
     * @param index the index of the element to replace
     * @param value the new value to store
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public void setElement(int index, T value) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + elements.length);
        }
        this.elements[index] = value;
        notifyObservers();
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return the size of the collection
     */
    public int size() {
        return elements.length;
    }

    /**
     * Checks if the collection is empty.
     *
     * @return true if the collection contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return elements.length == 0;
    }

    /**
     * Returns the element at the specified index as a double value.
     *
     * @param index the index of the element
     * @return the element value as double
     * @throws IndexOutOfBoundsException if index is out of range
     */
    public abstract double getAsDouble(int index);

    /**
     * Creates and returns a copy of this collection.
     *
     * @return a copy of the collection
     */
    public abstract AbstractNumericArray<T> copy();

    /**
     * Registers an observer to be notified about changes.
     *
     * @param observer the observer to register
     */
    @Override
    public void registerObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes a previously registered observer.
     *
     * @param observer the observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about a change in the collection.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Resets the ID counter (useful for testing purposes).
     */
    public static void resetIdCounter() {
        idCounter.set(1);
    }

    /**
     * Returns a string representation of the collection.
     *
     * @return a string containing the collection ID, size, and elements
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("NumericCollection{")
                .append("id=").append(id)
                .append(", size=").append(elements.length)
                .append(", elements=[");

        for (int i = 0; i < elements.length; i++) {
            builder.append(elements[i]);
            if (i < elements.length - 1) {
                builder.append(", ");
            }
        }
        builder.append("]}");
        return builder.toString();
    }
}