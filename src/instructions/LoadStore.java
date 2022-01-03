package instructions;
import bus.Bus;

public class LoadStore {

    private String station;
    private Integer address;// memory location
    private Boolean op; // true -> load ,,, false -> store
    private Register destination;
    private double [] memory;
    private Bus bus ;

    public LoadStore(String station,Register destination, int address, Boolean op, Bus bus, double []memory){
        this.station = station;
        this.address = address;
        this.destination = destination;
        this.op = op;
        this.bus = bus;
        this.memory = memory;
    }

    public double execute(){
        //LOAD
        if(op){
            // removed from here because the load writes back to the register file in the write back stage and not in the execution stage
//            destination.setValue(memory[address]);
            return memory[address];
        }
        //STORE
        else
            return destination.getValue();

    }


    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
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
