package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Simulator {
    // memory
    int[] instCodeBin;
    int[] registers;
    HashMap<String, Integer> savedLabels; // save all label's positions

    public Simulator() {
        // instCode = new String[Short.MAX_VALUE];
        instCodeBin = new int[Short.MAX_VALUE];
        registers = new int[8];
    }

    public void run(String filedesc) throws Exception {
        Arrays.fill(registers, 0); // reset all available val in the regs to 0
        Arrays.fill(instCodeBin, 0);
        savedLabels = new HashMap<>(); // clear all saved labels

        Assembler asb = new Assembler();
        String[] instCode = new String[Short.MAX_VALUE];

        int instCount = 0;

        try {
            Scanner s = new Scanner(new File(filedesc));
            String current = s.nextLine();
            instCode[instCount] = current;

            while (current != null) {
                if (!s.hasNext())
                    break;
                current = s.nextLine();
                instCount++;
            }

            saveAllLabelsAndSeparateCode(instCode, asb);

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO separate all inst codes into parts then convert them into binary and hex
        // address
        String[] instBinStr = asb.massConvert(instCode, savedLabels, instCount);
        System.arraycopy(instBinStr, 0, instCodeBin, 0, instBinStr.length);

        // TODO execute inst given, remember to print error if it does anything
        // undefined in the project spec

        printStateInitial(instBinStr.length);
        boolean doHalt = false;
        int instExecutedCount = 0;
        for (int pc = 0; pc < instCount; pc++) {

            if (doHalt) {
                System.out.println("machine halted");
                System.out.println("total of " + instExecutedCount + "instructions executed");
                System.out.println("final state of machine:");
            }
            // if pc is 0 or over the max value of 8-bit integer
            if (pc < 0 || pc > Short.MAX_VALUE) {
                if (pc < 0)
                    throw new Exception("pc underflow");
                throw new Exception("pc overflow");
            }
            printStateRun(pc, instBinStr.length);

            if (doHalt)
                break;

            // do something
            String instStr = Assembler.toXBits(Integer.toString(instCodeBin[pc]), 32);
            String type = instStr.substring(7, 10);

            int rS = binaryToDecimalUnsigned(instStr.substring(10, 14));
            int rD = binaryToDecimalUnsigned(instStr.substring(13, 16));
            int imm = binaryToDecimalUnsigned(instStr.substring(16)); // if its value is lower than 7, it will be used
                                                                      // as rD for R-type instructions
            switch (type) {
                case "000" -> {
                    // add
                    registers[imm] = registers[rS] + registers[rD];
                }
                case "001" -> {
                    // nand
                    registers[imm] = ~(registers[rS] & registers[rD]);
                }
                case "010" -> {
                    // lw
                    registers[rD] = instCodeBin[registers[rS] + imm];
                }
                case "011" -> {
                    // sw
                    instCodeBin[registers[rS] + imm] = registers[rD];
                }
                case "100" -> {
                    // beq
                    if (registers[rS] == registers[rD])
                        pc += 1 + imm;
                }
                case "101" -> {
                    // jalr
                    registers[rD] = pc + 1;
                    pc = registers[rS];
                }
                case "110" -> {
                    // halt
                    doHalt = true;
                }
                case "111" -> {
                    // noop

                }

            }

            instExecutedCount++;
        }

    }

    // --------------Used in run to show the current state
    private void printStateInitial(int maxSize) {
        // show mem
        for (int i = 0; i < maxSize; i++) {
            System.out.println("Memory" + "[" + i + "]" + instCodeBin[i]);
        }
        //
    }

    // print @@@ then show the current state of the simulator
    // example:
    // http://myweb.cmu.ac.th/sansanee.a/ComputerArchitecture/Project/ExSimulator.txt
    private void printStateRun(int pc, int maxSize) throws FileNotFoundException {

        System.out.println("@@@");
        System.out.println("state:");
        System.out.println("pc" + pc);
        System.out.println("memory:");

        for (int i = 0; i < maxSize; i++) {
            System.out.println("mem" + "[" + i + "]" + instCodeBin[i]);

        }
        System.out.println("@@@");

    }

    // TODO add any private code if necessary, do not forget to comment how it works
    // (either English or Thai) then type - [name] after the comment

    /**
     * Break instruction code into parts then keep all labels used in the file
     * <p>
     * By Saharit Kadkasem
     * </p>
     * 
     * @param instCode An array of instruction code
     * @param asb      An assembler
     * @return
     * @throws Exception If any duplicate label or any illegally defined label (e.g.
     *                   length greater than 6) is found
     */
    private String[][] saveAllLabelsAndSeparateCode(String[] instCode, Assembler asb) throws Exception {
        int instCount = 0;
        String[][] instInParts = new String[instCode.length][5];
        for (int i = 0; i < instCode.length; i++) {
            instInParts[i] = asb.separate(instCode[i]);

            if (instInParts[i][0] != null) {
                if (savedLabels.containsKey(instInParts[i][0]))
                    throw new Exception("Duplicate label");

                if (instInParts[i][0].length() <= 6)
                    savedLabels.put(instInParts[i][0], instCount);
                else {
                    throw new Exception("Labels must not be longer than 6 characters.");
                }
            }
        }

        return instInParts;
        // Printout debugging only
        // for (Map.Entry<String, Integer> e : savedLabels.entrySet()) {
        // System.out.println("Label: " + e.getKey() + " val: " + e.getValue());
        // }
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
    private static int binaryToDecimalUnsigned(String bits) {
        int result = 0;
        if (bits.length() > 32)
            return 0;

        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '1')
                result += Math.pow(2, bits.length() - i - 1);
        }

        return result;
    }
}
