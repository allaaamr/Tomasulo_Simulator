import bus.Bus;
import bus.BusListener;
import bus.Clock;
import instructions.AddSubInstruction;
import instructions.LoadStore;
import instructions.Register;

import java.util.Queue;

public class StoreBuffer implements BusListener {

    private Clock clock ;
    final private int length;
    private Object [][] table;
    private int storeCycles ;
    Bus bus;
    double [] memory;
    InstructionStatus status;
    Queue<WBObject> WB;

    public StoreBuffer(Clock clock , int length,int storeCycles, Bus bus, double [] memory , InstructionStatus status, Queue<WBObject> WB) {
        this.clock = clock;
        this.length = length;
        this.table = new Object[length][6];
        this.storeCycles =  storeCycles;
        this.bus = bus;
        this.memory = memory;
        this.status=status;
        this.WB= WB;

        // column 0-> memory address
        // column 1-> register
        // column 2-> waiting register
        // column 3-> busy
        // column 4-> remaining cycles
        // column 5-> instruction

        // SD   FO, 100
    }
    public boolean issue(Register register, int address) {
        LoadStore instruction = new LoadStore("", register, address, false, bus, memory);
        for (int i = 0; i < this.length; i++) {
            if(table[i][3] == null || !(boolean) table[i][3]){
                instruction.setStation("S"+i);
                table[i][0] = address;
                table[i][1] = register.isReady() ? register.getValue() : null;
                table[i][2] = !(register.isReady()) ? register.getInstruction() : null;
                table[i][3] = true;
                table[i][4] = storeCycles;
                table[i][5] = instruction;

                //Instruction issued successfully.
                int index = status.getLastIndex();
                status.statusTable [index][5] = "S"+i;
                return true;
            }
        }
        return false;
    }
    /*if we keep track of the location of the instruction in the table then we will just send the row number to this method and
    remove the loop */
    public void execute(){
        for(int j = 0 ; j < this.length; j++){

            // if not busy continue
            if(table[j][3] == null || !((boolean) table[j][3]))
                continue;


            // if it is just issued continue
            LoadStore ins = (LoadStore) table[j][5];
            int instructionIndex = status.stationIndex( ins.getStation() );
            boolean justIssued = (int) status.statusTable[instructionIndex][1] == clock.getCycles();

            if ( table[j][1] != null) {
                if (justIssued || status.statusTable[instructionIndex][3] != null)
                    continue;

                // I will start executing now  storeCycles === table[i][4]
                if (storeCycles == (int) table[j][4]) {
                    table[j][4] = (int) table[j][4] - 1;
                    //mark this instruction in the status table that it started executing
                    status.statusTable[instructionIndex][2] = clock.getCycles();
                }

                // Still executing table[i][4]>0
                // table[i][4]-1
                if ((int) table[j][4] > -1) {
                    table[j][4] = (int) table[j][4] - 1;
                }

                // finished executing table[i][4]==0
                // execute
                // add end of execution in status table
                if ((int) table[j][4] == -1) {
                    double regValue = ins.execute();
                    String station = ins.getStation();
                    WBObject wb = new WBObject(station, regValue, instructionIndex, table, j, true, (int) table[j][0]);
                    WB.add(wb);
                    //mark this instruction in the status table as done
                    status.statusTable[instructionIndex][3] = clock.getCycles();
                    // clear the table
                    table[j][3] = false;
                }
            }
        }
    }

    public Object[][] getTable(){
        return table;
    }

    @Override
    public void update(String instruction,double updatedValue) {
        for(Object row []: table){
            if (row[2]!= null && row[2].equals(instruction)){
                row[2] = null;
                row[1] = updatedValue ;
            }
        }
    }
}


