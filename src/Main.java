import bus.Bus;
import bus.Clock;
import instructions.Register;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Main{

    Clock clock;
    double memory [] ;
    Register[] registerFile ;
    Bus bus;

    LoadBuffer loadBuffer;
    StoreBuffer storeBuffer;
    ReservationStation AddSubStation;
    ReservationStation MulDivStation;
    Queue <InstructionCompiler> queue;


    public Main(String path, int loadBufferLength, int storeBufferLength, int addSubLength, int mulDivLength,
                int addSubCycles, int mulDivCycles, int loadCycles, int storeCycles
    ) throws IOException{

        clock = new Clock();
        memory = new double[2048];
        registerFile =  new Register[32];
        loadBuffer = new LoadBuffer(clock , loadBufferLength, bus);
        storeBuffer = new StoreBuffer(clock ,storeBufferLength);
        AddSubStation = new ReservationStation(clock ,addSubCycles,0, addSubLength, bus);
        MulDivStation= new ReservationStation(clock , 0, mulDivCycles , mulDivLength, bus);
        bus = new Bus(registerFile,loadBuffer,storeBuffer,AddSubStation,MulDivStation);

        //initializing register file registers
        for(int i = 0 ; i < registerFile.length ; i++){
            registerFile[i] = new Register();
        }

        registerFile[0].updateRegister(50);
        registerFile[1].updateRegister(70);
        registerFile[2].updateRegister(20);
        registerFile[3].updateRegister(1);

        queue = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;

        //filling the instruction queue from a file
        while((line = br.readLine()) != null){
            queue.add(new InstructionCompiler(line));
        }
        System.out.println(queue);
    }

    public void executeProgram(){
        while(!queue.isEmpty()){

            clock.updateClock();
            System.out.println("***********************************************************************************************");
            System.out.println(clock.getCycles());
            InstructionCompiler nextInstruction = queue.peek();
            System.out.println(queue.peek().toString());

            Register firstOperand = registerFile[nextInstruction.getFirstOperand()];
            Register secondOperand = registerFile[nextInstruction.getSecondOperand()];
            int destination = nextInstruction.getDestinationRegister();
            int address = nextInstruction.getMemoryAddress();

            boolean issued = false;

            switch (nextInstruction.getInstructionType()){

                case "ADD":
                    issued = AddSubStation.issueAddSub(destination, firstOperand, secondOperand ,true);
                    break;

                case "SUB":
                    issued = AddSubStation.issueAddSub(destination, firstOperand, secondOperand ,false);
                    break;

                case "MUL":
                    issued = MulDivStation.issueMulDiv(destination, firstOperand, secondOperand ,true);
                    break;

//                case "DIV":
//                    issued = MulDivStation.issueMulDiv(destination, firstOperand, secondOperand ,false);
//                    break;
//
//                case "LD":
//                    issued = loadBuffer.issue(destination,address);
//                    break;
//
//                case "SD":
//                    issued = storeBuffer.issue(firstOperand,destination);
//                    break;
            }

            if(issued) {
                System.out.println("instruction issued");
                queue.poll();
            }
            else
                System.out.println("Failed to Issue");

        }

        AddSubStation.executeAddSub();
        MulDivStation.executeMulDiv();
        //        loadBuffer.execute();
        //        storeBuffer.execute() ;

        System.out.println("***********************************************************************************************");

    }


    public static void main(String [] args) throws IOException {
        Main main = new Main("programme.txt",5,5,5,5,2,5,8,8);
        main.executeProgram();
    }
}


