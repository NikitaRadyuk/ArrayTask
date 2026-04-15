package com.inno.course.service.impl;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.service.StatisticsService;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementation of StatisticsService.
 * Provides concrete algorithms for calculating collection statistics.
 */
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = Logger.getLogger(StatisticsServiceImpl.class.getName());

    /**
     * Finds the minimum value in a collection using linear search.
     *
     * @param collection the collection to search
     * @param <T> the numeric type of the collection
     * @return Optional containing the minimum value, or empty if collection is null/empty
     */
    @Override
    public <T extends Number & Comparable<T>> Optional<T> findMinimum(AbstractNumericArray<T> collection) {
        if (collection == null || collection.isEmpty()) {
            logger.log(Level.WARNING, "Cannot find minimum in null or empty collection");
            return Optional.empty();
        }

        T[] elements = collection.getElements();
        T minimum = elements[0];

        for (int i = 1; i < elements.length; i++) {
            if (elements[i].compareTo(minimum) < 0) {
                minimum = elements[i];
            }
        }

        logger.log(Level.FINE, "Found minimum value: {0}", minimum);
        return Optional.of(minimum);
    }

    /**
     * Finds the maximum value in a collection using linear search.
     *
     * @param collection the collection to search
     * @param <T> the numeric type of the collection
     * @return Optional containing the maximum value, or empty if collection is null/empty
     */
    @Override
    public <T extends Number & Comparable<T>> Optional<T> findMaximum(AbstractNumericArray<T> collection) {
        if (collection == null || collection.isEmpty()) {
            logger.log(Level.WARNING, "Cannot find maximum in null or empty collection");
            return Optional.empty();
        }

        T[] elements = collection.getElements();
        T maximum = elements[0];

        for (int i = 1; i < elements.length; i++) {
            if (elements[i].compareTo(maximum) > 0) {
                maximum = elements[i];
            }
        }

        logger.log(Level.FINE, "Found maximum value: {0}", maximum);
        return Optional.of(maximum);
    }

    /**
     * Calculates the sum of all elements by iterating through the collection.
     *
     * @param collection the collection to process
     * @param <T> the numeric type of the collection
     * @return OptionalDouble containing the sum, or empty if collection is null/empty
     */
    @Override
    public <T extends Number> OptionalDouble calculateSum(AbstractNumericArray<T> collection) {
        if (collection == null || collection.isEmpty()) {
            logger.log(Level.WARNING, "Cannot calculate sum for null or empty collection");
            return OptionalDouble.empty();
        }

        double sum = 0.0;
        for (int i = 0; i < collection.size(); i++) {
            sum += collection.getAsDouble(i);
        }

        logger.log(Level.FINE, "Calculated sum: {0}", sum);
        return OptionalDouble.of(sum);
    }

    /**
     * Calculates the average by dividing the sum by the number of elements.
     *
     * @param collection the collection to process
     * @param <T> the numeric type of the collection
     * @return OptionalDouble containing the average, or empty if collection is null/empty
     */
    @Override
    public <T extends Number> OptionalDouble calculateAverage(AbstractNumericArray<T> collection) {
        if (collection == null || collection.isEmpty()) {
            logger.log(Level.WARNING, "Cannot calculate average for null or empty collection");
            return OptionalDouble.empty();
        }

        OptionalDouble sum = calculateSum(collection);
        if (sum.isPresent()) {
            double average = sum.getAsDouble() / collection.size();
            logger.log(Level.FINE, "Calculated average: {0}", average);
            return OptionalDouble.of(average);
        }

        return OptionalDouble.empty();
    }
}