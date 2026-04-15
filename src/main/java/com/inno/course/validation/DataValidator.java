package com.inno.course.validation;

/**
 * Interface for validating and parsing data lines.
 */
public interface DataValidator {

    /**
     * Checks if a line contains valid numeric data.
     *
     * @param line the line to validate
     * @return true if the line is valid, false otherwise
     */
    boolean isValidLine(String line);

    /**
     * Parses a line and extracts valid numeric values.
     *
     * @param line the line to parse
     * @return an array of valid numeric strings
     */
    String[] parseAndValidate(String line);
}