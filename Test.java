import components.Assembler;

public class Test {

    public static void main(String[] args) {
        // String exampleInst = "add 1 0 5";
        String exampleInst1 = "kek lw 5 e lol";
        Assembler asb = new Assembler();

        String[] result;
        try {
            result = asb.separate(exampleInst1);
            for (String word : result) {
                if (word == null)
                    continue;
                System.out.println(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}