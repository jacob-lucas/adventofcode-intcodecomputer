package com.jacoblucas.adventofcode2019.utils.intcode;

import com.jacoblucas.adventofcode2019.utils.intcode.instructions.ImmutableInputInstruction;
import com.jacoblucas.adventofcode2019.utils.intcode.instructions.InputInstruction;
import com.jacoblucas.adventofcode2019.utils.intcode.instructions.Instruction;
import com.jacoblucas.adventofcode2019.utils.intcode.instructions.InstructionFactory;
import com.jacoblucas.adventofcode2019.utils.intcode.instructions.OutputInstruction;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import io.vavr.collection.Stream;
import io.vavr.control.Try;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.INSTRUCTION_POINTER_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.MEMORY_KEY;
import static com.jacoblucas.adventofcode2019.utils.intcode.IntcodeComputerData.RELATIVE_BASE_KEY;
import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

public class IntcodeComputer {
    private static final int CONTINUE = 0;
    private static final int BREAK = 1;

    private BlockingQueue<BigInteger> input;
    private List<BigInteger> output;
    private List<IntcodeComputerOutputReceiver> receivers = List.empty();
    private IntcodeComputerData data = new IntcodeComputerData();

    public void feed(String arr) {
        final List<BigInteger> ints = List.of(arr.split(","))
                .flatMap(Stream::of)
                .map(BigInteger::new)
                .toList();
        Map<BigInteger, BigInteger> program = HashMap.empty();
        for (int i=0; i<ints.size(); i++) {
            program = program.put(BigInteger.valueOf(i), ints.get(i));
        }
        feed(program);
    }

    public void feed(final Map<BigInteger, BigInteger> program) {
        feed(program, Queue.empty());
    }

    public void feed(Map<BigInteger, BigInteger> program, final Queue<BigInteger> input) {
        this.output = List.empty();
        this.input = new LinkedBlockingQueue<>();
        this.input.addAll(input.toJavaList());

        data.put(RELATIVE_BASE_KEY, 0);
        data.put(INSTRUCTION_POINTER_KEY, 0);
        data.put(MEMORY_KEY, program);
    }

    public void subscribe(final IntcodeComputerOutputReceiver receiver) {
        if (receivers.find(r -> r.id().equals(receiver.id())).isEmpty()) {
            receivers = receivers.append(receiver);
//            System.out.println(String.format("[%s] Added subscriber [%s]", Thread.currentThread().getName(), receiver.id()));
        }
    }

    private void publish(final BigInteger output) {
        receivers.forEach(r -> {
            r.receive(output);
//            System.out.println(String.format("[%s] Published output [%d] to subscriber [%s]", Thread.currentThread().getName(), output, r.id()));
        });
    }

    public void receiveInput(final BigInteger input) {
        this.input.add(input);
    }

    public BigInteger getOutput() {
        return output.isEmpty() ? getMemory().get(BigInteger.ZERO).get() : output.last();
    }

    public IntcodeComputer execute() {
//        System.out.println(String.format("[%s] begin - queue:%s", Thread.currentThread().getName(), input));
        int result = CONTINUE;
        while (result == CONTINUE) {
            final int instructionPointer = getInstructionPointer();
            final Try<Instruction> instruction = InstructionFactory.at(instructionPointer, getMemory(), none(), getRelativeBase());
            if (instruction.isFailure()) {
                instruction.getCause().printStackTrace();
                result = BREAK;
            } else {
                final Try<Integer> resultTry = instruction.map(this::execute);
                if (resultTry.isSuccess()) {
                    result = resultTry.get();
                } else {
                    System.out.println(String.format("[pos=%d] Failed to execute %s", instructionPointer, instruction));
                    resultTry.getCause().printStackTrace();
                    result = BREAK;
                }
            }
        }
//        System.out.println(String.format("[%s] pos=%d end", Thread.currentThread().getName(), getInstructionPointer()));
        return this;
    }

    private int execute(Instruction instruction) {
        final int currentInstructionPointer = getInstructionPointer();
//        System.out.println(String.format("[pos=%d] Before: %s", currentInstructionPointer, getMemory()));

//        System.out.println(String.format("[%s] [pos=%d] Executing %s", Thread.currentThread().getName(), currentInstructionPointer, instruction));

        if (instruction.getOpcode() == Opcode.HALT) {
            return BREAK;
        } else if (instruction instanceof InputInstruction && instruction.getInput().isEmpty()) {
//            System.out.println(String.format("[%s] Awaiting input for instruction (ptr=%d) %s", Thread.currentThread().getName(), currentInstructionPointer, instruction));
            try {
                instruction = ImmutableInputInstruction.copyOf((InputInstruction)instruction).withInput(some(input.take()));
            } catch (InterruptedException e) {
                return BREAK;
            }
//            System.out.println(String.format("[%s] Input received = %d", Thread.currentThread().getName(), instruction.getInput().get()));
        }

        final Object result = instruction.execute(data);

        if (instruction instanceof OutputInstruction) {
            output = output.append((BigInteger)result);
            publish((BigInteger)result);
//            System.out.println(String.format("[%s] %s OUTPUT=%s", Thread.currentThread().getName(), instruction, result.toString()));
        }

        final int instructionPointer = getInstructionPointer();
        if (instructionPointer == currentInstructionPointer) {
            // only increment if an instruction has not adjusted the instruction pointer
            data.put(INSTRUCTION_POINTER_KEY, instructionPointer + instruction.getIncrement());
        }

//        System.out.println(String.format("[pos=%d] After: %s", currentInstructionPointer, getMemory()));
        return CONTINUE;
    }

    private int getInstructionPointer() {
        return data.get(INSTRUCTION_POINTER_KEY, Integer.class);
    }

    Map<BigInteger, BigInteger> getMemory() {
        return data.get(MEMORY_KEY, HashMap.class);
    }

    private int getRelativeBase() {
        return data.get(RELATIVE_BASE_KEY, Integer.class);
    }

    List<IntcodeComputerOutputReceiver> getReceivers() {
        return receivers;
    }
}
