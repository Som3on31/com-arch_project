import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import components.Assembler;

public class Test {

    public static void main(String[] args) throws Exception {

        try {
            List<String> allLines = Files.readAllLines(Paths.get("./testcases/testcase1.txt"));
            for (String line : allLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // String exampleInst = "add 1 0 5";
        String exampleInst1 = "lw 1 2 3";// lw rt rs imm
        String exampleInst = "add 1 2 1";
        // String exampleInst1 = "lw 0 1 five";
        Assembler asb = new Assembler();
        System.out.println();

        String[] result;
        try {
            result = asb.separate(exampleInst1);
            for (String word : result) {
                if (word == null)
                    continue;
                System.out.println(word);
            }
            asb.convert(result); // convert word to binary

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}