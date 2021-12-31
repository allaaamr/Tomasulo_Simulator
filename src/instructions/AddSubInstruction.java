package instructions;
import bus.*;


public class AddSubInstruction implements BusListener{
    private Integer destination;
    private Boolean op; // true => + ,,, false => -
    private Integer result;
    private Integer executionCycles;
    private Bus bus;


    public AddSubInstruction(int destination, boolean op, Bus bus){
        this.op = op;
        this.bus = bus;
        this.destination = destination;
    }

    public Integer getExecutionCycles() {
        return executionCycles;
    }

    public void setExecutionCycles(Integer executionCycles) {
        this.executionCycles = executionCycles;
    }

    public Integer compute(int operand1, int operand2){
        if(op)
            result = operand1+operand2;
        else
            result = operand1-operand2;

        bus.notify(destination, result);
        return result;
    }

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Integer getResult() {
        return result;
    }

    public Boolean getOp() {
        return this.op;
    }

    @Override
    public void update(Integer register, Integer updatedValue) {



    }
}
