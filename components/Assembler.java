package components;

import java.io.FileWriter;
import java.io.File;
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
            if (count >= 4) // the rest becomes a comment (won't be complied)
                break;
            if (!s.hasNext())
                break;
            current = s.next(); // go to the next word
            count++;
        }

        s.close();
        return converted;
    }

    public long binarytodeciaml(String binary) throws Exception {
        String c = binary.substring(0, 1);
        long decimal = 0;
        long check = Long.parseLong(c);

        if (check == 1 && binary.length() == 32) {
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
            decimal -= 1;
        } else if (binary.length() == 32) {
            // System.out.println(binary);
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

        } else {
            System.out.println("bit less than 32 bit or bit more than 32 bit");
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
     * @param instParts An instruction that has been separated into parts
     * @param labels    A map which contains the position of all saved labels
     * @param pc        Current position where the simulator is reading
     * @return A string of an instruction code
     * @throws Exception
     */
    public String convert(String[] instParts, Map<String, Integer> labels, int pc) throws Exception {
        String result;
        String type;
        switch (instParts[1]) {
            case "add" -> type = "000";
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

            result = "0000000" + type + rs + rt + "0000000000000" + rd;
        } else if (isItype(instParts[1])) { // case lw I-type
            long rS = Long.parseLong(String.valueOf(instParts[2]));
            long rD = Long.parseLong(String.valueOf(instParts[3]));
            long offsetField = isNumber(instParts[4]) ? Long.parseLong(String.valueOf(instParts[4]))
                    : instParts[1].equals("beq") ? labels.get(instParts[4]) - pc - 1 : labels.get(instParts[4]);

            if (offsetField < -32768 || offsetField > 32767)
                throw new Exception("Offset field must be within -32768 to 32767");

            String rd = Long.toBinaryString(rD);
            String rs = Long.toBinaryString(rS);
            String imm = Long.toBinaryString(offsetField);
            if (imm.length() > 16)
                imm = imm.substring(imm.length() - 16);
            rd = toXBits(rd, 3); // Check rd bit
            rs = toXBits(rs, 3); // Check rs1 bit
            imm = toXBits(imm, 16); // Check imm bit

            result = type + rs + rd + imm;
            do {
                result = "0" + result;
            } while (result.length() < 32);
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
        } else if (isFill(instParts[1])) {
            int valInt = isNumber(instParts[2]) ? Integer.parseInt(instParts[2]) : labels.get(instParts[2]);
            String val = Integer.toBinaryString(valInt);

            StringBuilder sb = new StringBuilder();
            for (int i = 32; i > val.length(); i--) {
                sb.append('0');
            }
            sb.append(val);
            result = sb.toString();
        } else
            throw new Exception("Type currently not supported at the moment");

        return result;
    }

    public String[] massConvert(String[] inst, Map<String, Integer> labels) {
        String[] instInBits = new String[inst.length];

        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < inst.length; i++) {
                if (inst[i] == null)
                    break;

                String instBin = convert(separate(inst[i]), labels, i);
                sb.append(binarytodeciaml(instBin));
                instInBits[i] = instBin;
                sb.append("\n");
            }

            File file = new File("./machine_code/mc.txt");
            int dupeCount = 1; // keeps track on current dupes if the original exists
            while (!file.createNewFile()) {
                file = new File("./machine_code/mc (" + dupeCount + ").txt");
                dupeCount++;
            }
            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return instInBits;
    }

    // ------------------------------ All utilities methods go
    // here---------------------------------
    protected static boolean isInst(String word) {
        word = word.toLowerCase();
        return isRtype(word) || isItype(word) || isJtype(word) || isOtype(word) || isFill(word);
    }

    protected static boolean isRtype(String word) {
        word = word.toLowerCase();
        return word.equals("add") || word.equals("nand");
    }

    protected static boolean isItype(String word) {
        word = word.toLowerCase();
        return word.equals("lw") || word.equals("sw") || word.equals("beq");
    }

    protected static boolean isJtype(String word) {
        word = word.toLowerCase();
        return word.equals("jalr");
    }

    protected static boolean isOtype(String word) {
        word = word.toLowerCase();
        return word.equals("halt") || word.equals("noop");
    }

    protected static boolean isFill(String word) {
        word = word.toLowerCase();
        return word.equals(".fill");
    }

    protected static boolean isNumber(String word) {
        boolean neg = false;
        int charCount = 0;
        for (char c : word.toCharArray()) {
            if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7'
                    || c == '8' || c == '9')) {
                if (c == '-' && !neg && charCount == 0) {
                    neg = true;
                    continue;
                }

                return false;
            }
            charCount++;
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
    protected static String toXBits(String bits, int numOfBits) {
        while (bits.length() < numOfBits) {
            bits = "0" + bits;
        }
        return bits;
    }

}
