package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import io.vavr.collection.Stream;
import io.vavr.control.Option;

public enum ParameterMode {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);

    private final int mode;

    ParameterMode(int mode) {
        this.mode = mode;
    }

    public static Option<ParameterMode> of(final int mode) {
        return Stream.of(ParameterMode.values())
                .filter(pm -> pm.mode == mode)
                .headOption();
    }
}
