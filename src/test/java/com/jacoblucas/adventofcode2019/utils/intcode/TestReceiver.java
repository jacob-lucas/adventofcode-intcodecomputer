package com.jacoblucas.adventofcode2019.utils.intcode;

import java.math.BigInteger;

class TestReceiver implements IntcodeComputerOutputReceiver {
    private BigInteger received;

    @Override
    public String id() {
        return "TR1";
    }

    @Override
    public void receive(BigInteger input) {
        received = input;
    }

    BigInteger getReceived() {
        return received;
    }
}
