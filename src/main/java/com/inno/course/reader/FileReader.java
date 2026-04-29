package com.inno.course.reader;

import com.inno.course.exception.NumericCollectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reader class for reading data from text files.
 *
 * @version 1.0
 * @see NumericCollectionException
 * @see Files
 * @see Path
 */
public class FileReader {

    /** Logger instance for this class using Log4j2 */
    private static final Logger logger = LogManager.getLogger(FileReader.class.getName());

    /**
     * Reads all lines from a file.
     *
     * @param filePath the path to the file (relative or absolute)
     * @return a list of non-null lines from the file (never null)
     * @throws NumericCollectionException if file not found or read error occurs
     */
    public List<String> readLines(String filePath) throws NumericCollectionException {
        logger.debug("Attempting to read file: {}", filePath);

        Path path = Paths.get(filePath);
        logger.trace("Converted to Path object: {}", path.toAbsolutePath());

        boolean fileExists = Files.exists(path);
        if (!fileExists) {
            logger.error("File not found: {}", filePath);
            throw new NumericCollectionException("File not found: " + filePath);
        }

        boolean isDirectory = Files.isDirectory(path);
        if (isDirectory) {
            logger.error("Path is a directory, not a file: {}", filePath);
            throw new NumericCollectionException("Path is a directory: " + filePath);
        }

        try (Stream<String> lines = Files.lines(path)) {
            logger.debug("File opened successfully, reading lines...");

            List<String> nonEmptyLines = lines
                    .filter(line -> line != null)
                    .collect(Collectors.toList());

            logger.info("Successfully read {} lines from file: {}", nonEmptyLines.size(), filePath);

            if (logger.isDebugEnabled()) {
                logger.debug("First 5 lines (if any):");
                nonEmptyLines.stream()
                        .limit(5)
                        .forEach(line -> logger.debug("  {}", line));
            }

            return nonEmptyLines;

        } catch (IOException e) {
            logger.error("Failed to read file: {} - {}", filePath, e.getMessage());
            throw new NumericCollectionException("Failed to read file: " + filePath, e);
        }
    }

}