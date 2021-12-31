package instructions;
import bus.*;

public class MultiplyDivInstruction {

    private Integer destination ;
    private Integer result;
    private Boolean op; // true => multiplication ... false => division
    private Integer executionCycles;
    private Bus bus;

    public MultiplyDivInstruction(int destination, boolean op, Bus bus){
        this.op = op;
        this.bus = bus;
        this.destination = destination;
    }

    public Integer compute(int operand1, int operand2){
        if(op)
            result = operand1*operand2;
        else
            result = operand1/operand2;

        bus.notify(destination, result);
        return result;
    }

    public Integer getExecutionCycles() { return executionCycles; }

    public void setExecutionCycles(Integer executionCycles) {this.executionCycles = executionCycles;}

    public Boolean getOp() { return op;}

    public void setOp(Boolean op) { this.op = op;}

    public Integer getDestination() {
        return destination;
    }

    public void setDestination(Integer destination) {
        this.destination = destination;
    }

    public Integer getResult() {
        return result;
    }


}
