import operators.LoadStore;

public class StoreBuffer {
    private Object [][] table;
    final private int length;

    public StoreBuffer(int length) {
        this.table = new Object[length][5];
        this.length = length;
        // column 0-> memory address
        // column 1-> register
        // column 2-> waiting register
        // column 3-> busy
        // column 4-> remaining cycles
    }

    public boolean issue(LoadStore instruction ,Object [][] RegisterFile ) {
        int registerNumber ;
        for (int i = 0; i < this.length; i++) {
            if (!(boolean) table[i][3]) {
                registerNumber = instruction.getRegNumber();
                table[i][0] = instruction.getMemLocation();
                table[i][1] = RegisterFile[registerNumber][0] instanceof Boolean ? RegisterFile[registerNumber][0] : null;
                table[i][2] = RegisterFile[registerNumber][0] instanceof Boolean ? null : RegisterFile[registerNumber][0];
                table[i][3] = true;
                table[i][4] = instruction.getExecutionCycles();
                return true;
            }
        }
        return false;
    }
   /*if we keep track of the location of the instruction in the table then we will just send the row number to this method and
   remove the loop */
    public void finishExecution(){
        for(int j = 0 ; j < this.length; j++){
            if((Integer) table[j][4] == 0){
                table[j][0] = null;
                table[j][1] = null;
                table [j][2] = null;
                table[j][3] = false;
                table [j][4] = false;
            }
        }
    }


    public Object[][] getTable(){
        return table;
    }


}


