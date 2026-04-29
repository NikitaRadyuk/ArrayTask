package com.inno.course.repository;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.repository.specification.Specification;
import java.util.List;

/**
 * Repository interface for managing numeric collections.
 * Provides CRUD operations and querying capabilities.
 */
public interface Repository {

    /**
     * Adds a collection to the repository.
     *
     * @param collection the collection to add
     */
    void add(AbstractNumericArray<?> collection);

    /**
     * Removes a collection by its ID.
     *
     * @param id the ID of the collection to remove
     */
    void removeById(Long id);

    /**
     * Removes a collection from the repository.
     *
     * @param array the collection to remove
     */
    void remove(AbstractNumericArray<?> array);

    /**
     * Finds a collection by its ID.
     *
     * @param id the ID to search for
     * @return the collection, or null if not found
     */
    AbstractNumericArray<?> findById(Long id);

    /**
     * Returns all collections in the repository.
     *
     * @return a list of all collections
     */
    List<AbstractNumericArray<?>> findAll();

    /**
     * Queries the repository using a specification.
     *
     * @param specification the specification to filter collections
     * @return a list of collections matching the specification
     */
    List<AbstractNumericArray<?>> query(Specification specification);

    /**
     * Updates an existing collection in the repository.
     *
     * @param collection the collection to update
     */
    void update(AbstractNumericArray<?> collection);

    /**
     * Returns the number of collections in the repository.
     *
     * @return the repository size
     */
    int size();

    /**
     * Removes all collections from the repository.
     */
    void clear();
}