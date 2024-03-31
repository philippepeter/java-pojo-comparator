package com.github.philippepeter.javapojocomparator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class JavaPojoComparator<T> {

    private final Constructor<T> constructor;

    public JavaPojoComparator(Class<T> invalidPojoClass) {
        try {
            constructor = invalidPojoClass.getConstructor();
            constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new IllegalArgumentException("No empty constructor found", e);
        }
    }

    public T compare(T referenceValue, T toCompareValue) {
        return computeDiff(referenceValue, toCompareValue);
    }

    private static <U> U computeDiff(U value1, U value2) {
        if (value1 == null && value2 == null) {
            return null;
        }

        if (value1 != null && value1.equals(value2)) {
            return null;
        }

        return computeNoNullDiff(value1, value2);
    }

    private static <U> U computeNoNullDiff(U value1, U value2) {
        Class<U> notNullClass = getNotNullClass(value1, value2);
        U computedDiff = newInstanceOf(notNullClass);

        Field[] declaredFields = notNullClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            Object subValue1 = getFieldValueOfObject(declaredField, value1);
            Object subValue2 = getFieldValueOfObject(declaredField, value2);

            Object subDiff = null;
            if (subValue1 != null || subValue2 != null) {
                if (isJavaBase(getNotNullClass(subValue1, subValue2))) {
                    subDiff = subValue1 != subValue2 ? subValue2 : null;
                } else {
                    subDiff = computeDiff(subValue1, subValue2);
                }
            }

            setFieldValueOfObject(declaredField, computedDiff, subDiff);

        }
        return computedDiff;
    }

    private static <U> void setFieldValueOfObject(Field declaredField, U computedDiff, Object value) {
        String setter = fetchSetterMethod(declaredField);
        try {
            computedDiff.getClass().getMethod(setter, declaredField.getType()).invoke(computedDiff, value);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <U> Object getFieldValueOfObject(Field field, U object) {
        if (object == null) {
            return null;
        }

        String getter = fetchGetterMethod(field);
        try {
            return object.getClass().getMethod(getter).invoke(object);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <U> Class<U> getNotNullClass(U value1, U value2) {
        return value1 != null ? (Class<U>) value1.getClass() : (Class<U>) value2.getClass();
    }

    private static boolean isJavaBase(Class<?> declaringClass) {
        return declaringClass.equals(Integer.class) ||
                declaringClass.equals(Double.class) ||
                declaringClass.equals(Float.class) ||
                declaringClass.equals(Boolean.class) ||
                declaringClass.equals(String.class) ||
                declaringClass.equals(Character.class) ||
                declaringClass.equals(Short.class);

    }

    private static <U> U newInstanceOf(Class<U> classToInstantiate) {
        try {
            return classToInstantiate.getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("value of class " + classToInstantiate + " is not a valid JAVA POJO", e);
        }
    }

    protected static String fetchSetterMethod(Field declaredField) {
        String toReturn = "set";
        String name = declaredField.getName();
        String firstChar = name.substring(0, 1);
        return toReturn + name.replaceFirst(firstChar, firstChar.toUpperCase(Locale.ROOT));
    }

    protected static String fetchGetterMethod(Field declaredField) {
        String toReturn = "get";
        String name = declaredField.getName();
        String firstChar = name.substring(0, 1);
        return toReturn + name.replaceFirst(firstChar, firstChar.toUpperCase(Locale.ROOT));
    }
}
