package src.operators;

public class LoadStore {
    private Integer memLocation; // memory location 
    private Integer regNumber; // number of the register
    private Boolean op; // true -> load ,,, false -> store
    private Object memory[]; // TODO: memory file .. to be discussed
    private Object regFile[]; // TODO: register file.. to be discussed

    public LoadStore(int memLocation, int regNumber, Boolean op, Object []memory, Object []regFile){
        this.memLocation = memLocation;
        this.regNumber = regNumber;
        this.op = op;
        this.memory = memory;
        this.regFile = regFile;
    }

    public void execute(){
        if(op){
            regFile[regNumber] = memory[memLocation];
        }
        else
            memory[memLocation] = regFile[regNumber];
    }

    public void setMemLocation(int memLocation){
        this.memLocation = memLocation;
    }

    public void setRegNumber(int regNumber){
        this.regNumber = regNumber;
    }

    public void setOp(Boolean op){
        this.op = op;
    }

    public void setMemory(Object [] memory){
        this.memory = memory;
    }

    public void setRegFile(Object [] regFile){
        this.regFile = regFile;
    }

    public Integer getMemLocation(){
        return memLocation;
    }

    public Integer getRegNumber(){
        return regNumber;
    }

    public Boolean getOp(){
        return op;
    }

    public Object[] getMemory(){
        return memory;
    }

    public Object[] getRegFile(){
        return regFile;
    }
}
