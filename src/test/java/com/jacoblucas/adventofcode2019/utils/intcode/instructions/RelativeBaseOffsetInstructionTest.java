package com.jacoblucas.adventofcode2019.utils.intcode.instructions;

import com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData;
import com.jacoblucas.adventofcode2019.utils.intcode.Opcode;
import io.vavr.collection.List;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.RELATIVE_BASE_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RelativeBaseOffsetInstructionTest {

    private static final IntcodeComputerData DATA = new IntcodeComputerData();

    @Before
    public void setUp() {
        DATA.put(RELATIVE_BASE_KEY, 1);
    }

    @Test
    public void testRelativeBaseOffsetPositionMode() {
        final Instruction<BigInteger> rboInstruction = ImmutableRelativeBaseOffsetInstruction.builder()
                .address(0)
                .opcode(Opcode.RELATIVE_BASE_OFFSET)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(3))
                                .mode(ParameterMode.POSITION)
                                .build()))
                .build();
        DATA.put(MEMORY_KEY, bigIntegerInput(9,3,99,5));
        assertThat(rboInstruction.execute(DATA).intValue(), is(6));
        assertThat(DATA.get(RELATIVE_BASE_KEY, Integer.class), is(6));
    }

    @Test
    public void testRelativeBaseOffsetImmediateMode() {
        final Instruction<BigInteger> rboInstruction = ImmutableRelativeBaseOffsetInstruction.builder()
                .address(0)
                .opcode(Opcode.RELATIVE_BASE_OFFSET)
                .parameters(List.of(
                        ImmutableParameter.builder()
                                .value(BigInteger.valueOf(3))
                                .mode(ParameterMode.IMMEDIATE)
                                .build()))
                .build();
        DATA.put(MEMORY_KEY, bigIntegerInput(9,3,99,5));
        assertThat(rboInstruction.execute(DATA).intValue(), is(4));
        assertThat(DATA.get(RELATIVE_BASE_KEY, Integer.class), is(4));
    }

}
