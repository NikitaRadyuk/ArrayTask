package com.inno.course.service;

import com.inno.course.entity.NumericArray;
import com.inno.course.service.impl.StatisticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.OptionalDouble;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StatisticsService implementation.
 */
class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsServiceImpl();
    }

    @Test
    void testFindMinimum() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{10, 5, 8, 3, 12}
        );

        Optional<Integer> min = statisticsService.findMinimum(collection);
        assertTrue(min.isPresent());
        assertEquals(3, min.get());
    }

    @Test
    void testFindMaximum() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{10, 5, 8, 3, 12}
        );

        Optional<Integer> max = statisticsService.findMaximum(collection);
        assertTrue(max.isPresent());
        assertEquals(12, max.get());
    }

    @Test
    void testCalculateSum() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{10, 20, 30, 40, 50}
        );

        OptionalDouble sum = statisticsService.calculateSum(collection);
        assertTrue(sum.isPresent());
        assertEquals(150.0, sum.getAsDouble());
    }

    @Test
    void testCalculateAverage() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{10, 20, 30, 40, 50}
        );

        OptionalDouble average = statisticsService.calculateAverage(collection);
        assertTrue(average.isPresent());
        assertEquals(30.0, average.getAsDouble());
    }

    @Test
    void testEmptyCollection() {
        NumericArray<Integer> collection = new NumericArray<>(new Integer[0]);

        Optional<Integer> min = statisticsService.findMinimum(collection);
        assertTrue(min.isEmpty());

        OptionalDouble sum = statisticsService.calculateSum(collection);
        assertTrue(sum.isEmpty());
    }
}