public class WBObject {

    String stationTag;
    double result;
    int instructionIndex;
    Object[][] stationTable;
    int stationIndex;
    boolean store;
    int address;

    public WBObject(String stationTag,   double result, int instructionIndex,  Object[][]  stationTable,int stationIndex, boolean store, int address){
        this.stationTag = stationTag;
        this.result = result;
        this.instructionIndex = instructionIndex;
        this.stationTable = stationTable;
        this.stationIndex= stationIndex;
        this.store = store;
        this.address= address;
    }
}
