package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import io.vavr.control.Option;
import org.immutables.value.Value;

import java.math.BigInteger;

public abstract class Instruction<T> {
    public abstract int getAddress();

    public abstract Opcode getOpcode();

    public abstract List<Parameter> getParameters();

    @Value.Default
    public Option<BigInteger> getInput() {
        return Option.none();
    }

    public abstract T execute(final IntcodeComputerData data);

    public Integer getIncrement() {
        return 1 + getParameters().size();
    }
}
