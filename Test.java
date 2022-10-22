import components.Assembler;
import components.Pair;
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

            // s.run("./testcases/testcase1.txt");
            // s.run("./testcases/testcase3_1.txt");
            Simulator s = new Simulator();
            Assembler asb = new Assembler();
            String filedesc = "./testcases/testcase3_2.txt";
            Pair<String[], Integer> machineCode = asb.massConvert(filedesc);
            s.run(machineCode.fst(), machineCode.snd());
            // s.run("./testcases/testcase_custom1.txt");

            // System.out.println(Simulator.binToDec("111", 5));

            // String bits = "10000000000000000000000000000000";
            // int x = binToDec(bits);

            // System.out.println(x);
            // System.out.println(Integer.MAX_VALUE + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(combination(7, 3));
    }

    private static int combination(int n, int r) {
        if (r > n || r < 0 || n < 0)
            return 5555555;

        if (n == r || r == 0)
            return 1;
        else
            return combination(n - 1, r) + combination(n - 1, r - 1);
    }
}