package com.inno.course.service.impl;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.service.SortingService;

import java.lang.reflect.Field;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementation of SortingService.
 * Provides bubble sort and quick sort algorithms.
 */
public class SortingServiceImpl implements SortingService {

    private static final Logger logger = Logger.getLogger(SortingServiceImpl.class.getName());

    /**
     * Sorts a collection using bubble sort algorithm.
     * Repeatedly steps through the list, compares adjacent elements and swaps them if they're in wrong order.
     *
     * @param collection the collection to sort
     * @param <T> the numeric type of the collection
     */
    @Override
    public <T extends Number & Comparable<T>> void bubbleSort(AbstractNumericArray<T> collection) {
        if (collection == null || collection.size() <= 1) {
            logger.log(Level.WARNING, "Cannot sort null or collection with size <= 1");
            return;
        }

        T[] elements = collection.getElements();
        int size = elements.length;

        for (int i = 0; i < size - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < size - i - 1; j++) {
                if (elements[j].compareTo(elements[j + 1]) > 0) {
                    T temp = elements[j];
                    elements[j] = elements[j + 1];
                    elements[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        updateCollection(collection, elements);
        logger.log(Level.INFO, "Bubble sort completed for collection of size {0}", size);
    }

    /**
     * Sorts a collection using quick sort algorithm (divide and conquer).
     * Picks a pivot element and partitions the array around it.
     *
     * @param collection the collection to sort
     * @param <T> the numeric type of the collection
     */
    @Override
    public <T extends Number & Comparable<T>> void quickSort(AbstractNumericArray<T> collection) {
        if (collection == null || collection.size() <= 1) {
            logger.log(Level.WARNING, "Cannot sort null or collection with size <= 1");
            return;
        }

        T[] elements = collection.getElements();
        performQuickSort(elements, 0, elements.length - 1);
        updateCollection(collection, elements);
        logger.log(Level.INFO, "Quick sort completed for collection of size {0}", elements.length);
    }

    /**
     * Recursive helper method for quick sort.
     *
     * @param array the array to sort
     * @param low the starting index
     * @param high the ending index
     * @param <T> the comparable type
     */
    private <T extends Comparable<T>> void performQuickSort(T[] array, int low, int high) {
        if (low < high) {
            int partitionIndex = partition(array, low, high);
            performQuickSort(array, low, partitionIndex - 1);
            performQuickSort(array, partitionIndex + 1, high);
        }
    }

    /**
     * Partitions the array for quick sort.
     * Places the pivot element in its correct position.
     *
     * @param array the array to partition
     * @param low the starting index
     * @param high the ending index (pivot)
     * @param <T> the comparable type
     * @return the final index of the pivot element
     */
    private <T extends Comparable<T>> int partition(T[] array, int low, int high) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    /**
     * Updates the collection's internal array using reflection.
     * Notifies observers after the update.
     *
     * @param collection the collection to update
     * @param newElements the new array of elements
     * @param <T> the numeric type
     */
    @SuppressWarnings("unchecked")
    private <T extends Number> void updateCollection(AbstractNumericArray<T> collection, T[] newElements) {
        try {
            Field field = AbstractNumericArray.class.getDeclaredField("elements");
            field.setAccessible(true);
            field.set(collection, newElements);
            collection.notifyObservers();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update collection elements", e);
            throw new RuntimeException("Failed to update collection", e);
        }
    }
}