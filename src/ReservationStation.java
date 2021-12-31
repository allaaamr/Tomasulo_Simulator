import bus.*;
import instructions.*;

public class ReservationStation implements BusListener{
    private Object[][] table;
    private Integer length;
    private Bus bus;
    private Clock clock;

    public ReservationStation (Clock clock , Integer length, Bus bus){
        this.length= length;
        this.table = new Object[length][7];
        this.bus = bus;
        this.clock = clock;
        // column 0 -> Busy
        // column 1 -> Op
        // column 2 -> Vj
        // column 3 -> Vk
        // column 4 -> Qj
        // column 5 -> Qk
        // column 6 -> Remaining cycles for execution
    }

    public boolean issueAddSub(int rd, Register rs, Register rt, boolean type ){
        AddSubInstruction instruction = new AddSubInstruction(rd, type, bus);
        for(int i = 0 ; i < this.length ; i++){
            if(!(boolean) this.table[i][0]){
                this.table[i][0] = true;
                this.table[i][1] = type;
                this.table[i][2] = rs.isReady() ? rs.getValue() : null;
                this.table[i][3] = rt.isReady() ? rt.getValue() : null;
                this.table[i][4] = rs.isReady() ? null :  rs.getInstruction();
                this.table[i][5] = rt.isReady() ? null :  rt.getInstruction();
//              this.table[i][6] = instruction.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public boolean issueMulDiv(int rd, Register rs, Register rt, boolean type){
        MultiplyDivInstruction instruction = new MultiplyDivInstruction(rd, type, bus);
        for(int i = 0 ; i < this.length ; i++){
            if(!(boolean) this.table[i][0]){
                this.table[i][0] = true;
                this.table[i][1] = type;
                this.table[i][2] = rs.isReady() ? rs.getValue() : null;
                this.table[i][3] = rt.isReady() ? rt.getValue() : null;
                this.table[i][4] = rs.isReady() ? null :  rs.getInstruction();
                this.table[i][5] = rt.isReady() ? null :  rt.getInstruction();
//              this.table[i][6] = m.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Object[][] getTable() {
        return table;
    }


    @Override
    public void update(Integer register, Integer updatedValue) {

    }
}