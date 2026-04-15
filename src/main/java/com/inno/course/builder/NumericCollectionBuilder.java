package com.inno.course.builder;

import com.inno.course.entity.NumericArray;
import com.inno.course.exception.NumericCollectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for constructing NumericArray instances.
 * Implements the Builder design pattern for step-by-step collection creation.
 *
 * @author Your Name
 * @version 1.0
 * @see NumericArray
 * @see NumericCollectionException
 */
public class NumericCollectionBuilder {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(NumericCollectionBuilder.class.getName());

    /** List of elements to be added to the collection */
    private final List<Number> elements;

    /** The target numeric type (Integer, Double, etc.) */
    private Class<? extends Number> type;

    /**
     * Constructs a new builder with an empty element list.
     * Default type is Integer.
     */
    public NumericCollectionBuilder() {
        this.elements = new ArrayList<>();
        this.type = Integer.class;
    }

    /**
     * Adds integer elements to the collection.
     * Sets the collection type to Integer.
     *
     * @param values the integer values to add
     * @return this builder instance for method chaining
     */
    public NumericCollectionBuilder withIntegers(Integer... values) {
        for (Integer value : values) {
            elements.add(value);
        }
        this.type = Integer.class;
        logger.debug("Added {} integer elements", values.length);
        return this;
    }

    /**
     * Adds double elements to the collection.
     * Sets the collection type to Double.
     *
     * @param values the double values to add
     * @return this builder instance for method chaining
     */
    public NumericCollectionBuilder withDoubles(Double... values) {
        for (Double value : values) {
            elements.add(value);
        }
        this.type = Double.class;
        logger.debug("Added {} double elements", values.length);
        return this;
    }

    /**
     * Adds a single element to the collection.
     *
     * @param element the element to add
     * @return this builder instance for method chaining
     */
    public NumericCollectionBuilder addElement(Number element) {
        elements.add(element);
        logger.trace("Added element: {}", element);
        return this;
    }

    /**
     * Clears all previously added elements.
     *
     * @return this builder instance for method chaining
     */
    public NumericCollectionBuilder clear() {
        int clearedCount = elements.size();
        elements.clear();
        logger.debug("Cleared {} elements from builder", clearedCount);
        return this;
    }

    /**
     * This method converts the collected elements into an array of the
     * specified numeric type and creates a new NumericArray instance.
     *
     * @return a new NumericArray instance containing all added elements
     * @throws NumericCollectionException if no elements have been added
     */
    public NumericArray<? extends Number> build() throws NumericCollectionException {
        if (elements.isEmpty()) {
            logger.error("Attempted to build collection with no elements");
            throw new NumericCollectionException("Cannot build collection with no elements");
        }

        logger.info("Building collection with {} elements of type {}",
                elements.size(), type.getSimpleName());

        if (type == Integer.class) {
            Integer[] array = elements.stream()
                    .map(Number::intValue)
                    .toArray(Integer[]::new);
            logger.info("Successfully built Integer array with {} elements", array.length);
            return new NumericArray<>(array);
        } else if (type == Double.class) {
            Double[] array = elements.stream()
                    .map(Number::doubleValue)
                    .toArray(Double[]::new);
            logger.info("Successfully built Double array with {} elements", array.length);
            return new NumericArray<>(array);
        } else {
            logger.error("Unsupported number type: {}", type.getName());
            throw new NumericCollectionException("Unsupported number type: " + type.getName());
        }
    }
}