package com.github.philippepeter.javapojocomparator;

import com.github.philippepeter.javapojocomparator.pojo.InvalidPojo;
import com.github.philippepeter.javapojocomparator.pojo.Pojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JavaPojoComparatorTest {
    @ParameterizedTest
    @EnumSource(PojoTests.class)
    void test(PojoTests pojoTests) {
        JavaPojoComparator<Pojo> javaPojoComparator = new JavaPojoComparator<>(Pojo.class);

        Pojo result = javaPojoComparator.compare(pojoTests.getReference(), pojoTests.getToCompare());

        Assertions.assertEquals(pojoTests.getExpected(), result);
    }

    @Test
    void no_empty_constructor_should_throw_illegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new JavaPojoComparator<>(InvalidPojo.class));
    }
}
