package com.inno.course.repository.specification.impl;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.repository.specification.Specification;
import com.inno.course.warehouse.Warehouse;
import java.util.function.DoublePredicate;

/**
 * Specification for filtering collections based on their maximum value.
 *
 * @version 1.0
 * @see Specification
 * @see Warehouse
 * @see AbstractNumericArray
 */
public class MaxSpecification implements Specification {

    /** Predicate that tests whether a max value satisfies the condition */
    private final DoublePredicate predicate;

    /**
     * Constructs a new MaxSpecification with the given predicate.
     *
     * @param predicate the predicate to test the maximum value against
     */
    public MaxSpecification(DoublePredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Tests whether a collection satisfies this specification.
     *
     * @param collection the collection to test (must not be null)
     * @return true if the collection's maximum value satisfies the predicate,
     *         false otherwise
     */
    @Override
    public boolean isSatisfiedBy(AbstractNumericArray<?> collection) {
        Warehouse warehouse = Warehouse.getInstance();
        double max = warehouse.getMax(collection.getId());
        return predicate.test(max);
    }

    /**
     * Creates a specification for collections with maximum value greater than
     * the given threshold.
     *
     * @param value the threshold value (exclusive)
     * @return a MaxSpecification instance
     */
    public static MaxSpecification greaterThan(double value) {
        return new MaxSpecification(max -> max > value);
    }

    /**
     * Creates a specification for collections with maximum value less than
     * the given threshold.
     *
     * @param value the threshold value (exclusive)
     * @return a MaxSpecification instance
     */
    public static MaxSpecification lessThan(double value) {
        return new MaxSpecification(max -> max < value);
    }

    /**
     * Creates a specification for collections with maximum value equal to
     * the given value (within a small epsilon for floating-point precision).
     *
     * @param value the value to compare against
     * @return a MaxSpecification instance
     */
    public static MaxSpecification equalTo(double value) {
        return new MaxSpecification(max -> Math.abs(max - value) < 0.0001);
    }

    /**
     * Creates a specification for collections with maximum value between
     * the given minimum and maximum values (inclusive).
     *
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return a MaxSpecification instance
     */
    public static MaxSpecification between(double min, double max) {
        return new MaxSpecification(maxValue -> maxValue >= min && maxValue <= max);
    }
}