package com.inno.course.service;

import com.inno.course.entity.AbstractNumericArray;

/**
 * Service interface for sorting numeric collections.
 * Provides two different sorting algorithm implementations.
 */
public interface SortingService {

    /**
     * Sorts a collection using the bubble sort algorithm.
     * Time complexity: O(n?) in worst case.
     *
     * @param collection the collection to sort (will be modified)
     * @param <T> the numeric type of the collection
     */
    <T extends Number & Comparable<T>> void bubbleSort(AbstractNumericArray<T> collection);

    /**
     * Sorts a collection using the quick sort algorithm.
     * Time complexity: O(n log n) average case, O(n?) worst case.
     *
     * @param collection the collection to sort (will be modified)
     * @param <T> the numeric type of the collection
     */
    <T extends Number & Comparable<T>> void quickSort(AbstractNumericArray<T> collection);
}