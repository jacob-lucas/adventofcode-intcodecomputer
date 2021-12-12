package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.google.common.base.Preconditions;
import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import org.immutables.value.Value;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;

@Value.Immutable
public abstract class MemoryUpdateInstruction extends Instruction<Map<BigInteger, BigInteger>> {
    @Override
    public Map<BigInteger, BigInteger> execute(final IntcodeComputerData data) {
        Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        final List<Parameter> parameters = getParameters();
        final Parameter p1 = parameters.get(0);
        final Parameter p2 = parameters.get(1);
        final Parameter p3 = parameters.get(2);

        BigInteger c = p3.getValue();
        if (p3.getMode() == ParameterMode.RELATIVE) {
            c = c.add(BigInteger.valueOf(p3.getRelativeBase()));
        }

        memory = memory.put(c, getOpcode().apply(p1.resolve(memory), p2.resolve(memory)));
        data.put(MEMORY_KEY, memory);
        return memory;
    }

    @Value.Check
    public void check() {
        Preconditions.checkState(List.of(Opcode.ADD, Opcode.MULTIPLY, Opcode.LESS_THAN, Opcode.EQUALS).contains(getOpcode()), "MemoryUpdateInstruction must have be either opcode ADD, MULTIPLY, LESS_THAN, or EQUALS");
        Preconditions.checkState(getParameters().size() == 3, "MemoryUpdateInstruction must contain only three parameters");
    }
}
