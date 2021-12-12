package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.google.common.base.Preconditions;
import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import org.immutables.value.Value;

@Value.Immutable
public abstract class HaltInstruction extends Instruction<Integer> {
    @Override
    public Integer execute(final IntcodeComputerData data) {
        // do nothing
        return 0;
    }

    @Value.Check
    public void check() {
        Preconditions.checkState(getOpcode() == Opcode.HALT, "HaltInstruction must have HALT opcode");
        Preconditions.checkState(getParameters().isEmpty(), "HaltInstruction must contain zero parameters");
    }
}
