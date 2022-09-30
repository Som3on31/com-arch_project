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
    public long binarytodeciaml (String binary)throws Exception{
        String check = binary.substring(14,15);
        long decimal = 0; 

        if(check=="1"  ){
            int a=0;
            StringBuilder builder = new StringBuilder();

                for (int i = 0; i < binary.length(); i++) {builder.append((binary.charAt(i) == '1' ? '0' : '1'));}
                String str1 = builder.substring(0, 16);//bit16-32
                String str2 = builder.substring(16, 32);//bit0-16
                long binaryString=Long.parseLong(str2);//bit0-16
                long binaryString1=Long.parseLong(str1); //bit16-32

                //bit0-16//
                while(true){  
                    if(binaryString == 0){  break;  } 
                    else {  
                        long temp = binaryString%10;  
                        decimal += temp*Math.pow(2, a);  
                        binaryString = binaryString/10;  
                        a++;  
                     }  
                  }  
                  //bit16-32//
                  int b=16;
                  while(true){  
                    if(binaryString1 == 0){  break;  } 
                    else {  
                        long temp = binaryString1%10;  
                        decimal += temp*Math.pow(2, b);  
                        binaryString1 = binaryString1/10;  
                        a++;  
                     }  
                  }
                  decimal -= decimal*2;
        }else {
            String str1 = binary.substring(0, 16);//bit16-32
            String str2 = binary.substring(16, 32);//bit0-16
            long binaryString=Long.parseLong(str2);//bit0-16
            long binaryString1=Long.parseLong(str1); //bit16-32
            ////// 0-16 bit //////////    
           int n = 0;  
           while(true){  
             if(binaryString == 0){  break;  } 
             else {  
                 long temp = binaryString%10;  
                 decimal += temp*Math.pow(2, n);  
                 binaryString = binaryString/10;  
                 n++;  
              }  
           }  
           /////////////// 16 bit -32 bit//////////
           int m=16;
           while(true){  
             if(binaryString1 == 0){  break;  } 
             else {  
                 long temp = binaryString1%10;  
                 decimal += temp*Math.pow(2, m);  
                 binaryString1 = binaryString1/10;  
                 m++;  
              }  
           }

        }
        
        return decimal;
    }

    public String decimaltohexadecimal (long decimal) throws Exception{
        int d=(int) decimal;
        int rem;  
        String hex="";   
        char hexchars[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};  
        while(d>0){  
            rem=d%16;   
            hex=hexchars[rem]+hex;   
            d=d/16;  
            rem++;
        }  
        
        return hex;  
    }

    public void convert(String[] result) throws Exception {
        if (result[1].equals("lw")) { // case lw I-type
            long rS = Long.parseLong(String.valueOf(result[2]));
            long rD = Long.parseLong(String.valueOf(result[3]));
            long r3 = 3;
            String rd = Long.toBinaryString(rD);
            String rs = Long.toBinaryString(rS);
            String imm = Long.toBinaryString(r3);
            do {
                rd = "0" + rd;
            } while (rd.length() < 3); // Check rd bit
            do {
                rs = "0" + rs;
            } while (rs.length() < 3); // Check rs1 bit
            do {
                imm = "0" + imm;
            } while (imm.length() < 16); // Check imm bit

            System.out.println("");
            System.out.println("Ins : " + result[1]);
            System.out.println("rs : " + rs);
            System.out.println("rd : " + rd);
            System.out.println("imm : " + imm);
            String temp = "010" + rs + rd + imm;
            do {
            temp = "0" + temp;
            } while (temp.length() < 32);
            System.out.println(binarytodeciaml(temp));
        } else if(result[1].equals("add")){ //R-type
            long r1 = Long.parseLong(String.valueOf(result[2])); //rs1
            long r2 = Long.parseLong(String.valueOf(result[3])); //rs2
            long r3 = Long.parseLong(String.valueOf(result[4])); //rd 
            String rd = Long.toBinaryString(r3);//destReg
            String rs = Long.toBinaryString(r1); //reg A
            String rt = Long.toBinaryString(r2); //reg B
            if(rd.length() < 3){
                do {
                    rd = "0" + rd;
                } while (rd.length() < 3); // Check rd bit
            }
            if(rs.length() < 3){
                do {
                    rs = "0" + rs;
                } while (rs.length() < 3); // Check rs1 bit
            }
            if(rt.length() < 3){
                do {
                    rt = "0" + rt;
                } while (rt.length() < 3); // Check rs2 bit
            }
            System.out.println("");
            System.out.println("Ins : " + result[1]);
            System.out.println("rd : " + rd); //bit 0-2
            System.out.println("rs1 : " + rs);//bit 21-19
            System.out.println("rs2 : " + rt);// bit 18-16 
            String temp = "0000000"+"000"+rs+rt+"0000000000000"+rd;
            System.out.println(binarytodeciaml(temp));
        }
        else {
            System.out.println("error");
        }
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
