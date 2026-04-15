package com.inno.course.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementation of DataValidator for validating lines with numeric values.
 * Supports multiple delimiters: commas, semicolons, hyphens, spaces, and pipes.
 */
public class LineValidator implements DataValidator {

    private static final Logger logger = Logger.getLogger(LineValidator.class.getName());
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");

    /**
     * Validates if a line contains only valid numbers.
     *
     * @param line the line to validate
     * @return true if all tokens are valid numbers or line is empty
     */
    @Override
    public boolean isValidLine(String line) {
        if (line == null) {
            return false;
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return true;
        }

        String[] parts = splitLine(trimmed);
        for (String part : parts) {
            String cleaned = part.trim();
            if (!cleaned.isEmpty() && !NUMBER_PATTERN.matcher(cleaned).matches()) {
                logger.log(Level.FINE, "Invalid number format: {0}", cleaned);
                return false;
            }
        }

        return true;
    }

    /**
     * Parses a line and extracts all valid numeric values.
     *
     * @param line the line to parse
     * @return an array of valid numeric strings (invalid ones are skipped)
     */
    @Override
    public String[] parseAndValidate(String line) {
        if (line == null) {
            return new String[0];
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return new String[0];
        }

        String[] parts = splitLine(trimmed);
        List<String> validNumbers = new ArrayList<>();

        for (String part : parts) {
            String cleaned = part.trim();
            if (!cleaned.isEmpty() && NUMBER_PATTERN.matcher(cleaned).matches()) {
                validNumbers.add(cleaned);
            } else if (!cleaned.isEmpty()) {
                logger.log(Level.WARNING, "Skipping invalid value: {0}", cleaned);
            }
        }

        logger.log(Level.FINE, "Parsed {0} valid numbers from line", validNumbers.size());
        return validNumbers.toArray(new String[0]);
    }

    /**
     * Splits a line by common delimiters.
     *
     * @param line the line to split
     * @return array of string tokens
     */
    private String[] splitLine(String line) {
        return line.split("[,;\\-\\s|]+");
    }
}