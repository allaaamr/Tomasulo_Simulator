package instructions;

public class Register {

    private int regNumber ;
    private boolean ready;
    private double value ;
    private String instruction;

    public Register(){
        this.value = 0;
        this.ready = true;
    }

    public void updateRegister (double newVal) {
        ready = true;
        this.value = newVal;
    }

    public String toString () {
        return "[R"+regNumber+" = "+value+"]";
    }
    public double getValue() {
        return value;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setRegNumber(int regNumber) {
        this.regNumber = regNumber;
    }

    public void setInstruction(String instruction) {
        this.ready = false;
        this.instruction = instruction;
    }
}
