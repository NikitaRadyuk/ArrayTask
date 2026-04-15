package com.inno.course.service;

import com.inno.course.entity.NumericArray;
import com.inno.course.service.impl.SortingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SortingService implementation.
 */
class SortingServiceTest {

    private SortingService sortingService;

    @BeforeEach
    void setUp() {
        sortingService = new SortingServiceImpl();
    }

    @Test
    void testBubbleSort() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{5, 3, 8, 1, 9, 2, 7, 4, 6}
        );

        sortingService.bubbleSort(collection);

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected, collection.getElements());
    }

    @Test
    void testQuickSort() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{5, 3, 8, 1, 9, 2, 7, 4, 6}
        );

        sortingService.quickSort(collection);

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(expected, collection.getElements());
    }

    @Test
    void testAlreadySorted() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{1, 2, 3, 4, 5}
        );

        sortingService.bubbleSort(collection);

        Integer[] expected = {1, 2, 3, 4, 5};
        assertArrayEquals(expected, collection.getElements());
    }
}