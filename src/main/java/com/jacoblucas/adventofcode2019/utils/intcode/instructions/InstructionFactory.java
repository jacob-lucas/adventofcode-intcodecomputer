package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.math.BigInteger;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.isIn;

public class InstructionFactory {
    public static Try<Instruction> at(final int address, final Map<BigInteger, BigInteger> program) {
        return at(address, program, Option.none());
    }

    public static Try<Instruction> at(final int address, final Map<BigInteger, BigInteger> program, final Option<BigInteger> input) {
        return at(address, program, input, 0);
    }

    public static Try<Instruction> at(final int address, final Map<BigInteger, BigInteger> program, final Option<BigInteger> input, final int relativeBase) {
        return Try.of(() -> {
            final int instruction = program.get(BigInteger.valueOf(address)).get().intValue();
            final Opcode opcode = Opcode.of(instruction % 100).get();

            final int numExpectedParameters = Match(opcode).of(
                    Case($(isIn(Opcode.ADD, Opcode.MULTIPLY, Opcode.LESS_THAN, Opcode.EQUALS)), 3),
                    Case($(isIn(Opcode.JUMP_IF_TRUE, Opcode.JUMP_IF_FALSE)), 2),
                    Case($(isIn(Opcode.SAVE, Opcode.OUTPUT, Opcode.JUMP_IF_TRUE, Opcode.JUMP_IF_FALSE, Opcode.RELATIVE_BASE_OFFSET)), 1),
                    Case($(Opcode.HALT), 0));

            final List<Parameter> params = Stream.range(1, numExpectedParameters + 1)
                    .map(n -> getParameter(instruction, address, n, relativeBase, program))
                    .toList();

            return (Instruction) Match(opcode).of(
                    Case($(isIn(Opcode.ADD, Opcode.MULTIPLY, Opcode.LESS_THAN, Opcode.EQUALS)), () -> ImmutableMemoryUpdateInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .parameters(params)
                            .build()),
                    Case($(isIn(Opcode.JUMP_IF_TRUE, Opcode.JUMP_IF_FALSE)), () -> ImmutableJumpInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .parameters(params)
                            .build()),
                    Case($(Opcode.OUTPUT), () -> ImmutableOutputInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .parameters(params)
                            .build()),
                    Case($(Opcode.HALT), () -> ImmutableHaltInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .parameters(params)
                            .build()),
                    Case($(Opcode.SAVE), () -> ImmutableInputInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .input(input)
                            .parameters(params)
                            .build()),
                    Case($(Opcode.RELATIVE_BASE_OFFSET), () -> ImmutableRelativeBaseOffsetInstruction.builder()
                            .address(address)
                            .opcode(opcode)
                            .parameters(params)
                            .build()),
                    Case($(), () -> null));
        });
    }

    static Opcode getOpcode(final int instruction) {
        return Opcode.of(instruction % 100).get();
    }

    private static Parameter getParameter(
            final int instruction,
            final int address,
            final int parameterNumber,
            final int relativeBase,
            final Map<BigInteger, BigInteger> program
    ) {
        final BigInteger value = program.get(BigInteger.valueOf(address + parameterNumber)).get();
        final ParameterMode mode = getMode(instruction, parameterNumber);
        return ImmutableParameter.builder()
                .value(value)
                .mode(mode)
                .relativeBase(relativeBase)
                .build();
    }

    static ParameterMode getMode(final int instruction, final int parameterNumber) {
        final char[] mode = String.format("%05d", instruction).toCharArray();
        final int value = Character.getNumericValue(Match(parameterNumber).of(
                Case($(1), () -> mode[2]),
                Case($(2), () -> mode[1]),
                Case($(3), () -> mode[0])));
        return ParameterMode.of(value).get();
    }
}
