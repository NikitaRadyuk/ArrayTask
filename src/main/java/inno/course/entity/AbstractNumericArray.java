package main.java.inno.course.entity;

public abstract class AbstractNumericArray<T extends Number> {
    protected T[] array;

    public AbstractNumericArray(T[] array) {
        this.array = array.clone();
    }

    public T[] getArray() {
        return array.clone();
    }

    public int length() {
        return array.length;
    }

    public double getAsDouble(int index){
        return array[index].doubleValue();
    }
}
