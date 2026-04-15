package com.inno.course.observer;

import com.inno.course.entity.AbstractNumericArray;

/**
 * Interface for objects that observe Observable objects.
 * Receives updates when the observed object changes.
 */
@FunctionalInterface
public interface Observer {

    /**
     * Called when the observed collection changes.
     *
     * @param collection the collection that changed
     */
    void update(AbstractNumericArray<?> collection);
}