import bus.Bus;
import bus.BusListener;
import bus.Clock;

public class LoadBuffer implements BusListener {

    private final int length;
    private Object [][] table ;
    private Bus bus;
    private Clock clock;

    public LoadBuffer(Clock clock , Integer length, Bus bus){

        this.clock = clock;
        this.length = length;
        this.bus = bus ;
        this.table = new Object[length][3];
        // column 0 -> Busy
        // column 1 -> Address
        // column 2 -> Remaining cycles for execution
    }

    public boolean issue(int destination, int address){
        for(int i = 0 ; i < this.length ; i++){
            if(! ((boolean) this.table[i][0])){
                this.table[i][0] = true;
                this.table[i][1] = address;
                //this.table[i][2] = l.getExecutionCycles();
                // Instruction issued successfully.
                return true;
            }
        }
        // No available slots.
        return false;
    }

    @Override
    public void update(String instruction,double updatedValue) {

    }
}
