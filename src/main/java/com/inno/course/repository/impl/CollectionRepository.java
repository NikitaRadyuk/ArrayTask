package com.inno.course.repository.impl;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.observer.Observer;
import com.inno.course.repository.Repository;
import com.inno.course.repository.specification.Specification;
import com.inno.course.warehouse.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Singleton implementation of the Repository interface for managing numeric collections.
 *
 * @version 1.0
 * @see Repository
 * @see AbstractNumericArray
 * @see Warehouse
 * @see Specification
 */
public class CollectionRepository implements Repository {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(CollectionRepository.class.getName());

    /** Singleton instance of the repository */
    private static CollectionRepository instance;

    /** Main storage map: collection ID → collection object */
    private final Map<Long, AbstractNumericArray<?>> storage;

    /** Map for storing observers associated with each collection */
    private final Map<Long, Observer> observerMap;

    /** Warehouse instance for statistics management */
    private final Warehouse warehouse;

    /**
     * Private constructor for Singleton pattern.
     * Initializes storage maps and gets the Warehouse instance.
     */
    private CollectionRepository() {
        this.storage = new ConcurrentHashMap<>();
        this.observerMap = new ConcurrentHashMap<>();
        this.warehouse = Warehouse.getInstance();
        logger.info("CollectionRepository initialized");
    }

    /**
     * Returns the singleton instance of CollectionRepository.
     *
     * @return the singleton repository instance
     */
    public static synchronized CollectionRepository getInstance() {
        if (instance == null) {
            instance = new CollectionRepository();
            logger.debug("Created new CollectionRepository instance");
        }
        return instance;
    }

    /**
     * Adds a collection to the repository.
     *
     * @param collection the collection to add (cannot be null)
     */
    @Override
    public void add(AbstractNumericArray<?> collection) {
        if (collection == null) {
            logger.warn("Attempted to add null collection");
            return;
        }

        Long collectionId = collection.getId();
        logger.debug("Adding collection with id: {}", collectionId);

        storage.put(collectionId, collection);

        // Create and store observer for this collection
        Observer collectionObserver = new Observer() {
            @Override
            public void update(AbstractNumericArray<?> changedCollection) {
                logger.trace("Observer triggered for collection: {}", changedCollection.getId());
                warehouse.onCollectionChanged(changedCollection);
            }
        };

        observerMap.put(collectionId, collectionObserver);
        collection.registerObserver(collectionObserver);
        warehouse.registerCollection(collection);

        logger.info("Added collection: id={}, size={}", collectionId, collection.size());
    }

    /**
     * Removes a collection from the repository by its ID.
     *
     * @param id the ID of the collection to remove
     */
    public void removeById(Long id) {
        logger.debug("Attempting to remove collection with id: {}", id);

        AbstractNumericArray<?> removed = storage.remove(id);
        if (removed != null) {
            Observer collectionObserver = observerMap.remove(id);
            if (collectionObserver != null) {
                removed.removeObserver(collectionObserver);
                logger.trace("Removed observer for collection: {}", id);
            }
            warehouse.unregisterCollection(removed);
            logger.info("Removed collection with id: {}", id);
        } else {
            logger.warn("Collection with id {} not found", id);
        }
    }

    /**
     * Removes a collection from the repository.
     *
     * @param collection the collection to remove
     */
    @Override
    public void remove(AbstractNumericArray<?> collection) {
        if (collection != null) {
            removeById(collection.getId());
        } else {
            logger.warn("Attempted to remove null collection");
        }
    }

    /**
     * Finds a collection by its ID.
     *
     * @param id the ID to search for
     * @return the collection if found, null otherwise
     */
    @Override
    public AbstractNumericArray<?> findById(Long id) {
        logger.debug("Finding collection by id: {}", id);
        return storage.get(id);
    }

    /**
     * Returns all collections in the repository.
     *
     * @return a new ArrayList containing all collections (never null)
     */
    @Override
    public List<AbstractNumericArray<?>> findAll() {
        logger.debug("Retrieving all collections, count: {}", storage.size());
        return new ArrayList<>(storage.values());
    }

    /**
     * Queries the repository using a specification.
     *
     * @param specification the specification to filter collections
     * @return a list of collections matching the specification
     */
    @Override
    public List<AbstractNumericArray<?>> query(Specification specification) {
        logger.debug("Executing query with specification: {}", specification.getClass().getSimpleName());

        List<AbstractNumericArray<?>> result = storage.values().stream()
                .filter(specification::isSatisfiedBy)
                .collect(Collectors.toList());

        logger.debug("Query returned {} results", result.size());
        return result;
    }

    /**
     * Updates an existing collection in the repository.
     *
     * @param collection the collection to update
     */
    @Override
    public void update(AbstractNumericArray<?> collection) {
        if (collection == null) {
            logger.warn("Cannot update null collection");
            return;
        }

        Long collectionId = collection.getId();

        if (storage.containsKey(collectionId)) {
            storage.put(collectionId, collection);
            warehouse.updateCollection(collection);
            logger.info("Updated collection: id={}", collectionId);
        } else {
            logger.warn("Cannot update non-existent collection with id: {}", collectionId);
        }
    }

    /**
     * Returns the number of collections in the repository.
     *
     * @return the current size of the repository
     */
    @Override
    public int size() {
        return storage.size();
    }

    /**
     * Removes all collections from the repository.
     *
     */
    @Override
    public void clear() {
        logger.info("Clearing repository, current size: {}", storage.size());

        // Remove all observers before clearing
        for (Map.Entry<Long, AbstractNumericArray<?>> entry : storage.entrySet()) {
            Long id = entry.getKey();
            AbstractNumericArray<?> collection = entry.getValue();
            Observer collectionObserver = observerMap.get(id);

            if (collectionObserver != null) {
                collection.removeObserver(collectionObserver);
                logger.trace("Removed observer for collection: {}", id);
            }
        }

        storage.clear();
        observerMap.clear();
        warehouse.clear();

        logger.info("Repository cleared");
    }
}