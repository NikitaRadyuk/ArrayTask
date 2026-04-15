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
 * @author Your Name
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

        // Check if file exists
        boolean fileExists = Files.exists(path);
        if (!fileExists) {
            logger.error("File not found: {}", filePath);
            throw new NumericCollectionException("File not found: " + filePath);
        }

        // Check if path is a file (not a directory)
        boolean isDirectory = Files.isDirectory(path);
        if (isDirectory) {
            logger.error("Path is a directory, not a file: {}", filePath);
            throw new NumericCollectionException("Path is a directory: " + filePath);
        }

        // Read file using try-with-resources (auto-closes)
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

    /**
     * Reads all lines from a file and returns them as a stream.
     * Useful for processing large files line by line without storing all in memory.
     *
     * @param filePath the path to the file
     * @return a Stream of lines from the file
     * @throws NumericCollectionException if file not found or read error occurs
     */
    public Stream<String> readLinesAsStream(String filePath) throws NumericCollectionException {
        logger.debug("Attempting to open file as stream: {}", filePath);

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            logger.error("File not found: {}", filePath);
            throw new NumericCollectionException("File not found: " + filePath);
        }

        try {
            Stream<String> lines = Files.lines(path);
            logger.info("Successfully opened file as stream: {}", filePath);
            return lines;
        } catch (IOException e) {
            logger.error("Failed to open file stream: {} - {}", filePath, e.getMessage());
            throw new NumericCollectionException("Failed to open file: " + filePath, e);
        }
    }

    /**
     * Checks if a file exists and is readable.
     *
     * @param filePath the path to the file
     * @return true if the file exists and is readable, false otherwise
     */
    public boolean isFileReadable(String filePath) {
        Path path = Paths.get(filePath);
        boolean exists = Files.exists(path);
        boolean isReadable = Files.isReadable(path);
        boolean isFile = !Files.isDirectory(path);

        boolean result = exists && isReadable && isFile;

        if (result) {
            logger.debug("File is readable: {}", filePath);
        } else {
            logger.warn("File is not readable: {} (exists: {}, readable: {}, isFile: {})",
                    filePath, exists, isReadable, isFile);
        }

        return result;
    }

    /**
     * Gets the file size in bytes.
     *
     * @param filePath the path to the file
     * @return file size in bytes, or -1 if error occurs
     * @throws NumericCollectionException if file not found
     */
    public long getFileSize(String filePath) throws NumericCollectionException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            logger.error("File not found: {}", filePath);
            throw new NumericCollectionException("File not found: " + filePath);
        }

        try {
            long size = Files.size(path);
            logger.debug("File size: {} bytes for {}", size, filePath);
            return size;
        } catch (IOException e) {
            logger.error("Failed to get file size: {} - {}", filePath, e.getMessage());
            throw new NumericCollectionException("Failed to get file size: " + filePath, e);
        }
    }
}