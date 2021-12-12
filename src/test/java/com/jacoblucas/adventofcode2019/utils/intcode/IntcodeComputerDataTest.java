package com.jacoblucas.adventofcode2019.utils.intcode;

import org.junit.Before;
import org.junit.Test;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.INSTRUCTION_POINTER_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class IntcodeComputerDataTest {

    private IntcodeComputerData data;

    @Before
    public void setUp() {
        data = new IntcodeComputerData();
    }

    @Test
    public void testGet() {
        data.put(INSTRUCTION_POINTER_KEY, 3);
        assertThat(data.get(INSTRUCTION_POINTER_KEY, Integer.class), is(3));
    }

    @Test
    public void testPut() {
        final IntcodeComputerData settings = new IntcodeComputerData();
        final Integer result = settings.put(INSTRUCTION_POINTER_KEY, 3);
        assertThat(result, is(nullValue()));
    }

}
