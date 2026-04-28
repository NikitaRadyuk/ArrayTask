package com.inno.course.warehouse;

import com.inno.course.entity.AbstractNumericArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton warehouse that stores statistical information for all collections.
 *
 * @version 1.0
 * @see CollectionStatistics
 * @see AbstractNumericArray
 */
public class Warehouse {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(Warehouse.class.getName());

    /** Singleton instance of Warehouse */
    private static Warehouse instance;

    /** Map storing statistics for each collection by its ID */
    private final Map<String, CollectionStatistics> statisticsMap;

    /**
     * Private constructor for Singleton pattern.
     * Initializes the statistics storage map.
     */
    private Warehouse() {
        this.statisticsMap = new ConcurrentHashMap<>();
        logger.info("Warehouse instance created");
    }

    /**
     * Returns the singleton instance of Warehouse.
     *
     * @return the singleton Warehouse instance
     */
    public static synchronized Warehouse getInstance() {
        if (instance == null) {
            instance = new Warehouse();
            logger.debug("Created new Warehouse instance");
        }
        return instance;
    }

    /**
     * Registers a collection in the warehouse and calculates its statistics.
     *
     * @param collection the collection to register (must not be null)
     */
    public void registerCollection(AbstractNumericArray<?> collection) {
        logger.debug("Registering collection: {}", collection.getId());
        updateStatistics(collection);
        logger.info("Collection registered in warehouse: {}", collection.getId());
    }

    /**
     * Removes a collection from the warehouse.
     *
     * @param collection the collection to unregister (must not be null)
     */
    public void unregisterCollection(AbstractNumericArray<?> collection) {
        String collectionId = collection.getId();
        logger.debug("Unregistering collection: {}", collectionId);

        CollectionStatistics removed = statisticsMap.remove(collectionId);
        if (removed != null) {
            logger.info("Collection unregistered from warehouse: {}", collectionId);
        } else {
            logger.warn("Collection not found in warehouse during unregister: {}", collectionId);
        }
    }

    /**
     * Updates statistics for a collection in the warehouse.
     *
     * @param collection the collection to update (must not be null)
     */
    public void updateCollection(AbstractNumericArray<?> collection) {
        logger.debug("Updating statistics for collection: {}", collection.getId());
        updateStatistics(collection);
        logger.debug("Collection statistics updated: {}", collection.getId());
    }

    /**
     * Called when a collection changes (Observer pattern).
     *
     * @param collection the collection that changed (must not be null)
     */
    public void onCollectionChanged(AbstractNumericArray<?> collection) {
        logger.trace("Collection change detected: {}", collection.getId());
        updateStatistics(collection);
        logger.debug("Warehouse notified of collection change: {}", collection.getId());
    }

    /**
     * Calculates and stores statistics for a collection.
     *
     * @param collection the collection to process
     */
    private void updateStatistics(AbstractNumericArray<?> collection) {
        String collectionId = collection.getId();
        logger.trace("Calculating statistics for collection: {}", collectionId);

        double sum = calculateSum(collection);
        double min = calculateMin(collection);
        double max = calculateMax(collection);
        double average = collection.isEmpty() ? 0.0 : sum / collection.size();

        CollectionStatistics stats = new CollectionStatistics(sum, average, min, max, collection.size());
        statisticsMap.put(collectionId, stats);

        logger.trace("Statistics calculated for {}: sum={}, avg={}, min={}, max={}, size={}",
                collectionId, sum, average, min, max, collection.size());
    }

    /**
     * Calculates the sum of all elements in a collection.
     *
     * @param collection the collection to process
     * @return the sum of all elements
     */
    private double calculateSum(AbstractNumericArray<?> collection) {
        double sum = 0.0;
        for (int i = 0; i < collection.size(); i++) {
            sum += collection.getAsDouble(i);
        }
        return sum;
    }

