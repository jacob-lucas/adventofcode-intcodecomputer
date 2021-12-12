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
public abstract class InputInstruction extends Instruction<Map<BigInteger, BigInteger>> {
    @Override
    public Map<BigInteger, BigInteger> execute(final IntcodeComputerData data) {
        final Parameter parameter = getParameters().get(0);
        BigInteger a = parameter.getValue();
        if (parameter.getMode() == ParameterMode.RELATIVE) {
            a = a.add(BigInteger.valueOf(parameter.getRelativeBase()));
        }

        Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        memory = memory.put(a, getInput().get());
        data.put(MEMORY_KEY, memory);
        return memory;
    }

    @Value.Check
    public void check()  {
        Preconditions.checkState(getOpcode() == Opcode.SAVE, "InputInstruction must have INPUT opcode");
        Preconditions.checkState(getParameters().size() == 1, "InputInstruction must contain only one parameter");
    }
}
