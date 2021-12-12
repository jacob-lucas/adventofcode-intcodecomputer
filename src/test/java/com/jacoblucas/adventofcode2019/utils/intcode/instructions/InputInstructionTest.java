package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.RELATIVE_BASE_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InputInstructionTest {

    private static final InputInstruction SAVE = ImmutableInputInstruction.builder()
            .address(0)
            .opcode(Opcode.SAVE)
            .input(Option.of(BigInteger.valueOf(5)))
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.ZERO)
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    @Test
    public void testExecute() {
        final IntcodeComputerData data = new IntcodeComputerData();
        data.put(MEMORY_KEY, bigIntegerInput(3, 0, 4, 0, 99));
        assertThat(SAVE.execute(data), is(bigIntegerInput(5, 0, 4, 0, 99)));
    }

    @Test
    public void testExecuteWithRelative() {
        final int input = 4;
        final InputInstruction instruction = ImmutableInputInstruction.builder()
                .address(2)
                .opcode(Opcode.SAVE)
                .input(Option.of(BigInteger.valueOf(input)))
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(2))
                                .mode(ParameterMode.RELATIVE)
                                .relativeBase(5)
                                .build()))
                .build();
        final IntcodeComputerData data = new IntcodeComputerData();
        data.put(RELATIVE_BASE_KEY, input);
        data.put(MEMORY_KEY, bigIntegerInput(9, 5, 203, 2, 4, 0, 99));
        instruction.execute(data);

        final Map<BigInteger, BigInteger> memory = data.get(MEMORY_KEY, HashMap.class);
        assertThat(memory, is(bigIntegerInput(9, 5, 203, 2, 4, 0, 99, 4)));
        assertThat(memory.get(BigInteger.valueOf(7)).get().intValue(), is(input));
    }

    @Test
    public void testGetIncrement() {
        assertThat(SAVE.getIncrement(), is(2));
    }
}
