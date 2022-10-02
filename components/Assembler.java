package components;

import java.util.Map;
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
                                            // in the case of jalr, it's {label,inst,num,num,null}
                                            // label can be null but never a dupe of one another

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

    public long binarytodeciaml(String binary) throws Exception {
        String check = binary.substring(14, 15);
        long decimal = 0;

        if (check == "1") {
            int a = 0;
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < binary.length(); i++) {
                builder.append((binary.charAt(i) == '1' ? '0' : '1'));
            }
            String str1 = builder.substring(0, 16);// bit16-32
            String str2 = builder.substring(16, 32);// bit0-16
            long binaryString = Long.parseLong(str2);// bit0-16
            long binaryString1 = Long.parseLong(str1); // bit16-32

            // bit0-16//
            while (true) {
                if (binaryString == 0) {
                    break;
                } else {
                    long temp = binaryString % 10;
                    decimal += temp * Math.pow(2, a);
                    binaryString = binaryString / 10;
                    a++;
                }
            }
            // bit16-32//
            int b = 16;
            while (true) {
                if (binaryString1 == 0) {
                    break;
                } else {
                    long temp = binaryString1 % 10;
                    decimal += temp * Math.pow(2, b);
                    binaryString1 = binaryString1 / 10;
                    a++;
                }
            }
            decimal -= decimal * 2;
        } else {
            String str1 = binary.substring(0, 16);// bit16-32
            String str2 = binary.substring(16, 32);// bit0-16
            long binaryString = Long.parseLong(str2);// bit0-16
            long binaryString1 = Long.parseLong(str1); // bit16-32
            ////// 0-16 bit //////////
            int n = 0;
            while (true) {
                if (binaryString == 0) {
                    break;
                } else {
                    long temp = binaryString % 10;
                    decimal += temp * Math.pow(2, n);
                    binaryString = binaryString / 10;
                    n++;
                }
            }
            /////////////// 16 bit -32 bit//////////
            int m = 16;
            while (true) {
                if (binaryString1 == 0) {
                    break;
                } else {
                    long temp = binaryString1 % 10;
                    decimal += temp * Math.pow(2, m);
                    binaryString1 = binaryString1 / 10;
                    m++;
                }
            }

        }

        return decimal;
    }

    public String decimaltohexadecimal(long decimal) throws Exception {
        int d = (int) decimal;
        int rem;
        String hex = "";
        char hexchars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        while (d > 0) {
            rem = d % 16;
            hex = hexchars[rem] + hex;
            d = d / 16;
            rem++;
        }

        return hex;
    }

    /**
     * 
     * 
     * 
     * @param instParts an instruction divided into parts
     * @param labels    A map of saved labels from instruction separation
     * @throws Exception When this function detects any undefined type
     */
    public void convert(String[] instParts, Map<String, Integer> labels, int pc) throws Exception {
        String result;
        String type;
        switch (instParts[1]) {
            case "add" -> type = "010";
            case "nand" -> type = "001";
            case "lw" -> type = "010";
            case "sw" -> type = "011";
            case "beq" -> type = "100";
            case "jalr" -> type = "101";
            case "halt" -> type = "110";
            case "noop" -> type = "111";
            case ".fill" -> type = "E"; // won't be used for result, but as a placeholder and because it's E
            default -> throw new Exception("Undefined type");
        }
        if (isRtype(instParts[1])) {
            long r1 = Long.parseLong(String.valueOf(instParts[2])); // rs1
            long r2 = Long.parseLong(String.valueOf(instParts[3])); // rs2
            long r3 = Long.parseLong(String.valueOf(instParts[4])); // rd

            String rd = Long.toBinaryString(r3);// destReg
            String rs = Long.toBinaryString(r1); // reg A
            String rt = Long.toBinaryString(r2); // reg B
            rd = toXBits(rd, 3);
            rs = toXBits(rs, 3);
            rt = toXBits(rt, 3);

            System.out.println("");
            System.out.println("Ins : " + instParts[1]);
            System.out.println("rd : " + rd); // bit 0-2
            System.out.println("rs1 : " + rs);// bit 21-19
            System.out.println("rs2 : " + rt);// bit 18-16
            result = "0000000" + type + rs + rt + "0000000000000" + rd;
            System.out.println(binarytodeciaml(result));
        } else if (isItype(instParts[1])) { // case lw I-type
            long rS = Long.parseLong(String.valueOf(instParts[2]));
            long rD = Long.parseLong(String.valueOf(instParts[3]));
            long offsetField = isNumber(instParts[4]) ? Long.parseLong(String.valueOf(instParts[4]))
                    : instParts[1].equals("beq") ? labels.get(instParts[4]) - pc - 1 : labels.get(instParts[4]);

            String rd = Long.toBinaryString(rD);
            String rs = Long.toBinaryString(rS);
            String imm = Long.toBinaryString(offsetField);
            rd = toXBits(rd, 3); // Check rd bit
            rs = toXBits(rs, 3); // Check rs1 bit
            imm = toXBits(imm, 16); // Check imm bit

            System.out.println("");
            System.out.println("Ins : " + instParts[1]);
            System.out.println("rs : " + rs);
            System.out.println("rd : " + rd);
            System.out.println("imm : " + imm);
            result = type + rs + rd + imm;
            do {
                result = "0" + result;
            } while (result.length() < 32);
            System.out.println(binarytodeciaml(result));
        } else if (isJtype(instParts[1])) {
            long rS = Long.parseLong(String.valueOf(instParts[2]));
            long rD = Long.parseLong(String.valueOf(instParts[3]));

            String rs = Long.toBinaryString(rS);
            String rd = Long.toBinaryString(rD);
            rs = toXBits(rs, 3);
            rd = toXBits(rd, 3);

            StringBuilder sb = new StringBuilder();

            for (int i = 32; i > 0; i--) {
                if (i == 25) {
                    i = 17;
                    sb.append(type);
                    sb.append(rs);
                    sb.append(rd);
                    continue;
                }
                sb.append('0');
            }
            result = sb.toString();
            System.out.println(binarytodeciaml(result));
        } else if (isOtype(instParts[1])) {
            StringBuilder sb = new StringBuilder();
            for (int i = 32; i > 0; i--) {
                if (i == 25) {
                    i = 23;
                    sb.append(type);
                    continue;
                }
                sb.append('0');
            }
            result = sb.toString();
            System.out.println(binarytodeciaml(result));
        } else if (isFill(instParts[1])) {
            long valInt = Long.parseLong(instParts[2]);
            String val = Long.toBinaryString(valInt);

            StringBuilder sb = new StringBuilder();
            for (int i = 32; i > val.length(); i--) {
                sb.append('0');
            }
            sb.append(val);
            result = sb.toString();
            System.out.println(binarytodeciaml(result));
        } else
            throw new Exception("Type currently not supported at the moment");
    }

    // Type check
    private boolean isInst(String word) {
        word = word.toLowerCase();
        return isRtype(word) || isItype(word) || isJtype(word) || isOtype(word) || isFill(word);
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
        return word.equals("jalr");
    }

    private boolean isOtype(String word) {
        word = word.toLowerCase();
        return word.equals("halt") || word.equals("noop");
    }

    private boolean isFill(String word) {
        word = word.toLowerCase();
        return word.equals(".fill");
    }

    private boolean isNumber(String word) {
        for (char c : word.toCharArray()) {
            if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
                    || c == '8'
                    || c == '9'))
                return false;
        }
        return true;
    }

    /**
     * Fill 0 before the most significant bit until it reaches a specific number
     * 
     * @param bits      a string converted into binary
     * @param numOfBits a number of requited bits
     * @return a string bit that has been modified
     */
    private String toXBits(String bits, int numOfBits) {
        while (bits.length() < numOfBits) {
            bits = "0" + bits;
        }
        return bits;
    }

}
