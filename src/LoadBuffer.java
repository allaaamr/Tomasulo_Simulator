import bus.Bus;
import bus.BusListener;
import bus.Clock;
import instructions.LoadStore;
import instructions.Register;

import java.util.Queue;

public class LoadBuffer implements BusListener {

    private final int length;
    private Object [][] table ;
    private Bus bus;
    private Clock clock;
    private int loadCycles ;
    double [] memory;
    InstructionStatus status;
    Queue<WBObject> WB;

    public LoadBuffer(Clock clock , Integer length, Bus bus, int loadCycles ,InstructionStatus status, double [] memory, Queue<WBObject> WB){

        this.clock = clock;
        this.length = length;
        this.bus = bus ;
        this.table = new Object[length][4];
        this.memory = memory;
        this.status=status;
        this.loadCycles= loadCycles;
        this.WB =WB;
        // column 0 -> Busy
        // column 1 -> Address
        // column 2 -> Remaining cycles for execution
        // column 3 -> instruction
    }

    public boolean issue(Register destination, int address){
        LoadStore instruction = new LoadStore("", destination, address, true, bus, memory);
        for(int i = 0 ; i < this.length ; i++){
            if(table[i][3] == null || !((boolean) this.table[i][0])){
                instruction.setStation("L"+i);
                this.table[i][0] = true;
                this.table[i][1] = address;
                this.table[i][2] = loadCycles;
                this.table[i][3] = instruction;
                // Instruction issued successfully.
                bus.pushWaitingInstruction(instruction.getStation(),destination);
                destination.setReady(false);
                destination.setInstruction("L"+i);
                int index = status.getLastIndex();
                status.statusTable [index][5] = "L"+i;
                return true;
            }
        }
        // No available slots.
        return false;
    }

    public void execute(){
        for(int j = 0 ; j < this.length; j++){

            // if not busy continue
            if(table[j][0] == null || !((boolean) table[j][0]))
                continue;

            // if it is just issued continue
            LoadStore ins = (LoadStore) table[j][3];
            int instructionIndex = status.stationIndex( ins.getStation() );
            boolean justIssued = (int) status.statusTable[instructionIndex][1] == clock.getCycles();

            if(justIssued || status.statusTable[instructionIndex][3]!=null)
                continue;

            // I will start executing now
            if( loadCycles == (int )table[j][2]) {
                table[j][2] = (int) table[j][2] - 1;
                //mark this instruction in the status table that it started executing
                status.statusTable[instructionIndex][2]= clock.getCycles();
            }


            // Still executing
            else if( (int )table[j][2] > 0) {
                table[j][2] = (int) table[j][2] - 1;
            }
            // finished executing table[i][4]==0
            // execute
            // add end of execution in status table
            if( (int )table[j][2] == 0 ) {
                ins.execute();
                //mark this instruction in the status table as done
                status.statusTable[instructionIndex][3]= clock.getCycles();
                //Add to queue of WB
                String station = ins.getStation();
                double result = ins.execute();
                WBObject wb = new WBObject(station, result, instructionIndex, table, j, false, -1);
                WB.add(wb);
            }

        }
    }

    public Object[][] getTable() {return table;}

    @Override
    public void update(String instruction,double updatedValue) {

    }
}
