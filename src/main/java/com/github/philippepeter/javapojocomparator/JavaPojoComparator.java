package com.github.philippepeter.javapojocomparator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class JavaPojoComparator<T> {

    private final Constructor<T> constructor;
    private final T result;

    public JavaPojoComparator(Class<T> invalidPojoClass) {
        try {
            constructor = invalidPojoClass.getConstructor();
            result = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("No empty constructor found", e);
        }

    }

    public T compare(T referenceValue, T toCompareValue) {
        return result;
    }


}
