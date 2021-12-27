public class MulDivInstruction {
    private int rs;
    private int rt;
    private int rd;
    private boolean op; // true --> Mul, false --> Div
    private int executionCycles;

    public MulDivInstruction (int rs, int rt, int rd, boolean op, int executionCycles){
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.op = op;
        this.executionCycles = executionCycles;
    }

    public int getRs() {return rs; }

    public void setRs(int rs) {this.rs = rs;}

    public int getRt() {return rt; }

    public void setRt(int rt) {this.rt = rt; }

    public int getRd() {return rd;}

    public void setRd(int rd) {this.rd = rd;}

    public boolean getOp() {return op; }

    public void setOp(boolean op) {this.op = op; }

    public int getExecutionCycles() {return executionCycles; }

    public void setExecutionCycles(int executionCycles) {this.executionCycles = executionCycles;}
}
