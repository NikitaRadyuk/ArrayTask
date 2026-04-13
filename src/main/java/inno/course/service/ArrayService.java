package main.java.inno.course.service;

import main.java.inno.course.entity.AbstractNumericArray;

import java.util.OptionalDouble;
import java.util.Optional;

public class ArrayService {

    public static <T extends Number & Comparable<T>> Optional<T> findMin(AbstractNumericArray<T> array){
        if (array == null || array.length() == 0){
            return Optional.empty();
        }

        T min = array.getArray()[0];
        for (int i = 1; i < array.length(); i++) {
            if (array.getArray()[i].compareTo(min) < 0){
                min = array.getArray()[i];
            }
        }
        return Optional.of(min);
    }

    public static <T extends Number & Comparable<T>> Optional<T> findMax(AbstractNumericArray<T> array){
        if (array == null || array.length() == 0){
            return Optional.empty();
        }

        T max = array.getArray()[0];
        for (int i = 1; i < array.length(); i++) {
            if (array.getArray()[i].compareTo(max) > 0){
                max = array.getArray()[i];
            }
        }
        return Optional.of(max);
    }

    public static <T extends Number> OptionalDouble sum(AbstractNumericArray<T> array){
        if (array == null || array.length() == 0) {
            return OptionalDouble.empty();
        }

        double sum = 0.0;
        for (int i = 0; i < array.length(); i++) {
            sum += array.getAsDouble(i);
        }
        return OptionalDouble.of(sum);
    }

    public static <T extends Number> OptionalDouble average(AbstractNumericArray<T> array){
        OptionalDouble sum = sum(array);
        if (sum.isPresent() && array.length() > 0){
            return OptionalDouble.of(sum.getAsDouble() / array.length());
        }
        return OptionalDouble.empty();
    }
}
