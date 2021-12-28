package instructions;

public class MultiplyDivInstruction {

    private Integer destination ;
    private Integer operand1;
    private Integer operand2;
    private Integer result;
    private Boolean op; // true => multiplication ... false => division
    private Integer executionCycles;



    public MultiplyDivInstruction(){
        operand1 = 0;
        operand2 = 0;
        result = 0;
        op = true;
    }
    public MultiplyDivInstruction(int operand1, int operand2, boolean op){
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.op = op;
    }

    public Integer getExecutionCycles() { return executionCycles; }

    public void setExecutionCycles(Integer executionCycles) {this.executionCycles = executionCycles;}

    public Boolean getOp() { return op;}

    public void setOp(Boolean op) { this.op = op;}

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

    public Integer compute(){
        if(op)
            result = operand1*operand2;
        else
            result = operand1/operand2;

        return result;
    }

}
