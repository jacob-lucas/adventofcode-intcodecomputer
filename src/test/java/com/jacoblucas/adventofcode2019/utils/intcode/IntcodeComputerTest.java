package com.jacoblucas.adventofcode2019.utils.intcode;

import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static com.jacoblucas.adventofcode2019.utils.TestUtils.bigIntegerInput;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IntcodeComputerTest {

    private IntcodeComputer computer;
    private Map<BigInteger, BigInteger> program;



    @Before
    public void setUp() {
        this.program = bigIntegerInput(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50);
        this.computer = new IntcodeComputer();
        computer.feed(program);
    }

    @Test
    public void executeExamples() {
        computer.feed(bigIntegerInput(1, 0, 0, 0, 99));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(2,0,0,0,99)));

        computer.feed(bigIntegerInput(2, 3, 0, 3, 99));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(2,3,0,6,99)));

        computer.feed(bigIntegerInput(2, 4, 4, 5, 99, 0));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(2,4,4,5,99,9801)));

        computer.feed(bigIntegerInput(1, 1, 1, 4, 99, 5, 6, 0, 99));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(30,1,1,4,2,5,6,0,99)));

        computer.feed(bigIntegerInput(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3500,9,10,70,2,3,11,0,99,30,40,50)));
    }

    @Test
    public void executeExamplesWithModes() {
        computer.feed(bigIntegerInput(3, 0, 4, 0, 99), Queue.of(BigInteger.valueOf(5)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(5,0,4,0,99)));
        assertThat(computer.getOutput().intValue(), is(5));

        computer.feed(bigIntegerInput(1002,4,3,4,33));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(1002,4,3,4,99)));
        assertThat(computer.getOutput().intValue(), is(1002));

        computer.feed(bigIntegerInput(109,1,204,-1,209,14,1001,15,2,16,1202,6,2,18,99,9,10,3,12));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(109,1,204,-1,209,14,1001,15,2,16,1202,6,2,18,99,9,11,3,22)));
        assertThat(computer.getOutput().intValue(), is(109));
    }

    @Test
    public void executeExamplesWithEquals() {
        computer.feed(bigIntegerInput(3,9,8,9,10,9,4,9,99,-1,8), Queue.of(BigInteger.valueOf(5)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,9,8,9,10,9,4,9,99,0,8)));
        assertThat(computer.getOutput().intValue(), is(0));

        computer.feed(bigIntegerInput(3,9,8,9,10,9,4,9,99,-1,8), Queue.of(BigInteger.valueOf(8)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,9,8,9,10,9,4,9,99,1,8)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,3,1108,-1,8,3,4,3,99), Queue.of(BigInteger.valueOf(8)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1108,1,8,3,4,3,99)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,3,1108,-1,8,3,4,3,99), Queue.of(BigInteger.valueOf(-1)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1108,0,8,3,4,3,99)));
        assertThat(computer.getOutput().intValue(), is(0));
    }

    @Test
    public void executeExamplesWithLessThan() {
        computer.feed(bigIntegerInput(3,9,7,9,10,9,4,9,99,-1,8), Queue.of(BigInteger.valueOf(5)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,9,7,9,10,9,4,9,99,1,8)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,9,7,9,10,9,4,9,99,-1,8), Queue.of(BigInteger.valueOf(18)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,9,7,9,10,9,4,9,99,0,8)));
        assertThat(computer.getOutput().intValue(), is(0));

        computer.feed(bigIntegerInput(3,3,1107,-1,8,3,4,3,99), Queue.of(BigInteger.valueOf(-3)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1107,1,8,3,4,3,99)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,3,1107,-1,8,3,4,3,99), Queue.of(BigInteger.valueOf(10)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1107,0,8,3,4,3,99)));
        assertThat(computer.getOutput().intValue(), is(0));
    }

    @Test
    public void executeExamplesWithJumpIfTrue() {
        computer.feed(bigIntegerInput(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), Queue.of(BigInteger.valueOf(5)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1105,5,9,1101,0,0,12,4,12,99,1)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), Queue.of(BigInteger.ZERO));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,3,1105,0,9,1101,0,0,12,4,12,99,0)));
        assertThat(computer.getOutput().intValue(), is(0));
    }

    @Test
    public void executeExamplesWithJumpIfFalse() {
        computer.feed(bigIntegerInput(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), Queue.of(BigInteger.valueOf(5)));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,12,6,12,15,1,13,14,13,4,13,99,5,1,1,9)));
        assertThat(computer.getOutput().intValue(), is(1));

        computer.feed(bigIntegerInput(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), Queue.of(BigInteger.ZERO));
        assertThat(computer.execute().getMemory(), is(bigIntegerInput(3,12,6,12,15,1,13,14,13,4,13,99,0,0,1,9)));
        assertThat(computer.getOutput().intValue(), is(0));
    }

    @Test
    public void executeLongerExampleBelowEight() {
        final Map<BigInteger, BigInteger> memory = bigIntegerInput(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        computer.feed(memory, Queue.of(BigInteger.valueOf(5)));
        computer.execute();

        assertThat(computer.getOutput().intValue(), is(999));
    }

    @Test
    public void executeLongerExampleEqualEight() {
        final Map<BigInteger, BigInteger> memory = bigIntegerInput(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        computer.feed(memory, Queue.of(BigInteger.valueOf(8)));
        computer.execute();

        assertThat(computer.getOutput().intValue(), is(1000));
    }

    @Test
    public void executeLongerExampleAboveEight() {
        final Map<BigInteger, BigInteger> memory = bigIntegerInput(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        computer.feed(memory, Queue.of(BigInteger.valueOf(100)));
        computer.execute();

        assertThat(computer.getOutput().intValue(), is(1001));
    }

    @Test
    public void testBlocksDoubleSubscription() {
        final TestReceiver receiver = new TestReceiver();
        computer.subscribe(receiver);
        computer.subscribe(receiver);

        assertThat(computer.getReceivers().size(), is(1));
    }

    @Test
    public void testOutputSubscription() {
        final Map<BigInteger, BigInteger> memory = bigIntegerInput(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        computer.feed(memory, Queue.of(BigInteger.valueOf(100)));

        final TestReceiver receiver = new TestReceiver();
        computer.subscribe(receiver);

        computer.execute();

        assertThat(receiver.getReceived().intValue(), is(1001));
    }

    @Test(timeout = 500)
    public void testAwaitsInput() {
        final Map<BigInteger, BigInteger> memory = bigIntegerInput(
                3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99);
        computer.feed(memory); // no input

        Runnable t1 = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // do nothing
            }
            computer.receiveInput(BigInteger.valueOf(100));
        };
        t1.run();

        computer.execute();

        assertThat(computer.getOutput().intValue(), is(1001));
    }

    @Test
    public void supportsLargerNumbers() {
        final BigInteger a = BigInteger.valueOf(34915192);
        final BigInteger b = BigInteger.valueOf(34915192);
        final Map<BigInteger, BigInteger> program = bigIntegerInput(1102,34915192,34915192,7,4,7,99,0);
        computer.feed(program); // no input
        computer.execute();

        assertThat(String.valueOf(computer.getOutput()).length(), is(16));
        assertThat(computer.getOutput(), is(a.multiply(b)));
    }

    @Test
    public void outputsLargerNumbers() {
        computer.feed("104,1125899906842624,99"); // no input
        computer.execute();

        assertThat(computer.getOutput(), is(new BigInteger("1125899906842624")));
    }

    @Test
    public void supportsLargerThanInitialMemory() {
        computer.feed("1001,21,3,20,4,20,99");
        computer.execute();

        assertThat(computer.getOutput().intValue(), is(3));
    }

    @Test
    public void testDay72021Input() {
        final StringBuilder received = new StringBuilder();
        final IntcodeComputerOutputReceiver receiver = new IntcodeComputerOutputReceiver() {
            @Override
            public String id() {
                return "Day7-2021-Test";
            }

            @Override
            public void receive(BigInteger input) {
                received.append((char) input.intValue());
            }
        };

        computer.subscribe(receiver);
        computer.feed("1101,1,29,67,1102,0,1,65,1008,65,35,66,1005,66,28,1,67,65,20,4,0,1001,65,1,65,1106,0,8,99,35,67,101,99,105,32,110,39,101,115,116,32,112,97,115,32,117,110,101,32,105,110,116,99,111,100,101,32,112,114,111,103,114,97,109,10,346,92,161,1,634,8,35,96,1341,1149,237,33,593,459,801,98,1160,322,67,98,1219,475,12,274,24,1111,1134,14,195,234,654,202,1057,598,15,471,1583,70,4,244,96,407,51,1158,275,962,1034,694,696,271,591,389,1067,317,99,1321,177,18,257,1569,238,492,1006,857,33,31,984,296,146,1910,214,367,600,62,1365,478,31,238,384,1013,732,445,214,645,123,1069,391,80,1052,70,886,18,1472,547,94,1483,729,1220,1246,694,615,775,581,1056,405,428,138,1227,23,0,273,466,963,1250,324,1628,1122,498,588,0,236,499,390,92,64,1190,387,47,501,62,269,470,720,567,694,666,280,0,57,203,377,1061,781,857,698,50,291,370,1494,8,1124,665,113,381,457,901,151,932,95,555,72,156,192,170,606,1033,39,542,19,453,1286,797,1055,190,1,1075,1007,932,1503,224,209,138,559,532,342,115,772,728,470,479,122,76,174,810,270,1284,210,1182,176,683,1548,73,605,252,879,180,482,544,479,755,282,1617,486,183,551,369,916,32,234,516,1,212,6,1094,224,1316,694,195,1256,371,413,287,916,250,1143,126,574,1523,14,659,152,90,76,333,15,122,222,165,1184,476,682,75,298,1630,285,777,1167,381,606,161,287,136,329,641,560,516,1491,142,39,203,1147,459,505,586,186,234,99,591,213,323,355,653,1030,586,29,136,1021,773,1241,1099,564,65,226,8,337,179,117,1599,29,871,503,189,1033,193,278,786,1270,517,427,93,43,35,66,77,128,9,3,1277,1564,1005,219,1205,1517,739,60,25,401,408,441,143,108,506,1638,588,406,828,11,147,1167,1434,458,678,244,214,42,67,129,121,280,563,445,216,712,158,914,981,454,362,775,582,409,1659,1236,9,408,519,885,163,918,1001,1044,109,451,805,114,1375,251,331,147,1580,14,368,3,723,21,1771,20,188,228,247,124,726,615,543,297,347,765,816,668,649,1061,1732,328,1197,690,497,367,1219,957,1206,188,133,196,222,47,479,1901,243,859,1331,976,541,556,584,1337,156,1675,349,1321,817,764,303,359,42,992,367,271,138,163,329,444,591,15,1137,1418,1051,24,254,1867,149,169,295,230,1243,1372,263,43,973,485,676,463,563,380,402,446,518,698,1318,49,172,479,215,377,1110,1774,1154,707,158,879,259,473,362,245,1466,1074,527,636,307,1522,1371,1228,556,522,423,161,39,1602,1135,437,89,273,320,1031,838,133,123,189,816,539,239,568,878,917,82,905,291,825,268,1391,326,26,793,55,1356,629,84,241,261,1220,295,23,642,403,809,168,28,86,434,1178,12,1145,106,1091,942,168,1761,788,666,376,1353,44,12,209,658,23,205,964,964,566,367,336,62,462,565,151,505,1264,23,40,251,140,104,20,328,222,734,296,14,89,199,715,382,200,246,34,3,549,173,650,1219,52,626,23,65,802,334,286,1039,254,408,528,608,1554,516,83,429,1454,384,709,414,384,397,27,706,586,125,91,81,278,178,111,263,102,190,1235,287,1113,34,50,258,126,191,268,1262,745,1205,217,16,45,829,263,52,229,822,639,955,729,1251,98,112,94,550,247,269,1001,22,1282,420,700,41,445,493,19,44,75,1518,36,943,68,1697,172,558,1232,1229,122,234,755,499,845,3,1448,100,662,654,983,92,126,89,391,1672,1546,324,149,136,412,649,288,633,1226,10,1725,717,88,50,890,820,1114,1519,15,162,1769,963,610,241,350,502,73,249,263,143,324,180,615,426,139,94,5,954,117,657,418,170,635,5,276,8,723,344,32,822,3,323,11,22,471,811,51,52,49,1,575,20,1,287,664,277,252,551,366,836,181,559,35,27,28,28,856,1057,349,447,602,447,258,1874,1493,452,138,846,1530,40,482,60,101,840,120,23,115,281,389,44,1170,37,558,467,357,1090,18,120,526,284,930,885,152,169,674,14,97,639,1935,61,320,1275,1009,13,672,351,194,872,30,214,158,658,302,70,1404,137,3,818,162,910,199,987,392,310,922,962,751,1772,260,108,686,932,204,312,130,794,6,82,49,24,167,188,905,772,422,735,54,931,1040,723,16,640,858,428,81,119,85,45,1550,138,142,508,899,626,9,401,957,158,24,132,548,139,376,115,1661,203,485,1334,860,213,93,128,8,799,611,1470,2,800,353,75,166,26,120,14,869,222,21,1146,78,1500,321,0,738,309,1337,323,460,301,1025,205,717,436,125,166,1282,265,482,373,1247,173,228,729,78,125,366");
        computer.execute();

        System.out.println(received);
    }
}
