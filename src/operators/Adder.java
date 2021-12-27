package operators;

public class Adder {
    private Integer operand1;
    private Integer operand2;
    private Boolean op; // true => + ,,, false => -
    private Integer result;

    public Adder(){
        operand1 = 0;
        operand2 = 0;
        op = true;
        result = 0;
    }

    public Adder(int op1, int op2, boolean op){
        operand1 = op1;
        operand2 = op2;
        this.op = op;
        result = 0;
    }

    public Integer compute(){
        if(op)
            result=operand1+operand2;
        else result=operand1-operand2;

        return result;
    }

    /**
     * @return Integer return the operand1
     */
    public Integer getOperand1() {
        return operand1;
    }

    /**
     * @param operand1 the operand1 to set
     */
    public void setOperand1(Integer operand1) {
        this.operand1 = operand1;
    }

    /**
     * @return Integer return the operand2
     */
    public Integer getOperand2() {
        return operand2;
    }

    /**
     * @param operand2 the operand2 to set
     */
    public void setOperand2(Integer operand2) {
        this.operand2 = operand2;
    }

    /**
     * @return Integer return the result
     */
    public Integer getResult() {
        return result;
    }

    public Boolean getOp() {
        return this.op;
    }

}
