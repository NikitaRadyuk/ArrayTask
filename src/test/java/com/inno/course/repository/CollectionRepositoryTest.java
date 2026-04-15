package com.inno.course.repository;

import com.inno.course.entity.NumericArray;
import com.inno.course.repository.specifications.*;
import com.inno.course.warehouse.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CollectionRepository Tests")
public class CollectionRepositoryTest {

    private CollectionRepository repository;
    private Warehouse warehouse;
    private NumericArray<Integer> array1;
    private NumericArray<Integer> array2;
    private NumericArray<Double> array3;
    private NumericArray<Integer> array4;

    @BeforeEach
    void setUp() {
        // Clean repository before each test
        repository = CollectionRepository.getInstance();
        repository.clear();

        warehouse = Warehouse.getInstance();

        // Create test arrays
        array1 = new NumericArray<>(new Integer[]{1, 2, 3, 4, 5});
        array2 = new NumericArray<>(new Integer[]{10, 20, 30, 40, 50});
        array3 = new NumericArray<>(new Double[]{1.1, 2.2, 3.3, 4.4, 5.5});
        array4 = new NumericArray<>(new Integer[]{100, 200, 300});
    }

    // ==================== ADD TESTS ====================

    @Test
    @DisplayName("Should add collection to repository")
    void testAddCollection() {
        repository.add(array1);

        assertEquals(1, repository.size());
        assertNotNull(repository.findById(array1.getId()));
    }

    @Test
    @DisplayName("Should add multiple collections to repository")
    void testAddMultipleCollections() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array3);

        assertEquals(3, repository.size());
        assertNotNull(repository.findById(array1.getId()));
        assertNotNull(repository.findById(array2.getId()));
        assertNotNull(repository.findById(array3.getId()));
    }

    @Test
    @DisplayName("Should not add null collection")
    void testAddNullCollection() {
        repository.add(null);

        assertEquals(0, repository.size());
    }

    // ==================== REMOVE TESTS ====================

    @Test
    @DisplayName("Should remove collection by id")
    void testRemoveById() {
        repository.add(array1);
        repository.add(array2);

        String idToRemove = array1.getId();
        repository.remove(idToRemove);

        assertEquals(1, repository.size());
        assertNull(repository.findById(idToRemove));
        assertNotNull(repository.findById(array2.getId()));
    }

    @Test
    @DisplayName("Should remove collection by object")
    void testRemoveByObject() {
        repository.add(array1);
        repository.add(array2);

        repository.remove(array1);

        assertEquals(1, repository.size());
        assertNull(repository.findById(array1.getId()));
        assertNotNull(repository.findById(array2.getId()));
    }

    @Test
    @DisplayName("Should not fail when removing non-existent id")
    void testRemoveNonExistentId() {
        repository.add(array1);

        repository.remove("non-existent-id");

        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should not fail when removing null collection")
    void testRemoveNullCollection() {
        repository.add(array1);

        repository.remove((NumericArray<?>) null);

        assertEquals(1, repository.size());
    }

    // ==================== FIND TESTS ====================

    @Test
    @DisplayName("Should find collection by id")
    void testFindById() {
        repository.add(array1);
        repository.add(array2);

        NumericArray<?> found = (NumericArray<?>) repository.findById(array2.getId());

        assertNotNull(found);
        assertEquals(array2.getId(), found.getId());
        assertArrayEquals(array2.getElements(), found.getElements());
    }

    @Test
    @DisplayName("Should return null for non-existent id")
    void testFindByNonExistentId() {
        repository.add(array1);

        NumericArray<?> found = (NumericArray<?>) repository.findById("non-existent-id");

        assertNull(found);
    }

    @Test
    @DisplayName("Should find all collections")
    void testFindAll() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array3);

        List<com.inno.course.entity.AbstractNumericArray<?>> all = repository.findAll();

        assertEquals(3, all.size());
    }

    // ==================== QUERY TESTS ====================

    @Test
    @DisplayName("Should query collections by sum greater than threshold")
    void testQueryBySumGreaterThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SumSpecification.greaterThan(100);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(array2.getId())));
        assertTrue(result.stream().anyMatch(c -> c.getId().equals(array4.getId())));
    }

    @Test
    @DisplayName("Should query collections by sum less than threshold")
    void testQueryBySumLessThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SumSpecification.lessThan(100);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by sum between min and max")
    void testQueryBySumBetween() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SumSpecification.between(100, 500);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by size greater than threshold")
    void testQueryBySizeGreaterThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SizeSpecification.greaterThan(3);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should query collections by size less than threshold")
    void testQueryBySizeLessThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SizeSpecification.lessThan(4);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array4.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by size equal to value")
    void testQueryBySizeEqualTo() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SizeSpecification.equalTo(5);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should query collections by size between min and max")
    void testQueryBySizeBetween() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = SizeSpecification.between(2, 4);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array4.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by average greater than threshold")
    void testQueryByAverageGreaterThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = AverageSpecification.greaterThan(50);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array4.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by average between min and max")
    void testQueryByAverageBetween() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = AverageSpecification.between(10, 100);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by min greater than threshold")
    void testQueryByMinGreaterThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = MinSpecification.greaterThan(50);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(1, result.size());
        assertEquals(array4.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("Should query collections by max less than threshold")
    void testQueryByMaxLessThan() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        Specification spec = MaxSpecification.lessThan(100);
        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.query(spec);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should combine multiple specifications using stream filter")
    void testCombineSpecifications() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array4);

        List<com.inno.course.entity.AbstractNumericArray<?>> result = repository.findAll().stream()
                .filter(SumSpecification.greaterThan(100)::isSatisfiedBy)
                .filter(SizeSpecification.greaterThanOrEqual(5)::isSatisfiedBy)
                .collect(Collectors.toList());

        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("Should update collection in repository")
    void testUpdateCollection() {
        repository.add(array1);

        array1.setElement(0, 999);
        repository.update(array1);

        NumericArray<?> updated = (NumericArray<?>) repository.findById(array1.getId());
        assertEquals(999, updated.getElement(0));
    }

    @Test
    @DisplayName("Should not update non-existent collection")
    void testUpdateNonExistentCollection() {
        NumericArray<Integer> newArray = new NumericArray<>(new Integer[]{1, 2, 3});

        repository.update(newArray);

        assertEquals(0, repository.size());
        assertNull(repository.findById(newArray.getId()));
    }

    // ==================== SIZE AND CLEAR TESTS ====================

    @Test
    @DisplayName("Should return correct repository size")
    void testRepositorySize() {
        assertEquals(0, repository.size());

        repository.add(array1);
        assertEquals(1, repository.size());

        repository.add(array2);
        assertEquals(2, repository.size());

        repository.remove(array1.getId());
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should clear repository")
    void testClearRepository() {
        repository.add(array1);
        repository.add(array2);
        repository.add(array3);

        assertEquals(3, repository.size());

        repository.clear();

        assertEquals(0, repository.size());
        assertNull(repository.findById(array1.getId()));
        assertNull(repository.findById(array2.getId()));
        assertNull(repository.findById(array3.getId()));
    }

    // ==================== SINGLETON TESTS ====================

    @Test
    @DisplayName("Should return same instance of repository")
    void testSingletonInstance() {
        CollectionRepository instance1 = CollectionRepository.getInstance();
        CollectionRepository instance2 = CollectionRepository.getInstance();

        assertSame(instance1, instance2);
    }
}