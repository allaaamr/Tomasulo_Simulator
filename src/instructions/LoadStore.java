import bus.Register;
package instructions;

public class LoadStore {

    private Integer address;// memory location
    private Boolean op; // true -> load ,,, false -> store
    Register destination;
    private int [] memory; // TODO: memory file .. to be discussed

    public LoadStore(Register destination, int address, Boolean op, int []memory){
        this.address = address;
        this.destination = destination;
        this.op = op;
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

    public int[] getMemory() {
        return memory;
    }

    public void setMemory(int[] memory) {
        this.memory = memory;
    }
}
