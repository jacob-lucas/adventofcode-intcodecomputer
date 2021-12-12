package com.jacoblucas.adventofcode2019.utils.intcode;

import java.math.BigInteger;

public interface IntcodeComputerOutputReceiver {
    String id();

    void receive(final BigInteger input);
}
