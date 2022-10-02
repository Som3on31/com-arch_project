package components;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Simulator {
    // memory
    // String[] instCode;
    long[] instCodeBin;

    long[] registers;
    HashMap<String, Integer> savedLabels; // save all label's positions

    // private void instCode(){

    // }
    public Simulator() {
        // instCode = new String[Short.MAX_VALUE];
        instCodeBin = new long[Short.MAX_VALUE];
        registers = new long[8];
    }

    public void run(String filedesc) {
        Arrays.fill(registers, 0); // reset all available val in the regs to 0
        Arrays.fill(instCodeBin, 0);
        savedLabels = new HashMap<>();

        Assembler asb = new Assembler();
        String[] instCode = new String[Short.MAX_VALUE];

        try {
            Scanner s = new Scanner(new File(filedesc));
            String current = s.nextLine();
            int instCount = 0;

            while (current != null) {
                instCode[instCount] = current;
                if (!s.hasNext())
                    break;
                current = s.nextLine();
                instCount++;
            }

            saveAllLabels(filedesc, asb);

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO separate all inst codes into parts then convert them into binary and hex
        // address

        // TODO execute inst given, remember to print error if it does anything
        // undefined in the project spec

    }

    // --------------Used in run to show the current state
    private void printStateInitial() {
        // show mem

        //
    }

    private void printStateRun(int pc) {
        // print @@@ then show the current state of the simulator
        // example:
        // http://myweb.cmu.ac.th/sansanee.a/ComputerArchitecture/Project/ExSimulator.txt
    }

    // TODO add any private code if necessary, do not forget to comment how it works
    // (either English or Thai) then type - [name] after the comment

    /**
     * Break instruction code into parts then keep all labels used in the file
     * <p>
     * By Saharit Kadkasem
     * </p>
     * 
     * @param filedesc a file destination
     * @param asb      An assembler
     * @throws Exception If any duplicate label or any illegally defined label (e.g.
     *                   length greater than 6) is found
     */
    private void saveAllLabels(String filedesc, Assembler asb) throws Exception {
        int instCount = 0;

        Scanner fileScanner = new Scanner(filedesc);

        String current = fileScanner.nextLine();
        while (true) {
            System.out.println(current);
            String[] instInParts = asb.separate(current);

            if (instInParts[0] != null) {
                if (savedLabels.containsKey(instInParts[0]))
                    throw new Exception("Duplicate label");

                if (instInParts[0].length() <= 6)
                    savedLabels.put(instInParts[0], instCount);
                else {
                    throw new Exception("Labels must not be longer than 6 characters.");
                }
            }

            if (!fileScanner.hasNext()) {
                fileScanner.close();
                break;
            }
            current = fileScanner.nextLine();
            instCount++;
        }
        fileScanner.close();

        // Printout debugging only
        // for (Map.Entry<String, Integer> e : savedLabels.entrySet()) {
        // System.out.println("Label: " + e.getKey() + " val: " + e.getValue());
        // }
    }
}
