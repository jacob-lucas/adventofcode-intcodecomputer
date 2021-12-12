package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.google.common.base.Preconditions;
import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.immutables.value.Value;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.INSTRUCTION_POINTER_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;

@Value.Immutable
public abstract class JumpInstruction extends Instruction<BigInteger> {
    @Override
    public BigInteger execute(final IntcodeComputerData data) {
        final Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        final List<Parameter> parameters = getParameters();
        final Parameter p1 = parameters.get(0);
        final Parameter p2 = parameters.get(1);

        final BigInteger result = getOpcode().apply(p1.resolve(memory), p2.resolve(memory));
        if (BigInteger.ZERO.compareTo(result) <= 0) {
            data.put(INSTRUCTION_POINTER_KEY, result.intValue());
        }

        return result;
    }

    @Value.Check
    public void check() {
        Preconditions.checkState(List.of(Opcode.JUMP_IF_TRUE, Opcode.JUMP_IF_FALSE).contains(getOpcode()), "JumpInstruction must have be either opcode JUMP_IF_TRUE or JUMP_IF_FALSE");
        Preconditions.checkState(getParameters().size() == 2, "JumpInstruction must contain only two parameters");
    }
}