    /**
     * Finds the minimum value in a collection.
     *
     * @param collection the collection to process
     * @return the minimum value, or NaN if collection is empty
     */
    private double calculateMin(AbstractNumericArray<?> collection) {
        if (collection.isEmpty()) {
            logger.trace("Collection is empty, returning NaN for min");
            return Double.NaN;
        }

        double min = collection.getAsDouble(0);
        for (int i = 1; i < collection.size(); i++) {
            double value = collection.getAsDouble(i);
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * Finds the maximum value in a collection.
     *
     * @param collection the collection to process
     * @return the maximum value, or NaN if collection is empty
     */
    private double calculateMax(AbstractNumericArray<?> collection) {
        if (collection.isEmpty()) {
            logger.trace("Collection is empty, returning NaN for max");
            return Double.NaN;
        }

        double max = collection.getAsDouble(0);
        for (int i = 1; i < collection.size(); i++) {
            double value = collection.getAsDouble(i);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * Returns the sum for a collection by its ID.
     *
     * @param collectionId the collection ID
     * @return the sum, or NaN if collection not found
     */
    public double getSum(String collectionId) {
        CollectionStatistics stats = statisticsMap.get(collectionId);
        double result = stats != null ? stats.getSum() : Double.NaN;
        logger.trace("Retrieved sum for {}: {}", collectionId, result);
        return result;
    }

    /**
     * Returns the average for a collection by its ID.
     *
     * @param collectionId the collection ID
     * @return the average, or NaN if collection not found
     */
    public double getAverage(String collectionId) {
        CollectionStatistics stats = statisticsMap.get(collectionId);
        double result = stats != null ? stats.getAverage() : Double.NaN;
        logger.trace("Retrieved average for {}: {}", collectionId, result);
        return result;
    }

    /**
     * Returns the minimum value for a collection by its ID.
     *
     * @param collectionId the collection ID
     * @return the minimum value, or NaN if collection not found
     */
    public double getMin(String collectionId) {
        CollectionStatistics stats = statisticsMap.get(collectionId);
        double result = stats != null ? stats.getMin() : Double.NaN;
        logger.trace("Retrieved min for {}: {}", collectionId, result);
        return result;
    }

    /**
     * Returns the maximum value for a collection by its ID.
     *
     * @param collectionId the collection ID
     * @return the maximum value, or NaN if collection not found
     */
    public double getMax(String collectionId) {
        CollectionStatistics stats = statisticsMap.get(collectionId);
        double result = stats != null ? stats.getMax() : Double.NaN;
        logger.trace("Retrieved max for {}: {}", collectionId, result);
        return result;
    }

    /**
     * Returns the size for a collection by its ID.
     *
     * @param collectionId the collection ID
     * @return the size, or 0 if collection not found
     */
    public int getSize(String collectionId) {
        CollectionStatistics stats = statisticsMap.get(collectionId);
        int result = stats != null ? stats.getSize() : 0;
        logger.trace("Retrieved size for {}: {}", collectionId, result);
        return result;
    }

    /**
     * Returns all statistics for a collection.
     *
     * @param collectionId the collection ID
     * @return the CollectionStatistics object, or null if not found
     */
    public CollectionStatistics getStatistics(String collectionId) {
        logger.debug("Retrieving all statistics for: {}", collectionId);
        return statisticsMap.get(collectionId);
    }

    /**
     * Clears all statistics from the warehouse.
     */
    public void clear() {
        int clearedCount = statisticsMap.size();
        statisticsMap.clear();
        logger.info("Warehouse cleared: {} statistics entries removed", clearedCount);
    }

    /**
     * Logs all stored statistics for debugging purposes.
     */
    public void displayAllStatistics() {
        if (statisticsMap.isEmpty()) {
            logger.info("No statistics available in warehouse");
            return;
        }

        logger.info("=== Warehouse Statistics ===");
        for (Map.Entry<String, CollectionStatistics> entry : statisticsMap.entrySet()) {
            String shortId = entry.getKey().substring(0, Math.min(8, entry.getKey().length()));
            logger.info("Collection {}: {}", shortId, entry.getValue());
        }
    }
}