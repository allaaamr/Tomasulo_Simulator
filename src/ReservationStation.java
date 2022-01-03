import bus.*;
import instructions.*;

import java.util.Arrays;
import java.util.Queue;
import java.util.logging.Level;

public class ReservationStation implements BusListener{
    private Object[][] table;
    private Integer length;
    private Bus bus;
    private Clock clock;
    private int addSubCycles ;
    private int mulDivCycles ;
    Queue<WBObject> WB;
    InstructionStatus status;


    public ReservationStation (Clock clock ,Integer addSubCycles ,Integer mulDivCycles,Integer stationLength, Bus bus, InstructionStatus status,  Queue <WBObject> WB ){
        this.length= stationLength;
        this.table = new Object[length][7];
        this.bus = bus;
        this.clock = clock;
        this.addSubCycles = addSubCycles;
        this.mulDivCycles = mulDivCycles;
        this.status = status;
        this.WB = WB;
        // column 0 -> Busy
        // column 1 -> Instruction
        // column 2 -> Vj
        // column 3 -> Vk
        // column 4 -> Qj
        // column 5 -> Qk
        // column 6 -> Remaining cycles for execution

    }

    public boolean issueAddSub(Register rd, Register rs, Register rt, boolean type ){
        AddSubInstruction instruction = new AddSubInstruction("" ,type, bus);
        for(int i = 0 ; i < this.length ; i++){
            if(table[i][0] == null || !(boolean) this.table[i][0]){
                instruction.setStation("A"+i);
                this.table[i][0] = true;
                this.table[i][1] = instruction;
                this.table[i][2] = rs.isReady() ? rs.getValue() : null;
                this.table[i][3] = rt.isReady() ? rt.getValue() : null;
                this.table[i][4] = rs.isReady() ? null :  rs.getInstruction();
                this.table[i][5] = rt.isReady() ? null :  rt.getInstruction();
                this.table[i][6] = addSubCycles;

                //Instruction issued successfully.
                int index = status.getLastIndex();
                status.statusTable [index][5] = "A"+i;
                bus.pushWaitingInstruction(instruction.getStation(),rd);
                rd.setReady(false);
                rd.setInstruction("A"+i);
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void executeAddSub() {
        for (int i=0; i<length; i++){

            if(table[i][0] == null || !((boolean) table[i][0]))
                continue;

            AddSubInstruction ins = (AddSubInstruction) table[i][1];
            int instructionIndex = status.stationIndex( ins.getStation() );
            boolean justIssued = (int) status.statusTable[instructionIndex][1] == clock.getCycles();

            if(justIssued || status.statusTable[instructionIndex][3]!=null)
                continue;

            if ( ((boolean) table[i][0]) && table[i][2] != null && table[i][3] != null){

                if( (int) table[i][6] == this.addSubCycles) {
                    table[i][6] = (int) table[i][6] - 1;
                    //mark this instruction in the status table that it started executing
                    status.statusTable[instructionIndex][2]= clock.getCycles();
                }

                //instruction is still executing, edit remaining cycles
                else if( (int) table[i][6] > 0)
                    table[i][6] = (int) table[i][6] - 1;

                //instruction finished executing
                if( (int) table[i][6] == 0){
                    String station = ins.getStation();
                    double result = ins.compute((double) table[i][2], (double) table[i][3]);
                    WBObject wb = new WBObject(station, result, instructionIndex,this.table, i, false, -1 );
                    WB.add(wb);
                    //mark this instruction in the status table as done
                    status.statusTable[instructionIndex][3]= clock.getCycles();
                }

            }
        }
    }

    public boolean issueMulDiv(Register rd, Register rs, Register rt, boolean type){
        MultiplyDivInstruction instruction = new MultiplyDivInstruction("", type, bus);
        for(int i = 0 ; i < this.length ; i++){
            if(table[i][0] == null || !(boolean) this.table[i][0]){
                instruction.setStation("M"+i);
                this.table[i][0] = true;
                this.table[i][1] = instruction;
                this.table[i][2] = rs.isReady() ? rs.getValue() : null;
                this.table[i][3] = rt.isReady() ? rt.getValue() : null;
                this.table[i][4] = rs.isReady() ? null :  rs.getInstruction();
                this.table[i][5] = rt.isReady() ? null :  rt.getInstruction();
                this.table[i][6] = mulDivCycles;
                //Instruction issued successfully.
                int index = status.getLastIndex();
                status.statusTable [index][5] = "M"+i;
                bus.pushWaitingInstruction(instruction.getStation(),rd);
                rd.setReady(false);
                rd.setInstruction("M"+i);
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void executeMulDiv() {

        for (int i=0; i<length; i++){

            if(table[i][0] == null || !((boolean) table[i][0]))
                continue;


            MultiplyDivInstruction ins = (MultiplyDivInstruction) table[i][1];
            int instructionIndex = status.stationIndex( ins.getStation() );
            boolean justIssued = (int) status.statusTable[instructionIndex][1] == clock.getCycles();

            if(justIssued || status.statusTable[instructionIndex][3]!=null)
                continue;

            if ( ((boolean) table[i][0]) && table[i][2] != null && table[i][3] != null){
                /// It will start executing
                if( (int) table[i][6] == this.mulDivCycles) {
                    table[i][6] = (int) table[i][6] - 1;
                    //mark this instruction in the status table that it started executing
                    status.statusTable[instructionIndex][2]= clock.getCycles();
                }

                //instruction is still executing, edit remaining cycles
                else if( (int) table[i][6] > 0)
                    table[i][6] = (int) table[i][6] - 1;

                //instruction finished executing
                if( (int) table[i][6] == 0){
                    String station = ins.getStation();
                    double result =ins.compute((double) table[i][2], (double) table[i][3]);
                    WBObject wb = new WBObject(station, result, instructionIndex, table, i, false, -1);
                    WB.add(wb);
                    //mark this instruction in the status table as done
                    status.statusTable[instructionIndex][3]= clock.getCycles();


                }
            }
        }
    }

    @Override
    public void update(String  instruction, double updatedValue) {
        for(Object row []: table){
            if (row[4]!= null && row[4].equals(instruction)){
                row[4] = null;
                row[2] = updatedValue ;
            }

            if (row[5]!= null && row[5].equals(instruction)){
                row[5] = null;
                row[3] = updatedValue ;
            }
        }
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Object[][] getTable() {
        return table;
    }


}