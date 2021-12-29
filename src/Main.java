import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main{
    Register[] registerFile = new Register[32];
    Queue <InstructionCompiler> queue;
    LoadBuffer loadBuffer;
    StoreBuffer storeBuffer;
    ReservationStation AddSubStation;
    ReservationStation MulDivStation;

    public Main(String path, int loadBufferLength, int storeBufferLength, int AddSub, int MulDiv) throws IOException{
        loadBuffer = new LoadBuffer(loadBufferLength);
        storeBuffer = new StoreBuffer(storeBufferLength);
        AddSubStation = new ReservationStation(AddSub);
        MulDivStation= new ReservationStation(MulDiv);
        for(int i = 0 ; i < registerFile.length ; i++){
            registerFile[i] = new Register();
        }
        queue = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line;
        while((line = br.readLine()) != null){
            queue.add(new InstructionCompiler(line));
        }
    }

    public void executeProgram(){
        while(!queue.isEmpty()){
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


