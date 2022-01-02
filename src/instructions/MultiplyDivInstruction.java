package instructions;
import bus.*;

public class MultiplyDivInstruction {

    private String station;
    private double result;
    private Boolean op; // true => multiplication ... false => division
    private Bus bus;

    public MultiplyDivInstruction(String station , boolean op, Bus bus){
        this.station = station;
        this.op = op;
        this.bus = bus;
    }

    public double compute(double operand1,double operand2){
        if(op){
            result = operand1*operand2;
            System.out.println("MUL RESULT: " + result );
        }
        else {
            result = operand1 / operand2;
            System.out.println("DIV RESULT: " + result );
        }
        bus.notify(station, result);
        return result;
    }


    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Boolean getOp() { return op;}

    public void setOp(Boolean op) { this.op = op;}


    public double getResult() {
        return result;
    }


}
