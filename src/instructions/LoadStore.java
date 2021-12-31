package instructions;
import bus.Bus;

public class LoadStore {

    private Integer address;// memory location
    private Boolean op; // true -> load ,,, false -> store
    private Register destination;
    private double [] memory;
    private Bus bus ;

    public LoadStore(Register destination, int address, Boolean op, Bus bus, double []memory){
        this.address = address;
        this.destination = destination;
        this.op = op;
        this.bus = bus;
        this.memory = memory;
    }

    public void execute(){
        if(op){
            destination.setValue(memory[address]);
        }
        else
            memory[address] = destination.getValue();
    }

    public Integer getAddress() {
        return address;
    }

    public void setAddress(Integer address) {
        this.address = address;
    }

    public Boolean getOp() {
        return op;
    }

    public void setOp(Boolean op) {
        this.op = op;
    }

    public Register getDestination() {
        return destination;
    }

    public void setDestination(Register destination) {
        this.destination = destination;
    }

}
