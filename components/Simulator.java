package components;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Simulator {
    // memory
    String[] instCode = new String[Short.MAX_VALUE];
    int[] instCodeBin = new int[Short.MAX_VALUE];

    int[] registers = new int[8];
    LinkedList<String> savedLabels; // save all label's positions

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

        // TODO execute inst given

        s.close();
    }

    // used in run to show the current state
    private void printStateInitial() {
        // show mem

        //
    }

    private void printStateRun(int pc) {
        // print @@@ then show the current state of the simulator
        // example:
        // http://myweb.cmu.ac.th/sansanee.a/ComputerArchitecture/Project/ExSimulator.txt
    }
}
