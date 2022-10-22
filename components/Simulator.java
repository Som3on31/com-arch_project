package components;

import java.util.Arrays;

public class Simulator {
    // memory
    int[] memory;
    int[] registers;

    // status check
    boolean notStarted;
    boolean doHalt;
    int instExecutedCount = 0;

    public Simulator() {
        // instCode = new String[Short.MAX_VALUE];
        memory = new int[(int) Math.pow(2, 16)];
        registers = new int[8];
    }

    public void run(String[] machineBinary, int instCount) throws Exception {
        Arrays.fill(registers, 0); // reset all available val in the regs to 0
        Arrays.fill(memory, 0);

        // Scanner s = new Scanner(new File(filedesc));
        // String current = s.nextLine();
        // instCode[instCount] = current;

        // while (current != null) {

        // instCode[instCount] = current;
        // if (!s.hasNext())
        // break;

        // current = s.nextLine();
        // if (current.equals(""))
        // while (current.equals("")) {
        // current = s.nextLine();
        // }
        // instCount++;
        // }
        // instCount++;

        // s.close();

        // TODO separate all inst codes into parts then convert them into binary and hex
        // address
        memory = binToDec_Arr(machineBinary);

        // TODO execute inst given, remember to print error if it does anything
        // undefined in the project spec

        doHalt = false;
        notStarted = true;
        instExecutedCount = 0;
        for (int pc = 0; pc < instCount; pc++) {
            // if pc is 0 or over the max value of 8-bit integer
            if (pc < 0 || pc > (int) Math.pow(2, 16)) {
                if (pc < 0)
                    throw new Exception("pc underflow");
                throw new Exception("pc overflow");
            }
            printState(pc, instCount);

            if (doHalt) {
                break;
            }

            // do something
            String instStr = Integer.toBinaryString(memory[pc]);
            if (instStr.length() < 32)
                instStr = extendTo32Bits(instStr);
            String type = instStr.substring(7, 10);

            int rS = binToDec_U(instStr.substring(10, 13));
            int rD = binToDec_U(instStr.substring(13, 16));
            int imm = binToDec(instStr.substring(16), 16); // if its value is lower than 7, it will be used
                                                           // as rD for R-type instructions
            switch (type) {
                case "000" -> {
                    // add
                    if (imm != 0)
                        registers[imm] = registers[rS] + registers[rD];
                }
                case "001" -> {
                    // nand
                    if (imm != 0)
                        registers[imm] = ~(registers[rS] & registers[rD]);
                }
                case "010" -> {
                    // lw
                    if (rD != 0)
                        registers[rD] = memory[registers[rS] + imm];
                }
                case "011" -> {
                    // sw
                    memory[registers[rS] + imm] = registers[rD];
                }
                case "100" -> {
                    // beq
                    if (registers[rS] == registers[rD])
                        pc += imm;
                }
                case "101" -> {
                    // jalr
                    if (rD != 0)
                        registers[rD] = pc + 1;
                    pc = registers[rS] - 1;
                }
                case "110" -> {
                    // halt
                    doHalt = true;
                }
                case "111" -> {
                    // noop - do nothing

                }

            }

            if (pc == 0 && notStarted)
                notStarted = false;
            instExecutedCount++;
        }

    }

    // --------------Used in run to show the current state
    private void printStateInitial(int maxSize) {
        // show mem
        for (int i = 0; i < maxSize; i++) {
            System.out.println("Memory" + "[" + i + "]" + memory[i]);
        }
        //
    }

    // print @@@ then show the current state of the simulator
    // example:
    // http://myweb.cmu.ac.th/sansanee.a/ComputerArchitecture/Project/ExSimulator.txt
    private void printStateRun(int pc, int maxSize) {

        System.out.println("@@@");
        System.out.println("state:");
        System.out.println("    pc " + pc);
        System.out.println("    memory:");

        for (int i = 0; i < maxSize; i++) {
            System.out.println("        mem[ " + i + " ] " + memory[i]);

        }

        System.out.println("    registers:");
        for (int i = 0; i < registers.length; i++) {
            System.out.println("        reg[ " + i + " ] " + registers[i]);
        }

        System.out.println("end state\n");

    }

