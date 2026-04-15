package com.inno.course.repository.specifications;

import com.inno.course.entity.AbstractNumericArray;
import java.util.function.IntPredicate;

/**
 * Specification for filtering collections based on their size (number of elements).
 * Allows querying collections by size criteria such as greater than, less than, equal to, or between values.
 */
public class SizeSpecification implements Specification {

    private final IntPredicate predicate;

    /**
     * Constructs a SizeSpecification with the given predicate.
     *
     * @param predicate the predicate to test the collection size against
     */
    public SizeSpecification(IntPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * Tests whether a collection's size satisfies the predicate.
     *
     * @param collection the collection to test
     * @return true if the size satisfies the predicate, false otherwise
     */
    @Override
    public boolean isSatisfiedBy(AbstractNumericArray<?> collection) {
        return predicate.test(collection.size());
    }

    /**
     * Creates a specification for collections with size greater than the given value.
     *
     * @param value the threshold value (exclusive)
     * @return a SizeSpecification instance
     */
    public static SizeSpecification greaterThan(int value) {
        return new SizeSpecification(size -> size > value);
    }

    /**
     * Creates a specification for collections with size less than the given value.
     *
     * @param value the threshold value (exclusive)
     * @return a SizeSpecification instance
     */
    public static SizeSpecification lessThan(int value) {
        return new SizeSpecification(size -> size < value);
    }

    /**
     * Creates a specification for collections with size equal to the given value.
     *
     * @param value the value to compare
     * @return a SizeSpecification instance
     */
    public static SizeSpecification equalTo(int value) {
        return new SizeSpecification(size -> size == value);
    }

    /**
     * Creates a specification for collections with size greater than or equal to the given value.
     *
     * @param value the minimum value (inclusive)
     * @return a SizeSpecification instance
     */
    public static SizeSpecification greaterThanOrEqual(int value) {
        return new SizeSpecification(size -> size >= value);
    }

    /**
     * Creates a specification for collections with size less than or equal to the given value.
     *
     * @param value the maximum value (inclusive)
     * @return a SizeSpecification instance
     */
    public static SizeSpecification lessThanOrEqual(int value) {
        return new SizeSpecification(size -> size <= value);
    }

    /**
     * Creates a specification for collections with size between min and max values (inclusive).
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a SizeSpecification instance
     */
    public static SizeSpecification between(int min, int max) {
        return new SizeSpecification(size -> size >= min && size <= max);
    }

    /**
     * Creates a specification for collections with size not equal to the given value.
     *
     * @param value the value to exclude
     * @return a SizeSpecification instance
     */
    public static SizeSpecification notEqualTo(int value) {
        return new SizeSpecification(size -> size != value);
    }
}