package com.github.philippepeter.javapojocomparator;

import com.github.philippepeter.javapojocomparator.pojo.Pojo;
import com.github.philippepeter.javapojocomparator.pojo.PojoSubValue1;
import com.github.philippepeter.javapojocomparator.pojo.PojoSubValue2;
import lombok.Getter;

@Getter
public enum PojoTests {
    TWO_EMPTY(new Pojo(), new Pojo(), null),
    NO_DIFF(createFullPojo(1, "1", 1.1, true), createFullPojo(1, "1", 1.1, true), null),
    ONE_DIFF(createFullPojo(1, "1", 1.1, true), createFullPojo(2, "1", 1.1, true), createFullPojo(2, null, null, null));

    private final Pojo reference;
    private final Pojo toCompare;
    private final Pojo expected;

    PojoTests(Pojo reference, Pojo toCompare, Pojo expected) {
        this.reference = reference;
        this.toCompare = toCompare;
        this.expected = expected;
    }

    private static Pojo createFullPojo(Integer sub1Value1, String sub1value2, Double sub2Value1, Boolean sub2Value2) {
        Pojo pojo = new Pojo();
        if (sub1Value1 != null || sub1value2 != null) {
            PojoSubValue1 pojoTestSubValue1 = new PojoSubValue1();
            pojoTestSubValue1.setValue1(sub1Value1);
            pojoTestSubValue1.setValue2(sub1value2);
            pojo.setPojoTestSubValue1(pojoTestSubValue1);
        }

        if (sub2Value1 != null || sub2Value2 != null) {
            PojoSubValue2 pojoTestSubValue2 = new PojoSubValue2();
            pojoTestSubValue2.setValue1(sub2Value1);
            pojoTestSubValue2.setValue2(sub2Value2);
            pojo.setPojoTestSubValue2(pojoTestSubValue2);
        }

        return pojo;
    }
}
