package components;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Simulator {
    // memory
    String[] instCode = new String[Short.MAX_VALUE];
    int[] instCodeBin = new int[Short.MAX_VALUE];

    int[] registers = new int[8];
    HashMap<String, Integer> savedLabels; // save all label's positions

    // private void instCode(){

    // }
    public Simulator() {
        instCode = new String[Short.MAX_VALUE];
        instCodeBin = new int[Short.MAX_VALUE];
        registers = new int[8];

    }

    public void run(String filedesc) {
        Arrays.fill(registers, 0); // reset all available val in the regs to 0

        Scanner s = new Scanner(filedesc);
        Assembler asb = new Assembler();

        s.nextLine();

        // TODO separate all inst codes into parts then convert them into binary and hex
        // address

        // TODO execute inst given, remember to print error if it does anything
        // undefined to the project spec

        s.close();
    }

    // --------------Used in run to show the current state
    private void printStateInitial() {
        // show mem

        //
    }

    private void printStateRun(int pc) {
        // print @@@ then show the current state of the simulator
        // example:
        // http://myweb.cmu.ac.th/sansanee.a/ComputerArchitecture/Project/ExSimulator.txt
    }

    // TODO add any private code if necessary, do not forget to comment how it works
    // (either English or Thai) then type - [name] after the comment
}
