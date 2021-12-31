package bus;
import instructions.Register;

import java.util.ArrayList;
import java.util.Hashtable;

public class Bus {

    private Register registerFile [] ;
    private Hashtable<Integer, ArrayList<BusListener>> waitingInstructions ;

    public Bus (Register [] registerFile) {
        this.registerFile = registerFile;
        waitingInstructions = new Hashtable<Integer, ArrayList<BusListener>>();
    }

    public void notify (Integer register , Integer updatedValue){
        ArrayList<BusListener> instructionsToBeNotified = waitingInstructions.get(register);
        for (BusListener a: instructionsToBeNotified){
            a.update(register,updatedValue);
        }
        registerFile[register].updateRegister(updatedValue);
    }

    public void pushInstruction(Integer register, BusListener inst)
    {
        waitingInstructions.get(register).add(inst);
    }
}
