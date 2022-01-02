public class InstructionStatus {

    Object [][] statusTable ;
    int rows;
    private int lastIndex;
    // 0 -> Instruction
    // 1 -> Issue
    // 2 -> Start Exec
    // 3 -> End Exec
    // 4 -> Write Back
    // 5 -> Tag ( M1/A2...)

    public InstructionStatus (int rows) {
        this.rows = rows;
        this.lastIndex = 0;
        statusTable = new Object [rows][6];
    }

    public Object[][] getStatusTable() {
        return statusTable;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int stationIndex ( String tag){
        for( int i = 0; i < this.rows; i++){
            if ((this.statusTable[i][5]).equals(tag) && this.statusTable[i][4]==null)
                return i;
        }
        return -1;
    }
    public boolean finishedProgram(){
        for( Object row[]: statusTable){
            if (row[4] == null)
                return false;
        }
        return true;
    }

}
