package com.github.philippepeter.javapojocomparator;

import com.github.philippepeter.javapojocomparator.pojo.Pojo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class JavaPojoComparatorTest {
    @ParameterizedTest
    @EnumSource(PojoTests.class)
    void test(PojoTests pojoTests) {
        JavaPojoComparator<Pojo> javaPojoComparator = new JavaPojoComparator<>();

        PojoComparisons result = javaPojoComparator.compare(pojoTests.getReference(), pojoTests.getToCompare());

        Assertions.assertEquals(pojoTests.getExpected(), result);
    }
}
