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
    private int busyStations ;

    public ReservationStation (Clock clock ,Integer addSubCycles ,Integer mulDivCycles,Integer stationLength, Bus bus){
        this.length= stationLength;
        this.table = new Object[length][7];
        this.bus = bus;
        this.clock = clock;
        this.addSubCycles = addSubCycles;
        this.mulDivCycles = mulDivCycles;

        // column 0 -> Busy
        // column 1 -> Op
        // column 2 -> Vj
        // column 3 -> Vk
        // column 4 -> Qj
        // column 5 -> Qk
        // column 6 -> Remaining cycles for execution
    }


    public boolean issueAddSub(Register rd, Register rs, Register rt, boolean type ){
        AddSubInstruction instruction = new AddSubInstruction("" , type, bus);
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
                System.out.println(Arrays.toString(table[i])) ;
                busyStations++;
                bus.pushWaitingInstruction(instruction.getStation(),rd);
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void executeAddSub() {
        for (Object row[] : table){
            if (row[2] != null && row[3] != null){
                row[6] = (int)row[6]-1;
                if ((int)row[6] == 0){
                    AddSubInstruction ins = (AddSubInstruction) row[1];
                    ins.compute((double)row[2],(double)row[3]) ;
                    row[0] = false; // not busy now
                    busyStations--;
                }
                break;
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
                // Instruction issued successfully.
                System.out.println(Arrays.toString(table[i])) ;
                busyStations++;
                bus.pushWaitingInstruction(instruction.getStation(),rd);
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void executeMulDiv() {
        for (Object row[] : table){
            if (row[2] != null && row[3] != null){
                row[6] = (int)row[6]-1;
                System.out.println(((MultiplyDivInstruction)row[1]).getStation() +" still have "+row[6]+ " cycles to execute");
                if ((int)row[6] == 0){
                    MultiplyDivInstruction ins = (MultiplyDivInstruction) row[1];
                    for (Object r []:  table)
                        System.out.println(Arrays.toString(r));
                    System.out.println((double)row[2]+"  " +(double)row[3]);
                    ins.compute((double)row[2],(double)row[3]) ;
                    row[0] = false; // not busy now
                    busyStations--;
                }
                break;
            }
        }
    }

    @Override
    public void update(String  instruction, double updatedValue) {
        System.out.println("UPDATE: " +instruction + " with: "+ updatedValue);
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

    public boolean finishedExecution () {
        return busyStations == 0 ;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

    public Object[][] getTable() {
        return table;
    }


}