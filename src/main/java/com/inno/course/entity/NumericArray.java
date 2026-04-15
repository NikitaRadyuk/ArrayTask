package com.inno.course.entity;

import java.util.Arrays;

/**
 * Concrete implementation of AbstractNumericCollection.
 * Provides a generic collection for storing and manipulating numeric values.
 *
 * @param <T> the type of numbers in the collection (must extend Number)
 */
public class NumericArray<T extends Number> extends AbstractNumericArray<T> {

    /**
     * Constructs a new numeric collection with the specified elements.
     *
     * @param elements the array of elements to store
     */
    public NumericArray(T[] elements) {
        super(elements);
    }

    /**
     * Returns the element at the specified index as a double value.
     *
     * @param index the index of the element
     * @return the element value as double
     * @throws IndexOutOfBoundsException if index is out of range
     */
    @Override
    public double getAsDouble(int index) {
        validateIndex(index);
        return elements[index].doubleValue();
    }

    /**
     * Creates and returns a copy of this collection.
     *
     * @return a new NumericCollection instance with copied elements
     */
    @Override
    public AbstractNumericArray<T> copy() {
        T[] copyElements = Arrays.copyOf(elements, elements.length);
        return new NumericArray<>(copyElements);
    }

    /**
     * Validates that the index is within the bounds of the array.
     *
     * @param index the index to validate
     * @throws IndexOutOfBoundsException if index is out of range
     */
    private void validateIndex(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + elements.length);
        }
    }
}