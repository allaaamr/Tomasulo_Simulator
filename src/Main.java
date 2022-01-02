import bus.Bus;
import bus.BusListener;
import bus.Clock;
import instructions.Register;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{

    Clock clock;
    double memory [] ;
    Register[] registerFile ;
    Bus bus;

    LoadBuffer loadBuffer;
    StoreBuffer storeBuffer;
    ReservationStation addSubStation;
    ReservationStation mulDivStation;
    Queue <InstructionCompiler> queue;
    InstructionStatus status;


    public Main(String path, int loadBufferLength, int storeBufferLength, int addSubLength, int mulDivLength,
                int addSubCycles, int mulDivCycles, int loadCycles, int storeCycles) throws IOException{

        ////////////////////
        queue = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        //filling the instruction queue from a file
        while((line = br.readLine()) != null){
            queue.add(new InstructionCompiler(line));
        }
        ////////////////////
        clock = new Clock();
        memory = new double[2048];
        registerFile =  new Register[32];
        bus = new Bus(registerFile);
        loadBuffer = new LoadBuffer(clock , loadBufferLength, bus);
        status = new InstructionStatus(queue.size());
        storeBuffer = new StoreBuffer(clock ,storeBufferLength);
        addSubStation = new ReservationStation(clock ,addSubCycles,0, addSubLength, bus, status);
        mulDivStation= new ReservationStation(clock , 0, mulDivCycles , mulDivLength, bus, status);
        bus.setListeners(new BusListener[]{loadBuffer,storeBuffer,addSubStation,mulDivStation});


        //initializing register file registers
        for(int i = 0 ; i < registerFile.length ; i++){
            registerFile[i] = new Register();
        }

        registerFile[0].updateRegister(5);
        registerFile[1].updateRegister(7);
        registerFile[2].updateRegister(2);
        registerFile[3].updateRegister(2);
        registerFile[5].updateRegister(-5);


    }
    public void executeProgram() {

        while (!status.finishedProgram()) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter 1 to view the next cycle");
            int reply = sc.nextInt();
            if(reply==1) {
                clock.updateClock();
                System.out.println("***********************************************************************************************");

                System.out.println(" Cycle " + clock.getCycles());
                if (!queue.isEmpty()) {
                    InstructionCompiler nextInstruction = queue.peek();
                    System.out.println(queue.peek().toString());

                    Register firstOperand = registerFile[nextInstruction.getFirstOperand()];
                    Register secondOperand = registerFile[nextInstruction.getSecondOperand()];
                    int destination = nextInstruction.getDestinationRegister();
                    int address = nextInstruction.getMemoryAddress();

                    boolean issued = false;

                    switch (nextInstruction.getInstructionType()) {

                        case "ADD":
                            issued = addSubStation.issueAddSub(registerFile[destination], firstOperand, secondOperand, true);
                            break;

                        case "SUB":
                            issued = addSubStation.issueAddSub(registerFile[destination], firstOperand, secondOperand, false);
                            break;

                        case "MUL":
                            issued = mulDivStation.issueMulDiv(registerFile[destination], firstOperand, secondOperand, true);
                            break;

                        case "DIV":
                            issued = mulDivStation.issueMulDiv(registerFile[destination], firstOperand, secondOperand, false);
                            break;

                        case "LD":
                            issued = loadBuffer.issue(destination, address);
                            break;

                        case "SD":
                            issued = storeBuffer.issue(firstOperand, destination);
                            break;
                    }

                    if (issued) {
                        System.out.println("instruction issued");
                        InstructionCompiler issuedInstruction = queue.poll();
                        int index = status.getLastIndex();
                        status.statusTable[index][0] = issuedInstruction.getInstruction();
                        status.statusTable[index][1] = clock.getCycles();
                        status.setLastIndex(index + 1);
                    } else
                        System.out.println("Failed to Issue");


                }
                addSubStation.executeAddSub();
                mulDivStation.executeMulDiv();
                System.out.println(Arrays.toString(status.getStatusTable()[0]));
                System.out.println(Arrays.toString(status.getStatusTable()[1]));
                System.out.println(Arrays.toString(status.getStatusTable()[2]));
                System.out.println(Arrays.toString(status.getStatusTable()[3]));

                //      loadBuffer.execute();
                //      storeBuffer.execute() ;
                System.out.println("***********************************************************************************************");
            }
            else{
                System.exit(0);
            }
        }
    }

    public static void printRegFile(Register [] registerFile){
        for(int i=0; i<32; i+=4){
            System.out.print("R" +i +": "+registerFile[i].getValue() +"   ");
            System.out.print("R" +(i+1) +": "+registerFile[i+1].getValue()+"   ");
            System.out.print("R" +(i+2)  +": "+registerFile[i+2].getValue()+"   ");
            System.out.print("R" +(i+3) +": "+registerFile[i+3].getValue()+"   ");
            System.out.println();
        }
    }

    public static void main(String [] args) throws IOException {
        Main main = new Main("programme.txt", 5, 5, 5, 5, 1, 5, 8, 8);
        main.executeProgram();
        printRegFile(main.registerFile);
    }

}


