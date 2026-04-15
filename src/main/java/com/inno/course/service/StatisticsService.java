package com.inno.course.service;

import com.inno.course.entity.AbstractNumericArray;
import java.util.Optional;
import java.util.OptionalDouble;

/**
 * Service interface for calculating statistics on numeric collections.
 * Uses Optional to handle empty collections gracefully.
 */
public interface StatisticsService {

    /**
     * Finds the minimum value in a collection.
     *
     * @param collection the collection to search
     * @param <T> the numeric type of the collection
     * @return Optional containing the minimum value, or empty if collection is null/empty
     */
    <T extends Number & Comparable<T>> Optional<T> findMinimum(AbstractNumericArray<T> collection);

    /**
     * Finds the maximum value in a collection.
     *
     * @param collection the collection to search
     * @param <T> the numeric type of the collection
     * @return Optional containing the maximum value, or empty if collection is null/empty
     */
    <T extends Number & Comparable<T>> Optional<T> findMaximum(AbstractNumericArray<T> collection);

    /**
     * Calculates the sum of all elements in a collection.
     *
     * @param collection the collection to process
     * @param <T> the numeric type of the collection
     * @return OptionalDouble containing the sum, or empty if collection is null/empty
     */
    <T extends Number> OptionalDouble calculateSum(AbstractNumericArray<T> collection);

    /**
     * Calculates the average of all elements in a collection.
     *
     * @param collection the collection to process
     * @param <T> the numeric type of the collection
     * @return OptionalDouble containing the average, or empty if collection is null/empty
     */
    <T extends Number> OptionalDouble calculateAverage(AbstractNumericArray<T> collection);
}