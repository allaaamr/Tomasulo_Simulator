import operators.LoadStore;
public class LoadBuffer {
    private final int length;
    private final Object [][] table;

    public LoadBuffer(int length){
        this.length = length;
        this.table = new Object[length][3];
        // column 0 -> Busy
        // column 1 -> Address
        // column 2 -> Remaining cycles for execution

    }

    public boolean issue(LoadStore l){
        for(int i = 0 ; i < this.length ; i++){
            if(!(boolean) this.table[i][0]){
                this.table[i][0] = true;
                this.table[i][1] = l.getMemLocation();
                this.table[i][2] = l.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public int getLength() { return length; }

    public Object[][] getTable() { return table; }
}
