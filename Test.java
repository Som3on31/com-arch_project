// import java.util.LinkedHashMap;
// import java.util.Scanner;
// import java.io.File;

import components.Assembler;
import components.Simulator;

public class Test {

    // prevents construction anywhere but here
    private Test() {
    }

    public static void main(String[] args) throws Exception {

        // String exampleInst1 = "lw 1 2 3";// lw rt rs imm
        // String exampleInst1 = "beq 0 1 2";
        // String exampleInst1 = ".fill 5";
        // String exampleInst1 = ".fill -100";
        // String exampleInst1 = "end halt";
        // String exampleInst1 = "jalr 4 2";

        // Assembler asb = new Assembler();
        // System.out.println();

        try {
            // test label saving
            // LinkedHashMap<String, Integer> savedLabels = new LinkedHashMap<>();
            // int instCount = 0;
            // Scanner fileScanner = new Scanner(new File("./testcases/testcase1.txt"));

            // String[] inst = new String[50];

            // String current = fileScanner.nextLine();
            // while (true) {
            // inst[instCount] = current;
            // String[] instInParts = asb.separate(current);

            // if (instInParts[0] != null) {
            // if (savedLabels.containsKey(instInParts[0]))
            // throw new Exception("Duplicate label");

            // if (instInParts[0].length() <= 6)
            // savedLabels.put(instInParts[0], instCount);
            // else {
            // throw new Exception("Labels must not be longer than 6 characters.");
            // }
            // }

            // if (!fileScanner.hasNext())
            // break;
            // current = fileScanner.nextLine();
            // instCount++;
            // }
            // fileScanner.close();

            // asb.massConvert(inst, savedLabels, instCount);

            // for (Map.Entry<String, Integer> e : savedLabels.entrySet()) {
            // System.out.println("Label: " + e.getKey() + " val: " + e.getValue());
            // }

            // String[] result = asb.separate(exampleInst1);

            // for (String word : result) {
            // if (word == null)
            // continue;
            // System.out.println(word);
            // }

            // asb.convert(result, savedLabels, 0); // convert word to binary

            // first test case
            Simulator s = new Simulator();
            s.run("./testcases/testcase1.txt");

            // System.out.println(Simulator.binToDec("111", 5));

            // String bits = "10000000000000000000000000000000";
            // int x = binToDec(bits);

            // System.out.println(x);
            // System.out.println(Integer.MAX_VALUE + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int binToDec(String bits) {
        int result = 0;
        boolean twoC = false;

        StringBuilder sb = new StringBuilder();
        if (bits.length() == 32) {
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

        result = twoC ? (binToDec_U(sb.toString()) + 1) * (-1) : binToDec_U(sb.toString());

        return result;
    }

    private static int binToDec_U(String bits) {
        int result = 0;

        if (bits.length() <= 31) {
            for (int i = 0; i < bits.length(); i++) {
                if (bits.charAt(i) == '1')
                    result += Math.pow(2, bits.length() - 1 - i);
            }
        } else {
            return -3;
        }

        return result;
    }

}