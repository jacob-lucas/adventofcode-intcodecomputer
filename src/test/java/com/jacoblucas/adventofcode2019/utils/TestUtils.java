package com.jacoblucas.adventofcode2019.utils;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

import java.math.BigInteger;

public final class TestUtils {

    public static Map<BigInteger, BigInteger> bigIntegerInput(final Integer... ints) {
        Map<BigInteger, BigInteger> program = HashMap.empty();
        for (int i=0; i<ints.length; i++) {
            program = program.put(BigInteger.valueOf(i), BigInteger.valueOf(ints[i]));
        }
        return program;
    }

}
