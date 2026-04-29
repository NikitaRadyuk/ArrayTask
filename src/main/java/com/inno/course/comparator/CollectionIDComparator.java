package com.inno.course.comparator;

import com.inno.course.entity.AbstractNumericArray;
import java.util.Comparator;

/**
 * Comparator for sorting collections by their ID (lexicographically).
 */
public class CollectionIDComparator implements Comparator<AbstractNumericArray<?>> {

    /**
     * Compares two collections by their IDs.
     *
     * @param first the first collection
     * @param second the second collection
     * @return negative if first ID less than second ID, zero if equal, positive otherwise
     */
    @Override
    public int compare(AbstractNumericArray<?> first, AbstractNumericArray<?> second) {
        return first.getId().compareTo(second.getId());
    }
}