package com.inno.course;

import com.inno.course.builder.NumericCollectionBuilder;
import com.inno.course.comparator.*;
import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.entity.NumericArray;
import com.inno.course.exception.NumericCollectionException;
import com.inno.course.factory.NumericCollectionFactory;
import com.inno.course.reader.DataParser;
import com.inno.course.reader.FileReader;
import com.inno.course.repository.CollectionRepository;
import com.inno.course.repository.Repository;
import com.inno.course.repository.specifications.*;
import com.inno.course.service.StatisticsService;
import com.inno.course.service.impl.StatisticsServiceImpl;
import com.inno.course.service.SortingService;
import com.inno.course.service.impl.SortingServiceImpl;
import com.inno.course.validation.LineValidator;
import com.inno.course.warehouse.Warehouse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main application class demonstrating all functionality of the numeric collection system.
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Main entry point of the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Starting Numeric Collection Application");

        Repository repository = CollectionRepository.getInstance();
        Warehouse warehouse = Warehouse.getInstance();

        demonstrateDirectCreation();
        demonstrateBuilderPattern();
        demonstrateFactoryPattern();
        demonstrateRepositoryOperations(repository);
        demonstrateSpecifications(repository);
        demonstrateComparators(repository);
        demonstrateObserverPattern(repository, warehouse);
        demonstrateFileProcessing(repository);
        demonstrateStatisticsAndSorting();

        warehouse.displayAllStatistics();

        logger.log(Level.INFO, "Application completed successfully");
    }

    /**
     * Demonstrates direct creation of numeric collections.
     */
    private static void demonstrateDirectCreation() {
        logger.log(Level.INFO, "\n=== 1. Direct Collection Creation ===");

        NumericArray<Integer> intCollection = new NumericArray<>(
                new Integer[]{10, 20, 30, 40, 50}
        );
        logger.log(Level.INFO, "Created: {0}", intCollection);

        NumericArray<Double> doubleCollection = new NumericArray<>(
                new Double[]{1.1, 2.2, 3.3, 4.4, 5.5}
        );
        logger.log(Level.INFO, "Created: {0}", doubleCollection);
    }

    /**
     * Demonstrates builder pattern for collection creation.
     */
    private static void demonstrateBuilderPattern() {
        logger.log(Level.INFO, "\n=== 2. Builder Pattern ===");

        NumericCollectionBuilder builder = new NumericCollectionBuilder();

        try {
            NumericArray<?> collection1 = builder
                    .withIntegers(5, 3, 8, 1, 9, 2, 7, 4, 6)
                    .build();
            logger.log(Level.INFO, "Built: {0}", collection1);

            builder.clear();
            NumericArray<?> collection2 = builder
                    .withDoubles(5.5, 2.2, 8.8, 1.1, 9.9, 3.3, 7.7)
                    .build();
            logger.log(Level.INFO, "Built: {0}", collection2);

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "Builder failed", e);
        }
    }

    /**
     * Demonstrates factory pattern for collection creation.
     */
    private static void demonstrateFactoryPattern() {
        logger.log(Level.INFO, "\n=== 3. Factory Pattern ===");

        NumericCollectionFactory factory = new NumericCollectionFactory();

        try {
            String[] values = {"100", "200", "300", "400", "500"};
            NumericArray<Integer> collection = factory.createCollection(values, Integer.class);
            logger.log(Level.INFO, "Factory created: {0}", collection);

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "Factory failed", e);
        }
    }

    /**
     * Demonstrates repository operations.
     *
     * @param repository the repository instance
     */
    private static void demonstrateRepositoryOperations(Repository repository) {
        logger.log(Level.INFO, "\n=== 4. Repository Operations ===");

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

            logger.log(Level.INFO, "Repository size: {0}", repository.size());

            repository.remove(collection2.getId());
            logger.log(Level.INFO, "After removal, repository size: {0}", repository.size());

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "Repository operations failed", e);
        }
    }

    /**
     * Demonstrates specifications for querying collections.
     *
     * @param repository the repository instance
     */
    private static void demonstrateSpecifications(Repository repository) {
        logger.log(Level.INFO, "\n=== 5. Specifications ===");

        SumSpecification sumSpec = SumSpecification.greaterThan(100.0);
        List<AbstractNumericArray<?>> highSumCollections = repository.query(sumSpec);
        logger.log(Level.INFO, "Collections with sum > 100: {0}", highSumCollections.size());

        SizeSpecification sizeSpec = SizeSpecification.greaterThan(5);
        List<AbstractNumericArray<?>> largeCollections = repository.query(sizeSpec);
        logger.log(Level.INFO, "Collections with size > 5: {0}", largeCollections.size());

        AverageSpecification avgSpec = AverageSpecification.between(20.0, 40.0);
        List<AbstractNumericArray<?>> avgBetween = repository.query(avgSpec);
        logger.log(Level.INFO, "Collections with average between 20-40: {0}", avgBetween.size());
    }

    /**
     * Demonstrates comparators for sorting collections.
     *
     * @param repository the repository instance
     */
    private static void demonstrateComparators(Repository repository) {
        logger.log(Level.INFO, "\n=== 6. Comparators ===");

        List<AbstractNumericArray<?>> collections = repository.findAll();

        if (collections.size() > 1) {
            Collections.sort(collections, new CollectionIDComparator());
            logger.log(Level.INFO, "Sorted by ID:");
            collections.forEach(c -> logger.log(Level.INFO, "  {0}", c.getId()));

            Collections.sort(collections, new CollectionSizeComparator());
            logger.log(Level.INFO, "Sorted by size:");
            collections.forEach(c -> logger.log(Level.INFO, "  {0} (size: {1})",
                    new Object[]{c.getId().substring(0, 8), c.size()}));

            Collections.sort(collections, new CollectionFirstElementComparator());
            logger.log(Level.INFO, "Sorted by first element:");
            collections.forEach(c -> {
                if (!c.isEmpty()) {
                    logger.log(Level.INFO, "  {0} (first: {1})",
                            new Object[]{c.getId().substring(0, 8), c.getAsDouble(0)});
                }
            });
        }
    }

    /**
     * Demonstrates observer pattern for automatic statistics updates.
     *
     * @param repository the repository instance
     * @param warehouse the warehouse instance
     */
    private static void demonstrateObserverPattern(Repository repository, Warehouse warehouse) {
        logger.log(Level.INFO, "\n=== 7. Observer Pattern ===");

        List<AbstractNumericArray<?>> collections = repository.findAll();

        if (!collections.isEmpty()) {
            AbstractNumericArray<?> collection = collections.get(0);
            logger.log(Level.INFO, "Collection ID: {0}", collection.getId());
            logger.log(Level.INFO, "Before change - Sum: {0}, Min: {1}, Max: {2}, Avg: {3}",
                    new Object[]{
                            warehouse.getSum(collection.getId()),
                            warehouse.getMin(collection.getId()),
                            warehouse.getMax(collection.getId()),
                            warehouse.getAverage(collection.getId())});

            if (collection.size() > 0) {
                @SuppressWarnings("unchecked")
                AbstractNumericArray<Number> numCollection =
                        (AbstractNumericArray<Number>) collection;

                if (collection.getElements()[0] instanceof Integer) {
                    numCollection.setElement(0, 999);
                } else if (collection.getElements()[0] instanceof Double) {
                    numCollection.setElement(0, 999.0);
                }

                logger.log(Level.INFO, "After change - Sum: {0}, Min: {1}, Max: {2}, Avg: {3}",
                        new Object[]{
                                warehouse.getSum(collection.getId()),
                                warehouse.getMin(collection.getId()),
                                warehouse.getMax(collection.getId()),
                                warehouse.getAverage(collection.getId())});
            }
        }
    }

    /**
     * Demonstrates file processing for creating collections from file.
     *
     * @param repository the repository instance
     */
    private static void demonstrateFileProcessing(Repository repository) {
        logger.log(Level.INFO, "\n=== 8. File Processing ===");

        String filePath = "src/main/resources/data/input.txt";
        FileReader fileReader = new FileReader();
        LineValidator validator = new LineValidator();
        NumericCollectionFactory factory = new NumericCollectionFactory();
        DataParser parser = new DataParser(validator, factory);

        try {
            List<String> lines = fileReader.readLines(filePath);
            logger.log(Level.INFO, "Read {0} lines from file", lines.size());

            List<NumericArray<?>> collections = parser.parseLines(lines);
            logger.log(Level.INFO, "Successfully created {0} collections", collections.size());

            for (NumericArray<?> collection : collections) {
                repository.add(collection);
            }

        } catch (NumericCollectionException e) {
            logger.log(Level.SEVERE, "File processing failed", e);
        }
    }

    /**
     * Demonstrates statistics calculation and sorting algorithms.
     */
    private static void demonstrateStatisticsAndSorting() {
        logger.log(Level.INFO, "\n=== 9. Statistics and Sorting ===");

        try {
            NumericArray<Integer> collection = new NumericArray<>(
                    new Integer[]{15, 3, 22, 8, 19, 1, 14, 9, 5}
            );

            StatisticsService statsService = new StatisticsServiceImpl();
            SortingService sortingService = new SortingServiceImpl();

            Optional<Integer> min = statsService.findMinimum(collection);
            Optional<Integer> max = statsService.findMaximum(collection);
            OptionalDouble sum = statsService.calculateSum(collection);
            OptionalDouble avg = statsService.calculateAverage(collection);

            logger.log(Level.INFO, "Original: {0}", collection);
            logger.log(Level.INFO, "Min: {0}, Max: {1}, Sum: {2}, Average: {3}",
                    new Object[]{
                            min.orElse(null),
                            max.orElse(null),
                            sum.isPresent() ? sum.getAsDouble() : "N/A",
                            avg.isPresent() ? avg.getAsDouble() : "N/A"});

            NumericArray<Integer> bubbleSorted = (NumericArray<Integer>) collection.copy();
            sortingService.bubbleSort(bubbleSorted);
            logger.log(Level.INFO, "Bubble sort: {0}", bubbleSorted);

            NumericArray<Integer> quickSorted = (NumericArray<Integer>) collection.copy();
            sortingService.quickSort(quickSorted);
            logger.log(Level.INFO, "Quick sort: {0}", quickSorted);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Statistics and sorting failed", e);
        }
    }
}