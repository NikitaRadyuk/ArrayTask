package com.inno.course;

import com.inno.course.builder.NumericCollectionBuilder;
import com.inno.course.comparator.*;
import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.entity.NumericArray;
import com.inno.course.exception.NumericCollectionException;
import com.inno.course.factory.NumericCollectionFactory;
import com.inno.course.reader.DataParser;
import com.inno.course.reader.FileReader;
import com.inno.course.repository.Repository;
import com.inno.course.repository.impl.CollectionRepository;
import com.inno.course.repository.specifications.*;
import com.inno.course.service.StatisticsService;
import com.inno.course.service.impl.StatisticsServiceImpl;
import com.inno.course.service.SortingService;
import com.inno.course.service.impl.SortingServiceImpl;
import com.inno.course.validation.LineValidator;
import com.inno.course.warehouse.Warehouse;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main application class demonstrating all functionality of the numeric collection system.
 *
 * @version 2.0
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String SEPARATOR = "═══════════════════════════════════════════════════════════════";

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        printHeader("NUMERIC COLLECTION APPLICATION");

        Repository repository = CollectionRepository.getInstance();
        Warehouse warehouse = Warehouse.getInstance();

        // Демонстрация всех возможностей
        demonstrateDirectCreation();
        demonstrateBuilderPattern();
        demonstrateFactoryPattern();
        demonstrateRepositoryOperations(repository);
        demonstrateSpecifications(repository);
        demonstrateComparators(repository);
        demonstrateObserverPattern(repository, warehouse);
        demonstrateFileProcessing(repository);
        demonstrateStatisticsAndSorting();

        // Финальная статистика
        printSeparator();
        warehouse.displayAllStatistics();
        printSeparator();

        logger.log(Level.INFO, "✅ Application completed successfully");
    }

    /**
     * Demonstrates direct creation of numeric collections.
     */
    private static void demonstrateDirectCreation() {
        printSection("1. DIRECT COLLECTION CREATION");

        NumericArray<Integer> intCollection = new NumericArray<>(
                new Integer[]{10, 20, 30, 40, 50}
        );
        logger.log(Level.INFO, "Created Integer collection: ID={0}, size={1}, elements={2}",
                new Object[]{intCollection.getId(), intCollection.size(), formatElements(intCollection)});

        NumericArray<Double> doubleCollection = new NumericArray<>(
                new Double[]{1.1, 2.2, 3.3, 4.4, 5.5}
        );
        logger.log(Level.INFO, "Created Double collection: ID={0}, size={1}, elements={2}",
                new Object[]{doubleCollection.getId(), doubleCollection.size(), formatElements(doubleCollection)});
    }

    /**
     * Demonstrates builder pattern for collection creation.
     */
    private static void demonstrateBuilderPattern() {
        printSection("2. BUILDER PATTERN");

        NumericCollectionBuilder builder = new NumericCollectionBuilder();

        try {
            NumericArray<?> intCollection = builder
                    .withIntegers(5, 3, 8, 1, 9, 2, 7, 4, 6)
                    .build();
            logger.log(Level.INFO, "Built Integer collection: ID={0}, elements={1}",
                    new Object[]{intCollection.getId(), formatElements(intCollection)});

            builder.clear();
            NumericArray<?> doubleCollection = builder
                    .withDoubles(5.5, 2.2, 8.8, 1.1, 9.9, 3.3, 7.7)
                    .build();
            logger.log(Level.INFO, "Built Double collection: ID={0}, elements={1}",
                    new Object[]{doubleCollection.getId(), formatElements(doubleCollection)});

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "❌ Builder pattern failed: {0}", e.getMessage());
        }
    }

    /**
     * Demonstrates factory pattern for collection creation.
     */
    private static void demonstrateFactoryPattern() {
        printSection("3. FACTORY PATTERN");

        NumericCollectionFactory factory = new NumericCollectionFactory();

        try {
            String[] values = {"100", "200", "300", "400", "500"};
            NumericArray<Integer> collection = factory.createCollection(values, Integer.class);
            logger.log(Level.INFO, "Factory created collection: ID={0}, elements={1}",
                    new Object[]{collection.getId(), formatElements(collection)});

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "❌ Factory pattern failed: {0}", e.getMessage());
        }
    }

    /**
     * Demonstrates repository operations.
     *
     * @param repository the repository instance
     */
    private static void demonstrateRepositoryOperations(Repository repository) {
        printSection("4. REPOSITORY OPERATIONS");

        try {
            NumericCollectionBuilder builder = new NumericCollectionBuilder();

            NumericArray<?> collection1 = builder
                    .clear()
                    .withIntegers(10, 20, 30, 40, 50)
                    .build();

            NumericArray<?> collection2 = builder
                    .clear()
                    .withDoubles(1.1, 2.2, 3.3, 4.4, 5.5, 6.6)
                    .build();

            NumericArray<?> collection3 = builder
                    .clear()
                    .withIntegers(5, 3, 8, 1, 9, 2, 7, 4, 6)
                    .build();

            repository.add(collection1);
            repository.add(collection2);
            repository.add(collection3);

            logger.log(Level.INFO, "Repository size after adding 3 collections: {0}", repository.size());
            logger.log(Level.INFO, "Collections in repository: IDs {0}, {1}, {2}",
                    new Object[]{collection1.getId(), collection2.getId(), collection3.getId()});

            repository.remove(collection2.getId());
            logger.log(Level.INFO, "Repository size after removing collection {0}: {1}",
                    new Object[]{collection2.getId(), repository.size()});

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "❌ Repository operations failed: {0}", e.getMessage());
        }
    }

    /**
     * Demonstrates specifications for querying collections.
     *
     * @param repository the repository instance
     */
    private static void demonstrateSpecifications(Repository repository) {
        printSection("5. SPECIFICATIONS (QUERY BY CRITERIA)");

        List<AbstractNumericArray<?>> allCollections = repository.findAll();
        logger.log(Level.INFO, "Total collections in repository: {0}", allCollections.size());

        SumSpecification sumSpec = SumSpecification.greaterThan(100.0);
        List<AbstractNumericArray<?>> highSumCollections = repository.query(sumSpec);
        logger.log(Level.INFO, "Collections with sum > 100: {0}", highSumCollections.size());
        highSumCollections.forEach(c ->
                logger.log(Level.INFO, "  • Collection {0}: sum={1}",
                        new Object[]{c.getId(), getSumFromWarehouse(c.getId())})
        );

        SizeSpecification sizeSpec = SizeSpecification.greaterThan(5);
        List<AbstractNumericArray<?>> largeCollections = repository.query(sizeSpec);
        logger.log(Level.INFO, "Collections with size > 5: {0}", largeCollections.size());

        if (allCollections.size() >= 2) {
            AverageSpecification avgSpec = AverageSpecification.between(20.0, 40.0);
            List<AbstractNumericArray<?>> avgBetween = repository.query(avgSpec);
            logger.log(Level.INFO, "Collections with average between 20-40: {0}", avgBetween.size());
        }
    }

    /**
     * Demonstrates comparators for sorting collections.
     *
     * @param repository the repository instance
     */
    private static void demonstrateComparators(Repository repository) {
        printSection("6. COMPARATORS (SORTING)");

        List<AbstractNumericArray<?>> collections = repository.findAll();

        if (collections.size() > 1) {
            collections.sort(new CollectionIDComparator());
            logger.log(Level.INFO, "Sorted by ID:");
            collections.forEach(c -> logger.log(Level.INFO, "  • Collection {0}", c.getId()));

            collections.sort(new CollectionSizeComparator());
            logger.log(Level.INFO, "Sorted by size:");
            collections.forEach(c -> logger.log(Level.INFO, "  • Collection {0} (size: {1})",
                    new Object[]{c.getId(), c.size()}));

            collections.sort(new CollectionFirstElementComparator());
            logger.log(Level.INFO, "Sorted by first element:");
            collections.forEach(c -> {
                if (!c.isEmpty()) {
                    logger.log(Level.INFO, "  • Collection {0} (first: {1})",
                            new Object[]{c.getId(), c.getAsDouble(0)});
                }
            });
        } else {
            logger.log(Level.INFO, "Not enough collections for comparator demonstration");
        }
    }

    /**
     * Demonstrates observer pattern for automatic statistics updates.
     *
     * @param repository the repository instance
     * @param warehouse the warehouse instance
     */
    private static void demonstrateObserverPattern(Repository repository, Warehouse warehouse) {
        printSection("7. OBSERVER PATTERN (AUTO-UPDATE STATISTICS)");

        List<AbstractNumericArray<?>> collections = repository.findAll();

        if (!collections.isEmpty()) {
            AbstractNumericArray<?> collection = collections.get(0);
            Long id = collection.getId();

            logger.log(Level.INFO, "Working with collection ID: {0}", id);
            logger.log(Level.INFO, "Initial statistics:");
            printCollectionStats(warehouse, id);

            if (!collection.isEmpty()) {
                @SuppressWarnings("unchecked")
                AbstractNumericArray<Number> numCollection =
                        (AbstractNumericArray<Number>) collection;

                Object firstElement = collection.getElements()[0];
                logger.log(Level.INFO, "Changing first element from {0} to {1}",
                        new Object[]{firstElement, firstElement instanceof Integer ? 999 : 999.0});

                if (firstElement instanceof Integer) {
                    numCollection.setElement(0, 999);
                } else if (firstElement instanceof Double) {
                    numCollection.setElement(0, 999.0);
                }

                logger.log(Level.INFO, "Updated statistics:");
                printCollectionStats(warehouse, id);
            }
        } else {
            logger.log(Level.INFO, "No collections available for observer demonstration");
        }
    }

    /**
     * Demonstrates file processing for creating collections from file.
     *
     * @param repository the repository instance
     */
    private static void demonstrateFileProcessing(Repository repository) {
        printSection("8. FILE PROCESSING");

        String filePath = "src/main/resources/data/input.txt";
        FileReader fileReader = new FileReader();
        LineValidator validator = new LineValidator();
        NumericCollectionFactory factory = new NumericCollectionFactory();
        DataParser parser = new DataParser(validator, factory);

        try {
            List<String> lines = fileReader.readLines(filePath);
            logger.log(Level.INFO, "Read {0} lines from file: {1}",
                    new Object[]{lines.size(), filePath});

            List<NumericArray<?>> collections = parser.parseLines(lines);
            logger.log(Level.INFO, "Successfully created {0} collections from file", collections.size());

            for (NumericArray<?> collection : collections) {
                repository.add(collection);
                logger.log(Level.INFO, "Added collection ID={0}, size={1}, elements={2}",
                        new Object[]{collection.getId(), collection.size(), formatElements(collection)});
            }

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "❌ File processing failed: {0}", e.getMessage());
        }
    }

    /**
     * Demonstrates statistics calculation and sorting algorithms.
     */
    private static void demonstrateStatisticsAndSorting() {
        printSection("9. STATISTICS AND SORTING ALGORITHMS");

        try {
            NumericArray<Integer> collection = new NumericArray<>(
                    new Integer[]{15, 3, 22, 8, 19, 1, 14, 9, 5}
            );

            StatisticsService statsService = new StatisticsServiceImpl();
            SortingService sortingService = new SortingServiceImpl();

            logger.log(Level.INFO, "Original collection ID={0}: {1}",
                    new Object[]{collection.getId(), formatElements(collection)});

            Optional<Integer> min = statsService.findMinimum(collection);
            Optional<Integer> max = statsService.findMaximum(collection);
            OptionalDouble sum = statsService.calculateSum(collection);
            OptionalDouble avg = statsService.calculateAverage(collection);

            logger.log(Level.INFO, "Statistics:");
            logger.log(Level.INFO, "  • Minimum: {0}", min.orElse(null));
            logger.log(Level.INFO, "  • Maximum: {0}", max.orElse(null));
            logger.log(Level.INFO, "  • Sum: {0}", sum.isPresent() ? sum.getAsDouble() : "N/A");
            logger.log(Level.INFO, "  • Average: {0}", avg.isPresent() ? avg.getAsDouble() : "N/A");

            // Bubble Sort
            NumericArray<Integer> bubbleSorted = (NumericArray<Integer>) collection.copy();
            long bubbleStart = System.nanoTime();
            sortingService.bubbleSort(bubbleSorted);
            long bubbleTime = System.nanoTime() - bubbleStart;
            logger.log(Level.INFO, "Bubble sort result: {0} (took {1} ns)",
                    new Object[]{formatElements(bubbleSorted), bubbleTime});

            // Quick Sort
            NumericArray<Integer> quickSorted = (NumericArray<Integer>) collection.copy();
            long quickStart = System.nanoTime();
            sortingService.quickSort(quickSorted);
            long quickTime = System.nanoTime() - quickStart;
            logger.log(Level.INFO, "Quick sort result:  {0} (took {1} ns)",
                    new Object[]{formatElements(quickSorted), quickTime});

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Statistics and sorting failed: {0}", e.getMessage());
        }
    }

    // ==================== HELPER METHODS ====================

    /**
     * Prints a formatted header section.
     *
     * @param title the section title
     */
    private static void printSection(String title) {
        logger.log(Level.INFO, "\n┌─ {0} ─┐", title);
        logger.log(Level.INFO, "│");
    }

    /**
     * Prints a formatted header for the application.
     *
     * @param title the application title
     */
    private static void printHeader(String title) {
        logger.log(Level.INFO, "\n{0}", SEPARATOR);
        logger.log(Level.INFO, "  {0}", title);
        logger.log(Level.INFO, "{0}\n", SEPARATOR);
    }

    /**
     * Prints a separator line.
     */
    private static void printSeparator() {
        logger.log(Level.INFO, SEPARATOR);
    }

    /**
     * Prints statistics for a collection.
     *
     * @param warehouse the warehouse instance
     * @param id the collection ID
     */
    private static void printCollectionStats(Warehouse warehouse, Long id) {
        logger.log(Level.INFO, "  • Sum: {0}", warehouse.getSum(id));
        logger.log(Level.INFO, "  • Min: {0}", warehouse.getMin(id));
        logger.log(Level.INFO, "  • Max: {0}", warehouse.getMax(id));
        logger.log(Level.INFO, "  • Avg: {0}", warehouse.getAverage(id));
        logger.log(Level.INFO, "  • Size: {0}", warehouse.getSize(id));
    }

    /**
     * Gets sum from warehouse for a collection.
     *
     * @param id the collection ID
     * @return the sum value
     */
    private static double getSumFromWarehouse(Long id) {
        Warehouse warehouse = Warehouse.getInstance();
        return warehouse.getSum(id);
    }

    /**
     * Formats collection elements for logging.
     *
     * @param collection the collection
     * @return formatted string of elements
     */
    private static String formatElements(AbstractNumericArray<?> collection) {
        if (collection.isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < Math.min(collection.size(), 10); i++) {
            if (i > 0) sb.append(", ");
            sb.append(collection.getAsDouble(i));
        }
        if (collection.size() > 10) {
            sb.append(", ... (").append(collection.size()).append(" elements total)");
        }
        sb.append("]");
        return sb.toString();
    }
}