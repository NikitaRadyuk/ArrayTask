package com.inno.course.repository.specification.impl;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.repository.specification.Specification;
import com.inno.course.warehouse.Warehouse;
import java.util.function.DoublePredicate;

/**
 * Specification for filtering collections based on their sum.
 */
public class SumSpecification implements Specification {

    private final DoublePredicate predicate;

    /**
     * Constructs a SumSpecification with the given predicate.
     *
     * @param predicate the predicate to test the sum against
     */
    public SumSpecification(DoublePredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Tests whether a collection's sum satisfies the predicate.
     *
     * @param collection the collection to test
     * @return true if the sum satisfies the predicate, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(AbstractNumericArray<?> collection) {
        Warehouse warehouse = Warehouse.getInstance();
        double sum = warehouse.getSum(collection.getId());
        return predicate.test(sum);
    }

    /**
     * Creates a specification for collections with sum greater than the given value.
     *
     * @param value the threshold value
     * @return a SumSpecification instance
     */
    public static SumSpecification greaterThan(double value) {
        return new SumSpecification(sum -> sum > value);
    }

    /**
     * Creates a specification for collections with sum less than the given value.
     *
     * @param value the threshold value
     * @return a SumSpecification instance
     */
    public static SumSpecification lessThan(double value) {
        return new SumSpecification(sum -> sum < value);
    }

    /**
     * Creates a specification for collections with sum equal to the given value.
     *
     * @param value the value to compare
     * @return a SumSpecification instance
     */
    public static SumSpecification equalTo(double value) {
        return new SumSpecification(sum -> Math.abs(sum - value) < 0.0001);
    }

    /**
     * Creates a specification for collections with sum between min and max values.
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a SumSpecification instance
     */
    public static SumSpecification between(double min, double max) {
        return new SumSpecification(sum -> sum >= min && sum <= max);
    }
}