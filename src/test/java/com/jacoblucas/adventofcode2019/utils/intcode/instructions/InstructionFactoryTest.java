package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InstructionFactoryTest {

    @Test
    public void testGetOpcode() {
        assertThat(InstructionFactory.getOpcode(1), is(Opcode.ADD));
        assertThat(InstructionFactory.getOpcode(101), is(Opcode.ADD));
        assertThat(InstructionFactory.getOpcode(1101), is(Opcode.ADD));
        assertThat(InstructionFactory.getOpcode(11101), is(Opcode.ADD));

        assertThat(InstructionFactory.getOpcode(2), is(Opcode.MULTIPLY));
        assertThat(InstructionFactory.getOpcode(3), is(Opcode.SAVE));
        assertThat(InstructionFactory.getOpcode(4), is(Opcode.OUTPUT));
        assertThat(InstructionFactory.getOpcode(5), is(Opcode.JUMP_IF_TRUE));
        assertThat(InstructionFactory.getOpcode(6), is(Opcode.JUMP_IF_FALSE));
        assertThat(InstructionFactory.getOpcode(7), is(Opcode.LESS_THAN));
        assertThat(InstructionFactory.getOpcode(8), is(Opcode.EQUALS));
        assertThat(InstructionFactory.getOpcode(99), is(Opcode.HALT));
    }

    @Test
    public void testGetMode() {
        assertThat(InstructionFactory.getMode(1, 1), is(ParameterMode.POSITION));
        assertThat(InstructionFactory.getMode(1, 2), is(ParameterMode.POSITION));
        assertThat(InstructionFactory.getMode(1, 3), is(ParameterMode.POSITION));

        assertThat(InstructionFactory.getMode(101, 1), is(ParameterMode.IMMEDIATE));
        assertThat(InstructionFactory.getMode(201, 1), is(ParameterMode.RELATIVE));
        assertThat(InstructionFactory.getMode(101, 2), is(ParameterMode.POSITION));
        assertThat(InstructionFactory.getMode(101, 3), is(ParameterMode.POSITION));

        assertThat(InstructionFactory.getMode(1101, 1), is(ParameterMode.IMMEDIATE));
        assertThat(InstructionFactory.getMode(1101, 2), is(ParameterMode.IMMEDIATE));
        assertThat(InstructionFactory.getMode(2101, 2), is(ParameterMode.RELATIVE));
        assertThat(InstructionFactory.getMode(1101, 3), is(ParameterMode.POSITION));

        assertThat(InstructionFactory.getMode(10001, 1), is(ParameterMode.POSITION));
        assertThat(InstructionFactory.getMode(10001, 2), is(ParameterMode.POSITION));
        assertThat(InstructionFactory.getMode(21101, 3), is(ParameterMode.RELATIVE));
    }

    @Test
    public void testAdd() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(1, 4, 3, 4, 33);
        final Instruction add = InstructionFactory.at(0, program1).get();
        assertThat(add, is(ImmutableMemoryUpdateInstruction.builder()
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
                .build()));
    }

    @Test
    public void testMultiply() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(2, 4, 3, 4, 33);
        final Instruction multiply = InstructionFactory.at(0, program1).get();
        assertThat(multiply, is(ImmutableMemoryUpdateInstruction.builder()
                .address(0)
                .opcode(Opcode.MULTIPLY)
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
                .build()));
    }

    @Test
    public void testSave() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3, 9, 8, 9, 10, 9, 4, 9, 99, 5, 8);
        final Option<BigInteger> input = Option.of(BigInteger.ONE);
        final Instruction save = InstructionFactory.at(0, program1, input).get();
        assertThat(save, is(ImmutableInputInstruction.builder()
                .address(0)
                .opcode(Opcode.SAVE)
                .input(input)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(9))
                                .mode(ParameterMode.POSITION)
                                .build()))
                .build()));
    }

    @Test
    public void testJumpIfTrue() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3, 3, 5, 5, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
        final Instruction jumpIfTrue = InstructionFactory.at(2, program1).get();
        assertThat(jumpIfTrue, is(ImmutableJumpInstruction.builder()
                .address(2)
                .opcode(Opcode.JUMP_IF_TRUE)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(5))
                                .mode(ParameterMode.POSITION)
                                .build(),
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(9))
                                .mode(ParameterMode.POSITION)
                                .build()))
                .build()));
    }

    @Test
    public void testJumpIfFalse() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3, 3, 6, 5, 9, 1101, 0, 0, 12, 4, 12, 99, 1);
        final Instruction jumpIfFalse = InstructionFactory.at(2, program1).get();
        assertThat(jumpIfFalse, is(ImmutableJumpInstruction.builder()
                .address(2)
                .opcode(Opcode.JUMP_IF_FALSE)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(5))
                                .mode(ParameterMode.POSITION)
                                .build(),
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(9))
                                .mode(ParameterMode.POSITION)
                                .build()))
                .build()));
    }

    @Test
    public void testLessThan() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3,9,7,9,10,9,4,9,99,-1,8);
        final Instruction lessThan = InstructionFactory.at(2, program1).get();
        assertThat(lessThan, is(ImmutableMemoryUpdateInstruction.builder()
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
                .build()));
    }

    @Test
    public void testEquals() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3,9,8,9,10,9,4,9,99,-1,8);
        final Instruction equals = InstructionFactory.at(2, program1).get();
        assertThat(equals, is(ImmutableMemoryUpdateInstruction.builder()
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
                .build()));
    }

    @Test
    public void testOutput() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3, 9, 8, 9, 10, 9, 4, 9, 99, 5, 8);
        final Instruction output = InstructionFactory.at(6, program1).get();
        assertThat(output, is(ImmutableOutputInstruction.builder()
                .address(6)
                .opcode(Opcode.OUTPUT)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(9))
                                .mode(ParameterMode.POSITION)
                                .build()))
                .build()));
    }

    @Test
    public void testRelativeBaseOffset() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99);
        final Instruction output = InstructionFactory.at(0, program1).get();
        assertThat(output, is(ImmutableRelativeBaseOffsetInstruction.builder()
                .address(0)
                .opcode(Opcode.RELATIVE_BASE_OFFSET)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.ONE)
                                .mode(ParameterMode.IMMEDIATE)
                                .build()))
                .build()));
    }

    @Test
    public void testHalt() {
        final Map<BigInteger, BigInteger> program1 = bigIntegerInput(3, 9, 8, 9, 10, 9, 4, 9, 99, 5, 8);
        final Instruction halt = InstructionFactory.at(8, program1).get();
        assertThat(halt, is(ImmutableHaltInstruction.builder()
                .address(8)
                .opcode(Opcode.HALT)
                .parameters(List.of())
                .build()));
    }
}
