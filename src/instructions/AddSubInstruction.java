package instructions;
import bus.*;


public class AddSubInstruction {

    private String station;
    private Boolean op; // true => + ,,, false => -
    private double result;
    private Bus bus;


    public AddSubInstruction(String station , boolean op, Bus bus){
        this.station = station;
        this.op = op;
        this.bus = bus;
    }

    public double compute(double operand1,double operand2){
        if(op){
            result = operand1+operand2;
            System.out.println("ADD RESULT: " + result );
        }
        else {
            result = operand1 - operand2;
            System.out.println("SUB RESULT: " + result );
        }

        return result;
    }


    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public double getResult() {
        return result;
    }

    public Boolean getOp() {
        return this.op;
    }

}
