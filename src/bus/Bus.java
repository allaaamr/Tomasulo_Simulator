package bus;
import instructions.Register;

import java.util.Hashtable;

public class Bus {

    private Register registerFile [] ;
    private Hashtable<String,Register> waitingInstructions ;
    private BusListener [] listeners ;


    public Hashtable<String, Register> getWaitingInstructions() {
        return waitingInstructions;
    }

    public Bus (Register [] registerFile) {
        this.registerFile = registerFile;
        listeners = new BusListener[4] ;
        waitingInstructions = new Hashtable<>();
    }

    public void notify (String instruction ,double updatedValue){
        for (BusListener i : listeners)
            i.update(instruction,updatedValue);

        Register r =  waitingInstructions.get(instruction);
        if( r!= null && r.getInstruction().equals(instruction)) {
            waitingInstructions.get(instruction).updateRegister(updatedValue);
            waitingInstructions.remove(instruction);
        }
    }
    public void pushWaitingInstruction (String station,Register register) {
        waitingInstructions.put(station,register);
    }



    public void setListeners(BusListener l []){
        this.listeners = l;
    }

}
