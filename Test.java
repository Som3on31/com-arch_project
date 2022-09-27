import components.Assembler;

public class Test {

    public static void main(String[] args) throws Exception {
        // String exampleInst = "add 1 0 5";
        String exampleInst1 = "lw 0 1 five";
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
            asb.convert(result); //convert word to binary
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}