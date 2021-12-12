package com.jacoblucas.adventofcode2019.utils.intcode;

import io.vavr.control.Option;
import org.junit.Test;

import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.ADD;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.EQUALS;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.HALT;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.JUMP_IF_FALSE;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.JUMP_IF_TRUE;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.LESS_THAN;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.MULTIPLY;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.OUTPUT;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.RELATIVE_BASE_OFFSET;
import static com.jacoblucas.adventofcode2019.utils.intcode.Opcode.SAVE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OpcodeTest {

    @Test
    public void returnsOpcodeForCode() {
        assertThat(Opcode.of(1), is(Option.some(ADD)));
        assertThat(Opcode.of(2), is(Option.some(MULTIPLY)));
        assertThat(Opcode.of(3), is(Option.some(SAVE)));
        assertThat(Opcode.of(4), is(Option.some(OUTPUT)));
        assertThat(Opcode.of(5), is(Option.some(JUMP_IF_TRUE)));
        assertThat(Opcode.of(6), is(Option.some(JUMP_IF_FALSE)));
        assertThat(Opcode.of(7), is(Option.some(LESS_THAN)));
        assertThat(Opcode.of(8), is(Option.some(EQUALS)));
        assertThat(Opcode.of(9), is(Option.some(RELATIVE_BASE_OFFSET)));
        assertThat(Opcode.of(99), is(Option.some(HALT)));
        assertThat(Opcode.of(100), is(Option.none()));
    }

}
