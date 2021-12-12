package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.INSTRUCTION_POINTER_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JumpInstructionTest {

    private static final IntcodeComputerData DATA = new IntcodeComputerData();

    private static final JumpInstruction JUMP_IF_TRUE_NON_ZERO = ImmutableJumpInstruction.builder()
            .address(3)
            .opcode(Opcode.JUMP_IF_TRUE)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(5))
                            .mode(ParameterMode.IMMEDIATE)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.IMMEDIATE)
                            .build()))
            .build();

    private static final JumpInstruction JUMP_IF_TRUE_ZERO = ImmutableJumpInstruction.builder()
            .address(3)
            .opcode(Opcode.JUMP_IF_TRUE)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.ZERO)
                            .mode(ParameterMode.IMMEDIATE)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    private static final JumpInstruction JUMP_IF_FALSE_NON_ZERO = ImmutableJumpInstruction.builder()
            .address(2)
            .opcode(Opcode.JUMP_IF_FALSE)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(5))
                            .mode(ParameterMode.IMMEDIATE)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.POSITION)
                            .build()))
            .build();

    private static final JumpInstruction JUMP_IF_FALSE_ZERO = ImmutableJumpInstruction.builder()
            .address(2)
            .opcode(Opcode.JUMP_IF_FALSE)
            .parameters(List.of(
                    ImmutableParameter.builder()
                            .value(BigInteger.ZERO)
                            .mode(ParameterMode.IMMEDIATE)
                            .build(),
                    ImmutableParameter.builder()
                            .value(BigInteger.valueOf(9))
                            .mode(ParameterMode.IMMEDIATE)
                            .build()))
            .build();

    @Before
    public void setUp() {
        DATA.put(INSTRUCTION_POINTER_KEY, 1);
    }

    @Test
    public void testJumpIfTrueNonZero() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3, 3, 1105, 5, 9, 1101, 0, 0, 12, 4, 12, 99, 1));
        assertThat(JUMP_IF_TRUE_NON_ZERO.execute(DATA).intValue(), is(9));
        assertThat(DATA.get(INSTRUCTION_POINTER_KEY, Integer.class), is(9));
    }

    @Test
    public void testJumpIfTrueZero() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3,3,105,0,9,1101,0,0,12,4,12,99,1));
        assertThat(JUMP_IF_TRUE_ZERO.execute(DATA).intValue(), is(-1));
        assertThat(DATA.get(INSTRUCTION_POINTER_KEY, Integer.class), is(1));
    }

    @Test
    public void testJumpIfFalseNonZero() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3, 3, 1106, 5, 9, 1101, 0, 0, 12, 4, 12, 99, 1));
        assertThat(JUMP_IF_FALSE_NON_ZERO.execute(DATA).intValue(), is(-1));
        assertThat(DATA.get(INSTRUCTION_POINTER_KEY, Integer.class), is(1));
    }

    @Test
    public void testJumpIfFalseZero() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3,3,1106,0,9,1101,0,0,12,4,12,99,1));
        assertThat(JUMP_IF_FALSE_ZERO.execute(DATA).intValue(), is(9));
        assertThat(DATA.get(INSTRUCTION_POINTER_KEY, Integer.class), is(9));
    }

    @Test
    public void testGetIncrement() {
        DATA.put(MEMORY_KEY, bigIntegerInput(3, 3, 1105, 5, 9, 1101, 0, 0, 12, 4, 12, 99, 1));
        assertThat(JUMP_IF_TRUE_ZERO.getIncrement(), is(3));
        assertThat(JUMP_IF_FALSE_ZERO.getIncrement(), is(3));
    }
}
