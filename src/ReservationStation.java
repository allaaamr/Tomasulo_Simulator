public class ReservationStation {
    private boolean type; // true --> Add/Sub    , false --> Mul/Div
    private Object[][] table;
    private Integer length;

    public void setLength(Integer length) {
        this.length = length;
    }

    public Object[][] getTable() {
        return table;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public ReservationStation (boolean type , Integer length){
        this.type = type;
        this.length= length;
        this.table = new Object[length][7];
        // column 0 -> Busy
        // column 1 -> Op
        // column 2 -> Vj
        // column 3 -> Vk
        // column 4 -> Qj
        // column 5 -> Qk
        // column 6 -> Remaining cycles for execution
    }
    public boolean issueAddSub(AddSubInstruction a, Object [][] RegisterFile ){
        int rs = a.getRs();
        int rt = a.getRt();

        for(int i = 0 ; i < this.length ; i++){
            if(!(boolean) this.table[i][0]){
                this.table[i][0] = true;
                this.table[i][1] = a.getOp();
                this.table[i][2] = RegisterFile[rs][0] instanceof Boolean ? RegisterFile[rs][1] : null;
                this.table[i][3] = RegisterFile[rt][0] instanceof Boolean ? RegisterFile[rt][1] : null;
                this.table[i][4] = RegisterFile[rs][0] instanceof Boolean ? null : RegisterFile[rs][1] ;
                this.table[i][5] = RegisterFile[rt][0] instanceof Boolean ? null : RegisterFile[rt][1] ;
                this.table[i][6] = a.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public boolean issueMulDiv(MulDivInstruction m , Object [][] RegisterFile ){
        int rs = m.getRs();
        int rt = m.getRt();

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




}