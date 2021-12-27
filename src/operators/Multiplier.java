package operators;

public class Multiplier {
    
    private Integer operand1;
    private Integer operand2;
    private Integer result;
    private Boolean op; // true => multiplication ... false => division

    public Multiplier(){
        operand1 = 0;
        operand2 = 0;
        result = 0;
        op = true;
    }
    public Multiplier(int operand1, int operand2, boolean op){
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.op = op;
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

    public Integer compute(){
        if(op)
            result = operand1*operand2;
        else
            result = operand1/operand2;

        return result;
    }

}
