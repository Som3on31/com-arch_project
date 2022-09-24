package components;

import java.util.Scanner;

// import components.subcomponents.*;

public class Assembler {

    public Assembler() {

    }

    /**
     * 
     * @param instCode
     * @return a string of 32-bit code in binary
     * @throws Exception
     */
    public String[] separate(String instCode) throws Exception {
        String[] converted = new String[5]; // {label,inst,rs,rt,field} except for .fill,it will be
                                            // {label,inst,num/sign,null,null}

        Scanner s = new Scanner(instCode);
        String current = s.next();

        int count = 0;
        boolean isFill = false;
        while (current != null) {
            if (isInst(current) && count == 0)
                count++;
            if (isFill(current) && count == 1)
                isFill = true;
            if (isFill && count >= 3)
                break;

            converted[count] = current; // save the word
            if (!s.hasNext())
                break;
            current = s.next(); // go to the next word
            count++;
        }

        s.close();
        return converted;
    }

    // Type check
    private boolean isInst(String word) {
        word = word.toLowerCase();
        return isRtype(word) || isItype(word) || isJtype(word) || isOtype(word);
    }

    private boolean isRtype(String word) {
        word = word.toLowerCase();
        return word.equals("add") || word.equals("nand");
    }

    private boolean isItype(String word) {
        word = word.toLowerCase();
        return word.equals("lw") || word.equals("sw") || word.equals("beq");
    }

    private boolean isJtype(String word) {
        word = word.toLowerCase();
        return word.equals("jal");
    }

    private boolean isOtype(String word) {
        word = word.toLowerCase();
        return word.equals("halt") || word.equals("noop");
    }

    private boolean isFill(String word) {
        word = word.toLowerCase();
        return word.equals(".fill");
    }

}
