package com.inno.course.warehouse;

/**
 * Immutable class that stores statistical information about a collection.
 * Contains precomputed values for sum, average, minimum, maximum, and size.
 */
public class CollectionStatistics {

    private final double sum;
    private final double average;
    private final double min;
    private final double max;
    private final int size;

    /**
     * Constructs a new CollectionStatistics object with the specified values.
     *
     * @param sum the sum of all elements
     * @param average the average value of elements
     * @param min the minimum value in the collection
     * @param max the maximum value in the collection
     * @param size the number of elements in the collection
     */
    public CollectionStatistics(double sum, double average, double min, double max, int size) {
        this.sum = sum;
        this.average = average;
        this.min = min;
        this.max = max;
        this.size = size;
    }

    /**
     * Returns the sum of all elements.
     *
     * @return the sum
     */
    public double getSum() {
        return sum;
    }

    /**
     * Returns the average value of elements.
     *
     * @return the average
     */
    public double getAverage() {
        return average;
    }

    /**
     * Returns the minimum value in the collection.
     *
     * @return the minimum value
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the maximum value in the collection.
     *
     * @return the maximum value
     */
    public double getMax() {
        return max;
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a string representation of the statistics.
     *
     * @return a formatted string containing all statistics
     */
    @Override
    public String toString() {
        return String.format("Statistics{sum=%.2f, average=%.2f, min=%.2f, max=%.2f, size=%d}",
                sum, average, min, max, size);
    }
}