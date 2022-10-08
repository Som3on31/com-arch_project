import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.File;

import components.Assembler;
import components.Simulator;

public class Test {

    public static void main(String[] args) throws Exception {

        // String exampleInst1 = "lw 1 2 3";// lw rt rs imm
        // String exampleInst1 = "beq 0 1 2";
        // String exampleInst1 = ".fill 5";
        String exampleInst1 = ".fill -100";
        // String exampleInst1 = "end halt";
        // String exampleInst1 = "jalr 4 2";

        Assembler asb = new Assembler();
        Simulator sim = new Simulator();

        System.out.println();

        sim.printStateRun(0);

        try {
            // test label saving
            LinkedHashMap<String, Integer> savedLabels = new LinkedHashMap<>();
            int instCount = 0;
            Scanner fileScanner = new Scanner(new File("./testcases/testcase1.txt"));

            String[] inst = new String[50];

            String current = fileScanner.nextLine();
            while (true) {
                inst[instCount] = current;
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

                if (!fileScanner.hasNext())
                    break;
                current = fileScanner.nextLine();
                instCount++;
            }
            fileScanner.close();

            asb.massConvert(inst, savedLabels, instCount);

            // for (Map.Entry<String, Integer> e : savedLabels.entrySet()) {
            // System.out.println("Label: " + e.getKey() + " val: " + e.getValue());
            // }

            String[] result = asb.separate(exampleInst1);

            for (String word : result) {
                if (word == null)
                    continue;
                System.out.println(word);
            }

            asb.convert(result, savedLabels, 0); // convert word to binary

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}