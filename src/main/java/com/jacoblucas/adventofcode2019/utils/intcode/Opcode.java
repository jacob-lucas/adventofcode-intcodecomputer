package com.jacoblucas.adventofcode2019.utils.intcode;

import io.vavr.Function2;
import io.vavr.collection.Stream;
import io.vavr.control.Option;

import java.math.BigInteger;

public enum Opcode {
    ADD(1, BigInteger::add),
    MULTIPLY(2, BigInteger::multiply),
    SAVE(3, null),
    OUTPUT(4, null),
    JUMP_IF_TRUE(5, (a, b) -> a.equals(BigInteger.ZERO) ? BigInteger.valueOf(-1) : b),
    JUMP_IF_FALSE(6, (a, b) -> a.equals(BigInteger.ZERO) ? b : BigInteger.valueOf(-1)),
    LESS_THAN(7, (a, b) -> a.compareTo(b) < 0 ? BigInteger.ONE : BigInteger.ZERO),
    EQUALS(8, (a, b) -> a.equals(b) ? BigInteger.ONE : BigInteger.ZERO),
    RELATIVE_BASE_OFFSET(9, null),
    HALT(99, null);

    private final int code;
    private final Function2<BigInteger, BigInteger, BigInteger> func;

    Opcode(final int code, final Function2<BigInteger, BigInteger, BigInteger> func) {
        this.code = code;
        this.func = func;
    }

    public BigInteger apply(final BigInteger a, final BigInteger b) {
        return func.apply(a, b);
    }

    public static Option<Opcode> of(final int code) {
        return Stream.of(Opcode.values())
                .filter(o -> o.code == code)
                .headOption();
    }
}
