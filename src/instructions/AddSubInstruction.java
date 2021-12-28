package instructions;
import bus.*;


public class AddSubInstruction implements BusListener{
    private Integer destination;
    private Boolean op; // true => + ,,, false => -
    private Integer result;
    private Integer executionCycles;
    private Bus bus;

    public Integer getExecutionCycles() {
        return executionCycles;
    }

    public void setExecutionCycles(Integer executionCycles) {
        this.executionCycles = executionCycles;
    }

    public AddSubInstruction(int destination, boolean op, Bus bus){
        this.op = op;
        this.bus = bus;
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
        return operand1;
    }

    public void setDestination(Integer operand1) {
        this.operand1 = operand1;
    }

    public Integer getOperand1() {
        return operand1;
    }

    public void setOperand1(Integer operand1) {
        this.operand1 = operand1;
    }

    public Integer getOperand2() {
        return operand2;
    }

    public void setOperand2(Integer operand2) {
        this.operand2 = operand2;
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
