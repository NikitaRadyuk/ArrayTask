package com.inno.course.reader;

import com.inno.course.entity.NumericArray;
import com.inno.course.exception.NumericCollectionException;
import com.inno.course.factory.NumericCollectionFactory;
import com.inno.course.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser class for converting file lines into NumericArray objects.
 * Uses validator and factory for parsing and creation.
 *
 * @version 1.0
 * @see DataValidator
 * @see NumericCollectionFactory
 * @see NumericArray
 */
public class DataParser {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(DataParser.class.getName());

    /** Validator for checking line validity */
    private final DataValidator validator;

    /** Factory for creating NumericArray instances */
    private final NumericCollectionFactory factory;

    /**
     * Constructs a new DataParser with the specified validator and factory.
     *
     * @param validator the validator for validating lines (cannot be null)
     * @param factory the factory for creating collections (cannot be null)
     */
    public DataParser(DataValidator validator, NumericCollectionFactory factory) {
        this.validator = validator;
        this.factory = factory;
        logger.debug("DataParser initialized with validator: {} and factory: {}",
                validator.getClass().getSimpleName(), factory.getClass().getSimpleName());
    }

    /**
     * Parses a list of lines and creates collections for valid ones.
     *
     * @param lines the lines to parse (can be null or empty)
     * @return a list of created NumericArray objects (never null, may be empty)
     * @throws NumericCollectionException if a fatal error occurs during parsing
     */
    public List<NumericArray<?>> parseLines(List<String> lines) throws NumericCollectionException {
        List<NumericArray<?>> collections = new ArrayList<>();

        if (lines == null || lines.isEmpty()) {
            logger.warn("Input lines list is null or empty, nothing to parse");
            return collections;
        }

        logger.info("Starting to parse {} lines", lines.size());
        int validCollectionsCount = 0;
        int skippedLinesCount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNumber = i + 1;

            logger.debug("Processing line {}: '{}'", lineNumber, line);

            if (line == null || line.trim().isEmpty()) {
                logger.info("Line {} is empty, skipping", lineNumber);
                skippedLinesCount++;
                continue;
            }

            boolean isValid = validator.isValidLine(line);
            if (!isValid) {
                logger.warn("Line {} contains invalid data, skipping: '{}'", lineNumber, line);
                skippedLinesCount++;
                continue;
            }

            String[] validValues = validator.parseAndValidate(line);

            if (validValues.length == 0) {
                logger.info("Line {} contains no valid numbers, skipping", lineNumber);
                skippedLinesCount++;
                continue;
            }

            logger.debug("Line {} yielded {} valid numeric values", lineNumber, validValues.length);

            try {
                NumericArray<?> collection = tryCreateCollection(validValues, lineNumber);
                collections.add(collection);
                validCollectionsCount++;

                logger.info("Successfully created collection from line {} with {} elements",
                        lineNumber, collection.size());

            } catch (NumericCollectionException e) {
                logger.error("Failed to create collection from line {}: {}", lineNumber, e.getMessage());
            }
        }

        logger.info("Parsing completed. Created {} collections, skipped {} lines",
                validCollectionsCount, skippedLinesCount);

        return collections;
    }

    /**
     * Attempts to create a NumericArray from string values.
     * First tries Integer type, then falls back to Double.
     *
     * @param validValues the validated numeric string values
     * @param lineNumber the original line number (for logging)
     * @return a NumericArray instance
     * @throws NumericCollectionException if both Integer and Double parsing fail
     */
    private NumericArray<?> tryCreateCollection(String[] validValues, int lineNumber)
            throws NumericCollectionException {

        logger.debug("Attempting to create collection from line {} as Integer", lineNumber);

        try {
            return factory.createCollection(validValues, Integer.class);
        } catch (NumericCollectionException e) {
            logger.debug("Failed to parse line {} as Integer: {}", lineNumber, e.getMessage());
            logger.debug("Attempting to create collection from line {} as Double", lineNumber);
            return factory.createCollection(validValues, Double.class);
        }
    }
}