package com.inno.course.repository.specifications;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.warehouse.Warehouse;
import java.util.function.DoublePredicate;

/**
 * Specification for filtering collections based on their average value.
 */
public class AverageSpecification implements Specification {

    private final DoublePredicate predicate;

    /**
     * Constructs an AverageSpecification with the given predicate.
     *
     * @param predicate the predicate to test the average against
     */
    public AverageSpecification(DoublePredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Tests whether a collection's average satisfies the predicate.
     *
     * @param collection the collection to test
     * @return true if the average satisfies the predicate, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(AbstractNumericArray<?> collection) {
        Warehouse warehouse = Warehouse.getInstance();
        double average = warehouse.getAverage(collection.getId());
        return predicate.test(average);
    }

    /**
     * Creates a specification for collections with average greater than the given value.
     *
     * @param value the threshold value
     * @return an AverageSpecification instance
     */
    public static AverageSpecification greaterThan(double value) {
        return new AverageSpecification(avg -> avg > value);
    }

    /**
     * Creates a specification for collections with average less than the given value.
     *
     * @param value the threshold value
     * @return an AverageSpecification instance
     */
    public static AverageSpecification lessThan(double value) {
        return new AverageSpecification(avg -> avg < value);
    }

    /**
     * Creates a specification for collections with average equal to the given value.
     *
     * @param value the value to compare
     * @return an AverageSpecification instance
     */
    public static AverageSpecification equalTo(double value) {
        return new AverageSpecification(avg -> Math.abs(avg - value) < 0.0001);
    }

    /**
     * Creates a specification for collections with average between min and max values.
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return an AverageSpecification instance
     */
    public static AverageSpecification between(double min, double max) {
        return new AverageSpecification(avg -> avg >= min && avg <= max);
    }
}