    private void printStateFinal(int instExecutedCount) {

        System.out.println("machine halted");
        System.out.println("total of " + instExecutedCount + " instructions executed");
        System.out.println("final state of machine:");

    }

    private void printState(int pc, int maxSize) {
        if (notStarted)
            printStateInitial(maxSize);
        if (doHalt)
            printStateFinal(instExecutedCount);

        printStateRun(pc, maxSize);
    }

    // TODO add any private code if necessary, do not forget to comment how it works
    // (either English or Thai) then type - [name] after the comment

    /**
     * Coverts an array of binary string into a 32-bit integer by using a method
     * below.
     * 
     * <p>
     * By Saharit Kadkasem
     * </p>
     * 
     * @param bitsArr
     * @return
     */
    private static int[] binToDec_Arr(String[] bitsArr) {
        int[] result = new int[bitsArr.length];

        for (int i = 0; i < bitsArr.length && bitsArr[i] != null; i++) {
            result[i] = binToDec(bitsArr[i]);
        }

        return result;
    }

    /**
     * Converts a binary string to a variable length integer.
     * 
     * <p>
     * The operation starts by checking the length of {@code bits}. This method
     * will return one of the following
     * <ul>
     * <li>-1 if {@code bits} has the length of over 32.
     * <li>-2 if this method finds any unsupported character for binart conversion.
     * <li>-3 if {@code signPos} is over 32
     * <li>-4 if {@code signPos} is lesser than the length of {@code bits}
     * <li>a 32-bit integer otherwise
     * </ul>
     * </p>
     * 
     * <p>
     * By Saharit Kadkasem
     * </p>
     * 
     * @param bits    a binary string
     * @param signPos an integer at most 32-bit
     * @return an integer of a variable length up to 32 bits.
     */
    private static int binToDec(String bits, int signPos) {
        if (signPos > 32)
            return -3;

        if (signPos < bits.length())
            return -4;

        int result = 0;
        boolean twoC = false;

        StringBuilder sb = new StringBuilder();
        if (bits.length() == signPos || bits.length() == 32) {
            if (bits.charAt(0) == '1')
                twoC = true;

            for (int i = 1; i < bits.length(); i++) {
                if (bits.charAt(i) == '1')
                    sb.append('0');
                else if (bits.charAt(i) == '0')
                    sb.append('1');
                else
                    return -2;
            }

        } else if (bits.length() > 32) {
            return -1;
        }

        result = twoC ? (binToDec_U(sb.toString()) + 1) * (-1) : binToDec_U(bits);

        return result;
    }

    /**
     * Converts a binary string to a 32-bit integer.
     * 
     * By Saharit Kadkasem
     * 
     * @param bits a binary string
     * @return a signed integer. Unsupported character found in the string will
     *         return -2 and string length of over 2 will also return -1
     */
    private static int binToDec(String bits) {
        return binToDec(bits, 32);
    }

    /**
     * Coverts any binary string into unsigned 31-bit integer.
     * It will not work if the string is longer than 32 bits. Otherwise, it returns
     * 0.
     * 
     * By Saharit Kadkasem
     * 
     * @param bits A binary string
     * @return A number in the form of unsigned 31-bit integer
     */
    private static int binToDec_U(String bits) {
        int result = 0;

        if (bits.length() > 32)
            return 0;

        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1')
                result += Math.pow(2, bits.length() - i - 1);
        }

        return result;
    }

    private static String extendTo32Bits(String bits) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 32 - bits.length(); i++) {
            sb.append('0');
        }
        sb.append(bits);

        return sb.toString();
    }
}
