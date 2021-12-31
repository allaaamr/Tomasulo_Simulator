import bus.BusListener;
import bus.Clock;
import instructions.Register;

public class StoreBuffer implements BusListener {

    private Clock clock ;
    final private int length;
    private Object [][] table;

    public StoreBuffer(Clock clock , int length) {
        this.clock = clock;
        this.length = length;
        this.table = new Object[length][5];

        // column 0-> memory address
        // column 1-> register
        // column 2-> waiting register
        // column 3-> busy
        // column 4-> remaining cycles
    }

    public boolean issue(Register register, int address) {

        for (int i = 0; i < this.length; i++) {
            if(!(boolean) table[i][3]){
                table[i][0] = address;
                table[i][1] = register.isReady() ? register.getValue() : null;
                table[i][2] = !(register.isReady()) ? register.getInstruction() : null;
                table[i][3] = true;
               //table[i][4] = cycles;
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
                table [j][4] = null;
            }
        }
    }
    public Object[][] getTable(){
        return table;
    }

    @Override
    public void update(Integer register, Integer updatedValue) {

    }
}


