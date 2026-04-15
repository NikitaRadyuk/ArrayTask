package com.inno.course.comparator;

import com.inno.course.entity.AbstractNumericArray;
import java.util.Comparator;

/**
 * Comparator for sorting collections by their first element value.
 * Empty collections are considered smaller than non-empty ones.
 */
public class CollectionFirstElementComparator implements Comparator<AbstractNumericArray<?>> {

    /**
     * Compares two collections by their first element.
     *
     * @param first the first collection
     * @param second the second collection
     * @return negative if first element < second element, zero if equal, positive otherwise
     */
    @Override
    public int compare(AbstractNumericArray<?> first, AbstractNumericArray<?> second) {
        if (first.isEmpty() && second.isEmpty()) return 0;
        if (first.isEmpty()) return -1;
        if (second.isEmpty()) return 1;

        double firstElement = first.getAsDouble(0);
        double secondElement = second.getAsDouble(0);
        return Double.compare(firstElement, secondElement);
    }
}