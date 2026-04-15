package com.inno.course.repository.specifications;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.warehouse.Warehouse;
import java.util.function.DoublePredicate;

/**
 * Specification for filtering collections based on their minimum value.
 *
 * @author Your Name
 * @version 1.0
 * @see Specification
 * @see Warehouse
 * @see AbstractNumericArray
 * @see MaxSpecification
 * @see SumSpecification
 * @see AverageSpecification
 * @see SizeSpecification
 */
public class MinSpecification implements Specification {

    /** Predicate that tests whether a minimum value satisfies the condition */
    private final DoublePredicate predicate;

    /**
     * Constructs a new MinSpecification with the given predicate.
     *
     * @param predicate the predicate to test the minimum value against
     * @throws IllegalArgumentException if predicate is null
     */
    public MinSpecification(DoublePredicate predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }
        this.predicate = predicate;
    }

    /**
     * Tests whether a collection satisfies this specification.
     *
     * @param collection the collection to test (must not be null)
     * @return true if the collection's minimum value satisfies the predicate,
     *         false otherwise
     * @throws NullPointerException if collection is null
     */
    @Override
    public boolean isSatisfiedBy(AbstractNumericArray<?> collection) {
        Warehouse warehouse = Warehouse.getInstance();
        double min = warehouse.getMin(collection.getId());
        return predicate.test(min);
    }

    /**
     * Creates a specification for collections with minimum value greater than
     * the given threshold (exclusive).
     *
     * @param value the threshold value (exclusive)
     * @return a MinSpecification instance
     */
    public static MinSpecification greaterThan(double value) {
        return new MinSpecification(min -> min > value);
    }

    /**
     * Creates a specification for collections with minimum value greater than
     * or equal to the given threshold (inclusive).
     *
     * @param value the threshold value (inclusive)
     * @return a MinSpecification instance
     */
    public static MinSpecification greaterThanOrEqual(double value) {
        return new MinSpecification(min -> min >= value);
    }

    /**
     * Creates a specification for collections with minimum value less than
     * the given threshold (exclusive).
     *
     * @param value the threshold value (exclusive)
     * @return a MinSpecification instance
     */
    public static MinSpecification lessThan(double value) {
        return new MinSpecification(min -> min < value);
    }

    /**
     * Creates a specification for collections with minimum value less than
     * or equal to the given threshold (inclusive).
     *
     * @param value the threshold value (inclusive)
     * @return a MinSpecification instance
     */
    public static MinSpecification lessThanOrEqual(double value) {
        return new MinSpecification(min -> min <= value);
    }

    /**
     * Creates a specification for collections with minimum value equal to
     * the given value (within a small epsilon for floating-point precision).
     *
     * @param value the value to compare against
     * @return a MinSpecification instance
     */
    public static MinSpecification equalTo(double value) {
        return new MinSpecification(min -> Math.abs(min - value) < 0.0001);
    }

    /**
     * Creates a specification for collections with minimum value not equal to
     * the given value.
     *
     * @param value the value to exclude
     * @return a MinSpecification instance
     */
    public static MinSpecification notEqualTo(double value) {
        return new MinSpecification(min -> Math.abs(min - value) >= 0.0001);
    }

    /**
     * Creates a specification for collections with minimum value between
     * the given minimum and maximum values (inclusive).
     *
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return a MinSpecification instance
     * @throws IllegalArgumentException if min > max
     */
    public static MinSpecification between(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Min value cannot be greater than max value");
        }
        return new MinSpecification(minValue -> minValue >= min && minValue <= max);
    }

    /**
     * Creates a specification for collections with minimum value outside
     * the given range (exclusive).
     *
     * @param min the minimum value (exclusive)
     * @param max the maximum value (exclusive)
     * @return a MinSpecification instance
     */
    public static MinSpecification outside(double min, double max) {
        return new MinSpecification(minValue -> minValue < min || minValue > max);
    }
}