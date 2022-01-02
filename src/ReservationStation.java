import bus.*;
import instructions.*;

import java.util.Arrays;
import java.util.logging.Level;

public class ReservationStation implements BusListener{
    private Object[][] table;
    private Integer length;
    private Bus bus;
    private Clock clock;
    private int addSubCycles ;
    private int mulDivCycles ;
    InstructionStatus status;


    public ReservationStation (Clock clock ,Integer addSubCycles ,Integer mulDivCycles,Integer stationLength, Bus bus, InstructionStatus status){
        this.length= stationLength;
        this.table = new Object[length][7];
        this.bus = bus;
        this.clock = clock;
        this.addSubCycles = addSubCycles;
        this.mulDivCycles = mulDivCycles;
        this.status = status;
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
        for (Object row[] : table){
            if(row[0] == null)
                continue;
            if ( ((boolean) row[0]) && row[2] != null && row[3] != null){
                /// It will start executing
                if( (int) row[6] == this.addSubCycles) {
                    row[6] = (int) row[6] - 1;
                    //mark this instruction in the status table that it started executing
                    int instructionIndex = status.stationIndex( ((AddSubInstruction)row[1]).getStation() );
                    status.statusTable[instructionIndex][2]= clock.getCycles();
                }

                //instruction is still executing, edit remaining cycles
                else if( (int) row[6] > 0)
                    row[6] = (int) row[6] - 1;

                //instruction finished executing
                else{
                    row[0] = false;
                    AddSubInstruction ins = (AddSubInstruction) row[1];
                    ins.compute((double) row[2], (double) row[3]);
                    //mark this instruction in the status table as done
                    int instructionIndex = status.stationIndex(ins.getStation() );
                    status.statusTable[instructionIndex][3]= clock.getCycles();
                    status.statusTable[instructionIndex][4]= clock.getCycles() +1;
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
        for (Object row[] : table){
            if(row[0] == null)
                continue;

            if ( ((boolean) row[0]) && row[2] != null && row[3] != null){
                /// It will start executing
                if( (int) row[6] == this.mulDivCycles) {
                    row[6] = (int) row[6] - 1;
                    //mark this instruction in the status table that it started executing
                    int instructionIndex = status.stationIndex( ((MultiplyDivInstruction)row[1]).getStation() );
                    status.statusTable[instructionIndex][2]= clock.getCycles();
                }

                //instruction is still executing, edit remaining cycles
                else if( (int) row[6] > 0)
                    row[6] = (int) row[6] - 1;

                    //instruction finished executing
                else{
                    row[0] = false;
                    MultiplyDivInstruction ins = (MultiplyDivInstruction) row[1];
                    ins.compute((double) row[2], (double) row[3]);
                    //mark this instruction in the status table as done
                    int instructionIndex = status.stationIndex(ins.getStation() );
                    status.statusTable[instructionIndex][3]= clock.getCycles();
                    // write back
                    status.statusTable[instructionIndex][4]= clock.getCycles() +1;

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