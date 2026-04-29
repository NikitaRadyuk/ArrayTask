package com.inno.course.repository;

import com.inno.course.entity.AbstractNumericArray;
import com.inno.course.entity.NumericArray;
import com.inno.course.repository.impl.CollectionRepository;
import com.inno.course.repository.specification.*;
import com.inno.course.repository.specification.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Specifications Tests")
public class SpecificationsTest {

    private Repository repository;
    private NumericArray<Integer> array1;
    private NumericArray<Integer> array2;
    private NumericArray<Integer> array3;

    @BeforeEach
    void setUp() {
        repository = CollectionRepository.getInstance();
        repository.clear();

        // array1: min=1, max=5, sum=15, size=5, avg=3
        array1 = new NumericArray<>(new Integer[]{1, 2, 3, 4, 5});
        // array2: min=10, max=50, sum=150, size=5, avg=30
        array2 = new NumericArray<>(new Integer[]{10, 20, 30, 40, 50});
        // array3: min=100, max=300, sum=600, size=3, avg=200
        array3 = new NumericArray<>(new Integer[]{100, 200, 300});

        repository.add(array1);
        repository.add(array2);
        repository.add(array3);
    }


    @Test
    @DisplayName("SumSpecification - greater than")
    void testSumGreaterThan() {
        Specification spec = SumSpecification.greaterThan(100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("SumSpecification - less than")
    void testSumLessThan() {
        Specification spec = SumSpecification.lessThan(100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("SumSpecification - equal to")
    void testSumEqualTo() {
        Specification spec = SumSpecification.equalTo(150);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("SumSpecification - between")
    void testSumBetween() {
        Specification spec = SumSpecification.between(100, 500);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("SizeSpecification - greater than")
    void testSizeGreaterThan() {
        Specification spec = SizeSpecification.greaterThan(3);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("SizeSpecification - less than")
    void testSizeLessThan() {
        Specification spec = SizeSpecification.lessThan(4);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array3.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("SizeSpecification - equal to")
    void testSizeEqualTo() {
        Specification spec = SizeSpecification.equalTo(5);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("SizeSpecification - between")
    void testSizeBetween() {
        Specification spec = SizeSpecification.between(2, 4);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array3.getId(), result.get(0).getId());
    }


    @Test
    @DisplayName("AverageSpecification - greater than")
    void testAverageGreaterThan() {
        Specification spec = AverageSpecification.greaterThan(100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array3.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("AverageSpecification - less than")
    void testAverageLessThan() {
        Specification spec = AverageSpecification.lessThan(10);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("AverageSpecification - between")
    void testAverageBetween() {
        Specification spec = AverageSpecification.between(25, 35);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("MinSpecification - greater than")
    void testMinGreaterThan() {
        Specification spec = MinSpecification.greaterThan(50);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array3.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("MinSpecification - less than")
    void testMinLessThan() {
        Specification spec = MinSpecification.lessThan(10);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array1.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("MinSpecification - between")
    void testMinBetween() {
        Specification spec = MinSpecification.between(5, 20);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("MaxSpecification - greater than")
    void testMaxGreaterThan() {
        // array1 max=5, array2 max=50, array3 max=300
        Specification spec = MaxSpecification.greaterThan(100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array3.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("MaxSpecification - less than")
    void testMaxLessThan() {
        // array1 max=5, array2 max=50, array3 max=300
        Specification spec = MaxSpecification.lessThan(100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("MaxSpecification - between")
    void testMaxBetween() {
        Specification spec = MaxSpecification.between(10, 100);
        List<AbstractNumericArray<?>> result = repository.query(spec);
        assertEquals(1, result.size());
        assertEquals(array2.getId(), result.get(0).getId());
    }
}