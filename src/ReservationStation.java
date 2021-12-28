import bus.*;
import instructions.*;

public class ReservationStation implements BusListener{
    private Object[][] table;
    private Integer length;
    private Bus bus;
    public ReservationStation (Integer length, Bus bus){
        this.length= length;
        this.table = new Object[length][7];
        this.bus = bus;
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
                this.table[i][1] = a.getOp();
//                this.table[i][2] = RegisterFile[rs][0] instanceof Boolean ? RegisterFile[rs][1] : null;
//                this.table[i][3] = RegisterFile[rt][0] instanceof Boolean ? RegisterFile[rt][1] : null;
//                this.table[i][4] = RegisterFile[rs][0] instanceof Boolean ? null : RegisterFile[rs][1] ;
//                this.table[i][5] = RegisterFile[rt][0] instanceof Boolean ? null : RegisterFile[rt][1] ;
                this.table[i][6] = a.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public boolean issueMulDiv(MultiplyDivInstruction m , Object [][] RegisterFile, int rs, int rt ){

        for(int i = 0 ; i < this.length ; i++){
            if(!(boolean) this.table[i][0]){
                this.table[i][0] = true;
                this.table[i][1] = m.getOp();
                this.table[i][2] = RegisterFile[rs][0] instanceof Boolean ? RegisterFile[rs][1] : null;
                this.table[i][3] = RegisterFile[rt][0] instanceof Boolean ? RegisterFile[rt][1] : null;
                this.table[i][4] = RegisterFile[rs][0] instanceof Boolean ? null : RegisterFile[rs][1] ;
                this.table[i][5] = RegisterFile[rt][0] instanceof Boolean ? null : RegisterFile[rt][1] ;
                this.table[i][6] = m.getExecutionCycles();
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