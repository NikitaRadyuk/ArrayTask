package com.inno.course.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NumericCollection class.
 */
class NumericCollectionTest {

    @Test
    void testCreation() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{1, 2, 3}
        );

        assertNotNull(collection.getId());
        assertEquals(3, collection.size());
        assertFalse(collection.isEmpty());
    }

    @Test
    void testSetElement() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{1, 2, 3}
        );

        collection.setElement(1, 100);
        assertEquals(100, collection.getElement(1));
    }

    @Test
    void testCopy() {
        NumericArray<Integer> original = new NumericArray<>(
                new Integer[]{1, 2, 3}
        );

        AbstractNumericArray<Integer> copy = original.copy();
        assertNotSame(original, copy);
        assertEquals(original.size(), copy.size());
        assertArrayEquals(original.getElements(), copy.getElements());
    }

    @Test
    void testGetAsDouble() {
        NumericArray<Integer> collection = new NumericArray<>(
                new Integer[]{1, 2, 3}
        );

        assertEquals(1.0, collection.getAsDouble(0));
        assertEquals(2.0, collection.getAsDouble(1));
        assertEquals(3.0, collection.getAsDouble(2));
    }
}