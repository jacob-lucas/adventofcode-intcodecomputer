package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.google.common.base.Preconditions;
import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.immutables.value.Value;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.RELATIVE_BASE_KEY;

@Value.Immutable
public abstract class RelativeBaseOffsetInstruction extends Instruction<BigInteger> {
    @Override
    public BigInteger execute(final IntcodeComputerData data) {
        final Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        final Integer relativeBase = data.get(RELATIVE_BASE_KEY, Integer.class);
        final Integer value = getParameters().get(0).resolve(memory).intValue();

        final int updated = value + relativeBase;
        data.put(RELATIVE_BASE_KEY, updated);
        return BigInteger.valueOf(updated);
    }

    @Value.Check
    public void check() {
        Preconditions.checkState(getOpcode() == Opcode.RELATIVE_BASE_OFFSET, "RelativeBaseOffsetInstruction must have RELATIVE_BASE_OFFSET opcode");
        Preconditions.checkState(getParameters().size() == 1, "RelativeBaseOffsetInstruction must contain only one parameter");
    }
}
