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


    public Main(String path, int loadBufferLength, int storeBufferLength, int AddSub, int MulDiv) throws IOException{

        clock.updateClock(); ;
        memory = new double[2048];
        registerFile =  new Register[32];
        bus = new Bus(registerFile);
        loadBuffer = new LoadBuffer(clock , loadBufferLength);
        storeBuffer = new StoreBuffer(clock ,storeBufferLength);
        AddSubStation = new ReservationStation(clock , AddSub, bus);
        MulDivStation= new ReservationStation(clock , MulDiv, bus);

        //initializing register file registers
        for(int i = 0 ; i < registerFile.length ; i++){
            registerFile[i] = new Register();
        }

        queue = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line;

        //filling the instruction queue from a file
        while((line = br.readLine()) != null){
            queue.add(new InstructionCompiler(line));
        }
    }

    public void executeProgram(){
        while(!queue.isEmpty()){

            clock.updateClock();
            InstructionCompiler nextInstruction = queue.peek();

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

                case "DIV":
                    issued = MulDivStation.issueMulDiv(destination, firstOperand, secondOperand ,false);
                    break;

                case "LD":
                    issued = loadBuffer.issue(destination,address);
                    break;

                case "SD":
                    issued = storeBuffer.issue(firstOperand,destination);
                    break;
            }
            if(issued)
                queue.poll();

        }


    }


    public static void main(String [] args){

    }
}


