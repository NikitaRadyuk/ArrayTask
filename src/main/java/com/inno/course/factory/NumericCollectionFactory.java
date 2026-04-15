package com.inno.course.factory;

import com.inno.course.entity.NumericArray;
import com.inno.course.exception.NumericCollectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;

/**
 * Factory class for creating NumericArray instances from string arrays.
 * Implements the Factory Method pattern for creating numeric collections.
 *
 * @author Your Name
 * @version 1.0
 * @see NumericArray
 * @see NumericCollectionException
 */
public class NumericCollectionFactory {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(NumericCollectionFactory.class.getName());

    /**
     * Creates a NumericArray from an array of string values.
     * Attempts to parse strings as the specified numeric type.
     *
     * @param stringValues the string values to parse (cannot be null or empty)
     * @param type the target numeric type class (Integer.class, Double.class, etc.)
     * @param <T> the numeric type parameter
     * @return a new NumericArray instance containing parsed numbers
     * @throws NumericCollectionException if stringValues is null/empty, parsing fails,
     *         or the number type is unsupported
     */
    public <T extends Number> NumericArray<T> createCollection(
            String[] stringValues,
            Class<T> type) throws NumericCollectionException {

        if (stringValues == null || stringValues.length == 0) {
            logger.error("Cannot create collection: input array is null or empty");
            throw new NumericCollectionException("Cannot create collection from empty or null array");
        }

        logger.debug("Creating collection with {} values of type {}", stringValues.length, type.getSimpleName());

        T[] numbers = (T[]) Array.newInstance(type, stringValues.length);

        for (int i = 0; i < stringValues.length; i++) {
            try {
                String trimmed = stringValues[i].trim();

                if (type == Integer.class) {
                    numbers[i] = type.cast(Integer.parseInt(trimmed));
                } else if (type == Double.class) {
                    numbers[i] = type.cast(Double.parseDouble(trimmed));
                } else if (type == Float.class) {
                    numbers[i] = type.cast(Float.parseFloat(trimmed));
                } else if (type == Long.class) {
                    numbers[i] = type.cast(Long.parseLong(trimmed));
                } else {
                    logger.error("Unsupported number type: {}", type.getName());
                    throw new NumericCollectionException("Unsupported number type: " + type.getName());
                }

                logger.trace("Parsed value at index {}: {}", i, numbers[i]);

            } catch (NumberFormatException e) {
                logger.error("Failed to parse value at index {}: '{}'", i, stringValues[i]);
                throw new NumericCollectionException("Invalid number format: " + stringValues[i], e);
            }
        }

        logger.info("Successfully created NumericArray<{}> with {} elements",
                type.getSimpleName(), numbers.length);

        return new NumericArray<>(numbers);
    }
}