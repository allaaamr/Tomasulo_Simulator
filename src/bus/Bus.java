package bus;

import java.util.ArrayList;
import java.util.Hashtable;

public class Bus {

    private Integer registerFile [] ;
    private Hashtable<Integer, ArrayList<BusListener>> waitingInstructions ;

    public Bus (Integer [] registerFile) {
        this.registerFile = registerFile;
        waitingInstructions = new Hashtable<Integer, ArrayList<BusListener>>();
    }

    public void notify (Integer register , Integer updatedValue){
        ArrayList<BusListener> instructionsToBeNotified = waitingInstructions.get(register);
        for (BusListener a: instructionsToBeNotified){
            a.update(register,updatedValue);
        }
        registerFile[register] = updatedValue;
    }

    public void pushInstruction(Integer register, BusListener inst){
        waitingInstructions.get(register).add(inst);  
    }
}
