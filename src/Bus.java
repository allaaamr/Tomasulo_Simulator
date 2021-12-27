import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

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
}
