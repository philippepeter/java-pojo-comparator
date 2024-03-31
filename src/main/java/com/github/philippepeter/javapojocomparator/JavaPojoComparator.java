package com.github.philippepeter.javapojocomparator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JavaPojoComparator<T> {

    public PojoComparisons compare(T referenceValue, T toCompareValue) {
        List<PojoComparison> comparisonsResult = new ArrayList<>();
        computeDiff(referenceValue, toCompareValue, comparisonsResult, "");
        return new PojoComparisons(comparisonsResult);
    }

    private static <U> void computeDiff(U value1, U value2, List<PojoComparison> comparisons, String currentKey) {
        if (value1 == null && value2 == null) {
            return;
        }

        if (value1 != null && value1.equals(value2)) {
            return;
        }

        for (Field declaredField : getNotNullClass(value1, value2).getDeclaredFields()) {
            String subCurrentKey = currentKey + (currentKey.isEmpty() ? "" : ".") + declaredField.getName();
            processField(value1, value2, declaredField, comparisons, subCurrentKey);
        }
    }


    private static <U> void processField(U value1, U value2, Field declaredField, List<PojoComparison> comparisons, String currentKey) {
        Object subValue1 = getFieldValueOfObject(declaredField, value1);
        Object subValue2 = getFieldValueOfObject(declaredField, value2);

        if (subValue1 == null && subValue2 == null) {
            return;
        }

        if (subValue1 != null && subValue1.equals(subValue2)) {
            return;
        }

        if (isJavaBase(getNotNullClass(subValue1, subValue2))) {
            comparisons.add(new PojoComparison(currentKey, subValue2));
        } else {
            computeDiff(subValue1, subValue2, comparisons, currentKey);
        }
    }

    private static <U> Object getFieldValueOfObject(Field field, U object) {
        if (object == null) {
            return null;
        }
        try {
            return object.getClass().getMethod(fetchGetterMethod(field)).invoke(object);
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
                declaringClass.equals(Short.class) ||
                declaringClass.equals(int.class) ||
                declaringClass.equals(double.class) ||
                declaringClass.equals(float.class) ||
                declaringClass.equals(boolean.class) ||
                declaringClass.equals(short.class) ||
                declaringClass.equals(char.class);

    }

    protected static String fetchGetterMethod(Field declaredField) {
        String toReturn = declaredField.getType().equals(boolean.class) ? "is" : "get";
        String name = declaredField.getName();
        String firstChar = name.substring(0, 1);
        return toReturn + name.replaceFirst(firstChar, firstChar.toUpperCase(Locale.ROOT));
    }
}
