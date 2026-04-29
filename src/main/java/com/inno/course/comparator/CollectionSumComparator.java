package com.inno.course.comparator;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.warehouse.Warehouse;
import java.util.Comparator;

/**
 * Comparator for sorting collections by their sum (using Warehouse statistics).
 */
public class CollectionSumComparator implements Comparator<AbstractNumericArray<?>> {

    private final Warehouse warehouse;

    /**
     * Constructs a new CollectionSumComparator.
     * Gets the Warehouse singleton instance.
     */
    public CollectionSumComparator() {
        this.warehouse = Warehouse.getInstance();
    }

    /**
     * Compares two collections by their sum.
     *
     * @param first the first collection
     * @param second the second collection
     * @return negative if first sum less than second sum, zero if equal, positive otherwise
     */
    @Override
    public int compare(AbstractNumericArray<?> first, AbstractNumericArray<?> second) {
        double firstSum = warehouse.getSum(first.getId());
        double secondSum = warehouse.getSum(second.getId());
        return Double.compare(firstSum, secondSum);
    }
}