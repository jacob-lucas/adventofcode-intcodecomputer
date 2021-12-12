package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.RELATIVE_BASE_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MemoryUpdateInstructionTest {

    private static final IntcodeComputerData DATA = new IntcodeComputerData();

    private static final ImmutableMemoryUpdateInstruction ADD = ImmutableMemoryUpdateInstruction.builder()
            .address(0)
            .opcode(Opcode.ADD)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(4))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(3))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(4))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    private static final ImmutableMemoryUpdateInstruction MULTIPLY = ImmutableMemoryUpdateInstruction.builder()
            .address(0)
            .opcode(Opcode.MULTIPLY)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(4))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(3))
                            .mode(ParameterMode.IMMEDIATE)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(4))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    private static final ImmutableMemoryUpdateInstruction LESS_THAN = ImmutableMemoryUpdateInstruction.builder()
            .address(2)
            .opcode(Opcode.LESS_THAN)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(10))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    private static final ImmutableMemoryUpdateInstruction EQUALS = ImmutableMemoryUpdateInstruction.builder()
            .address(2)
            .opcode(Opcode.EQUALS)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(10))
                            .mode(ParameterMode.POSITION)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    @Test
    public void testAdd() {
        DATA.put(MEMORY_KEY, bigIntegerInput(1, 4, 3, 4, 33));
        assertThat(ADD.execute(DATA), is(bigIntegerInput(1,4,3,4,37)));
    }

    @Test
    public void testAddRelativeMode() {
        DATA.put(RELATIVE_BASE_KEY, 1);
        DATA.put(MEMORY_KEY, bigIntegerInput(21101, 4, 3, 4, 33));
        final Instruction instruction = ImmutableMemoryUpdateInstruction.builder()
                .address(0)
                .opcode(Opcode.ADD)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(4))
                                .mode(ParameterMode.IMMEDIATE)
                                .build(),
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(3))
                                .mode(ParameterMode.IMMEDIATE)
                                .build(),
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(4))
                                .mode(ParameterMode.RELATIVE)
                                .relativeBase(1)
                                .build()))
                .build();
        assertThat(instruction.execute(DATA), is(bigIntegerInput(21101,4,3,4,33,7)));
    }

    @Test
    public void testMultiply() {
        DATA.put(MEMORY_KEY, bigIntegerInput(1002, 4, 3, 4, 33));
        assertThat(MULTIPLY.execute(DATA), is(bigIntegerInput(1002,4,3,4,99)));
    }

    @Test
    public void testLessThan() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3,9,7,9,10,9,4,9,99,-1,8));
        assertThat(LESS_THAN.execute(DATA), is(bigIntegerInput(3,9,7,9,10,9,4,9,99,1,8)));
    }

    @Test
    public void testEquals() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3,9,8,9,10,9,4,9,99,-1,8));
        assertThat(EQUALS.execute(DATA), is(bigIntegerInput(3,9,8,9,10,9,4,9,99,0,8)));
    }

    @Test
    public void testGetIncrement() {
        assertThat(ADD.getIncrement(), is(4));
        assertThat(MULTIPLY.getIncrement(), is(4));
        assertThat(LESS_THAN.getIncrement(), is(4));
        assertThat(EQUALS.getIncrement(), is(4));
    }
}
