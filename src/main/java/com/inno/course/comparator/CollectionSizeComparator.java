package com.inno.course.comparator;

import com.inno.course.entity.AbstractNumericArray;

import java.util.Comparator;

/**
 * Comparator for sorting collections by their size (number of elements).
 */
public class CollectionSizeComparator implements Comparator<AbstractNumericArray<?>> {

    /**
     * Compares two collections by their sizes.
     *
     * @param first the first collection
     * @param second the second collection
     * @return negative if first size < second size, zero if equal, positive otherwise
     */
    @Override
    public int compare(AbstractNumericArray<?> first, AbstractNumericArray<?> second) {
        return Integer.compare(first.size(), second.size());
    }
}