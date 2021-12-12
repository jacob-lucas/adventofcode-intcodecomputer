package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.google.common.base.Preconditions;
import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import org.immutables.value.Value;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;

@Value.Immutable
public abstract class OutputInstruction extends Instruction<BigInteger> {
    @Override
    public BigInteger execute(final IntcodeComputerData data) {
        final Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        return getParameters().get(0).resolve(memory);
    }

    @Value.Check
    public void check() {
        Preconditions.checkState(getOpcode() == Opcode.OUTPUT, "OutputInstruction must have OUTPUT opcode");
        Preconditions.checkState(getParameters().size() == 1, "OutputInstruction must contain only one parameter");
    }
}
