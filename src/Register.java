public class Register {
    private boolean ready;
    private int value;
    private String instruction;

    public Register(){
        this.value = 0;
        this.ready = true;
    }

    public int getValue() {
        return value;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
