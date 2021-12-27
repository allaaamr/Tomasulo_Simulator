package instructions;

public class AddSubInstruction {
    private Integer destination;
    private Integer operand1;
    private Integer operand2;
    private Boolean op; // true => + ,,, false => -
    private Integer result;

    public AddSubInstruction(){
        operand1 = 0;
        operand2 = 0;
        op = true;
        result = 0;
    }

    public AddSubInstruction(int op1, int op2, boolean op){
        operand1 = op1;
        operand2 = op2;
        this.op = op;
        result = 0;
    }

    public Integer compute(){
        if(op)
            result = operand1+operand2;
        else
            result = operand1-operand2;

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

}
