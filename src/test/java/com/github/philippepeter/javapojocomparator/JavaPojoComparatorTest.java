package com.github.philippepeter.javapojocomparator;

import com.github.philippepeter.javapojocomparator.pojo.Pojo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Slf4j
class JavaPojoComparatorTest {
    @ParameterizedTest
    @EnumSource(PojoTests.class)
    void test(PojoTests pojoTests) {
        JavaPojoComparator<Pojo> javaPojoComparator = new JavaPojoComparator<>();

        PojoComparisons result = javaPojoComparator.compare(pojoTests.getReference(), pojoTests.getToCompare());
        log.info("Result is {}", result);

        Assertions.assertEquals(pojoTests.getExpected(), result);
    }
}
