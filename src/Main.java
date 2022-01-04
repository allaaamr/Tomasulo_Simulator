import bus.Bus;
import bus.BusListener;
import bus.Clock;
import instructions.Register;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.text.*;

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
    Queue <WBObject> WB;

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
        memory = new double[64];
        registerFile =  new Register[32];
        bus = new Bus(registerFile);
        WB = new LinkedList<>();
        status = new InstructionStatus(queue.size());
        loadBuffer = new LoadBuffer(clock , loadBufferLength, bus, loadCycles, status, memory, WB);
        storeBuffer = new StoreBuffer(clock ,storeBufferLength, storeCycles, bus, memory, status, WB);
        addSubStation = new ReservationStation(clock ,addSubCycles,0, addSubLength, bus, status, WB);
        mulDivStation= new ReservationStation(clock , 0, mulDivCycles , mulDivLength, bus, status, WB);
        bus.setListeners(new BusListener[]{loadBuffer,storeBuffer,addSubStation,mulDivStation});


        //initializing register file registers
        for(int i = 0 ; i < registerFile.length ; i++){
            registerFile[i] = new Register();
        }
        memory[10] = 15;
        memory[40] = 88;
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

                System.out.println("Cycle " + clock.getCycles());

                //issue
                if (!queue.isEmpty()) {
                    InstructionCompiler nextInstruction = queue.peek();
                    Register firstOperand = registerFile[nextInstruction.getFirstOperand()];
                    Register secondOperand = registerFile[nextInstruction.getSecondOperand()];
                    int destination = nextInstruction.getDestinationRegister();
                    Register destinationRegister = registerFile[destination];
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
                            System.out.println(firstOperand);
                            issued = loadBuffer.issue(destinationRegister, address);
                            break;

                        case "SD":
                            issued = storeBuffer.issue(destinationRegister, address);
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


                //Write Back
                if(!WB.isEmpty()){

                    WBObject wb = WB.poll();
                    // if store
                    if(wb.store) {
                        // memory[address] = destination.getValue();
                        memory[wb.address] = wb.result;
                        wb.stationTable[wb.stationIndex][3] = false;
    //                    System.out.println("memory" + memory[0]);
                    }
                    else {
                        bus.notify(wb.stationTag, wb.result);
                        wb.stationTable[wb.stationIndex][0] = false;
                    }
                    System.out.println(" ");
                    System.out.println("Station "+ wb.stationTag + " is writing on the bus");

                    status.statusTable[wb.instructionIndex][4]= clock.getCycles();
                }
                //execute
                mulDivStation.executeMulDiv();
                addSubStation.executeAddSub();
                loadBuffer.execute();
                storeBuffer.execute();



            // Printing Instruction Status
            System.out.println();
            System.out.println("Instruction Status:");
            printStatusTable(status.statusTable);
            System.out.println();


            // Printing Register File
            System.out.println();
            System.out.println("Register File:");
            printRegFile(registerFile);
            System.out.println();


            // Printing ADD/ SUB Reservation Station
            System.out.println();
            System.out.println("ADD/ SUB Reservation Station:");
            printASReservationStations(addSubStation.getTable());
            System.out.println();

                // Printing MUL/ DIV Reservation Station
                System.out.println();
                System.out.println("MUL/ DIV Reservation Station:");
                printMDReservationStations(mulDivStation.getTable());
                System.out.println();

                // Printing Load Buffer
                System.out.println();
                System.out.println(" Load Buffer:");
                printLoad(loadBuffer.getTable());
                System.out.println();

                // Printing Store Buffer
                System.out.println();
                System.out.println("Store Buffer:");
                printStore(storeBuffer.getTable());
                System.out.println();

                // Printing Memory
                System.out.println();
                System.out.println("Memory:");
                System.out.println();
                printMemory(memory);
                System.out.println();



                System.out.println("***********************************************************************************************");
            }
            else{
                System.exit(0);
            }
        }
    }

    public static void printRegFile(Register [] registerFile){
        System.out.print("R    Value    Q       ");
        System.out.print("R    Value    Q       ");
        System.out.print("R    Value    Q       ");
        System.out.print("R    Value    Q       ");
        System.out.println("");
        for(int i=0; i<32; i+=4){
            System.out.print("R" +i +":   "+registerFile[i].getValue() +"   " + registerFile[i].getInstruction() +"      ");
            System.out.print("R" +(i+1) +":   "+registerFile[i+1].getValue() +"   " + registerFile[i+1].getInstruction() +"      ");
            System.out.print("R" +(i+2)  +":   "+registerFile[i+2].getValue() +"   " + registerFile[i+2].getInstruction() +"      ");
            System.out.print("R" +(i+3) +":   "+registerFile[i+3].getValue() +"   " + registerFile[i+3].getInstruction() +"      ");
            System.out.println();
        }

    }

    public static void printMemory(double [] memory){
        for(int i=0; i<memory.length; i+=4){
            System.out.print("Memory Address " +i +": "+memory[i] +"   ");
            System.out.print("Memory Address " +(i+1) +": "+memory[i+1] +"   ");
            System.out.print("Memory Address " +(i+2) +": "+memory[i+2] +"   ");
            System.out.print("Memory Address " +(i+3) +": "+memory[i+3] +"   ");
            System.out.println();
        }
    }

    public static void printASReservationStations(Object [][] status){
        System.out.print("Tag   Busy   Vj    Vk    Qj    Qk");
        System.out.println("");
        for(int i=0; i<status.length; i++){
            System.out.println("A"+i+"   "+ status[i][0] +",  " + status[i][2] +",  " + status[i][3]+",  " + status[i][4] +",  " + status[i][5]);
        }
    }
    public static void printMDReservationStations(Object [][] status){
        System.out.print("Tag   Busy   Vj    Vk    Qj    Qk");
        System.out.println("");
        for(int i=0; i<status.length; i++){
            System.out.println("M"+i+"   "+ status[i][0] +",  " + status[i][2] +",  " + status[i][3]+",  " + status[i][4] +",  " + status[i][5]);
        }
    }
    public static void printStatusTable(Object [][] status){
        for(int i=0; i<status.length; i++){
            System.out.println(Arrays.toString(status[i]));
        }
    }
    public static void printLoad(Object [][] status){
        System.out.println("Tag   Busy   Address");
        for(int i=0; i<status.length; i++){
            System.out.println("L"+i+"   "+ status[i][0] +",     " + status[i][1]);
        }
    }
    public static void printStore(Object [][] status){
        System.out.println("Tag   Busy    Reg     Q");
        for(int i=0; i<status.length; i++){
            System.out.println("S"+i+"    "+ status[i][3] +",   " + status[i][1]+",   " + status[i][2]);
        }
    }


    public static void main(String [] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the length of the load buffer:");
        int loadBufferLength = sc.nextInt();
        System.out.println("Enter the length of the store buffer:");
        int storeBufferLength = sc.nextInt();
        System.out.println("Enter the length of the Add/Sub Reservation Station:");
        int addsub = sc.nextInt();
        System.out.println("Enter the length of the Mul/Div Reservation Station:");
        int mulDiv = sc.nextInt();
        System.out.println("Enter the number of cycles for the add and sub instructions:");
        int addsubCycles = sc.nextInt();
        System.out.println("Enter the number of cycles for the mul and div instructions:");
        int mulDivCycles = sc.nextInt();
        System.out.println("Enter the number of cycles for the load instructions:");
        int loadCycles = sc.nextInt();
        System.out.println("Enter the number of cycles for the store instructions:");
        int storeCycles = sc.nextInt();
        Main main = new Main("programme.txt", loadBufferLength, storeBufferLength, addsub, mulDiv, addsubCycles, mulDivCycles, loadCycles, storeCycles);
        main.executeProgram();
    }

}


