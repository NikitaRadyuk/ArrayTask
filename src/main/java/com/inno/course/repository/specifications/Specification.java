package com.inno.course.repository.specifications;

import com.inno.course.entity.AbstractNumericArray;

/**
 * Functional interface for defining query specifications.
 * Allows filtering collections based on custom criteria.
 */
@FunctionalInterface
public interface Specification {

    /**
     * Tests whether a collection satisfies the specification.
     *
     * @param collection the collection to test
     * @return true if the collection satisfies the specification, false otherwise
     */
    boolean isSatisfiedBy(AbstractNumericArray<?> collection);
}