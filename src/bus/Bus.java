package bus;
import instructions.Register;

import java.util.Hashtable;

public class Bus {

    private Register registerFile [] ;
    private Hashtable<String,Register> waitingInstructions ;
    BusListener [] listeners ;

    public Bus (Register [] registerFile,BusListener loadBuffer,BusListener storeBuffer,BusListener addSubStation, BusListener mulDivStation) {
        this.registerFile = registerFile;
        listeners = new BusListener[4] ;
        listeners[0] = loadBuffer;
        listeners[1] = storeBuffer;
        listeners[2] = addSubStation;
        listeners[3] = mulDivStation;
        waitingInstructions = new Hashtable<>();
    }

    public void notify (String instruction ,double updatedValue){
        for (BusListener i : listeners)
            i.update(instruction,updatedValue);

        waitingInstructions.get(instruction).updateRegister(updatedValue);
    }

}
