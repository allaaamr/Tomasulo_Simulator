package bus;

public class Clock {

    private int cycles ;

    public Clock (){
        this.cycles = 0 ;
    }

    public void updateClock (){
        cycles++ ;
    }

    public int getCycles () {
        return cycles;
    }
}
