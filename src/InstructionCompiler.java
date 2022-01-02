import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionCompiler {
     private String instructionType ;
     private int destinationRegister ;
     private int firstOperand ;
     private int secondOperand ;
     private int memoryAddress ;
     private String ins;


     public InstructionCompiler(String instruction) {
          this.ins = instruction;

          Pattern pattern = Pattern.compile("([A-Za-z0-9]+) *");
          Matcher matcher = pattern.matcher(instruction);
          if(matcher.find()) //set the destination register
               instructionType = matcher.group(1);

          pattern = Pattern.compile(" +([A-Za-z0-9]+) *,");
          matcher = pattern.matcher(instruction);
          if (matcher.find())
               destinationRegister = Integer.parseInt(matcher.group(1).substring(1)) ;

          if (instructionType.equals("LD") || instructionType.equals("SD")){ // LD R1, 100 |||| ST R2, 200
               pattern = Pattern.compile(", *([A-Za-z0-9]+) *");
               matcher = pattern.matcher(instruction);
               if (matcher.find()) //set the memory address
                    memoryAddress = Integer.parseInt(matcher.group(1).substring(1));
          }
          else if (instructionType.equals("ADD") ||
                   instructionType.equals("SUB") ||
                   instructionType.equals("MUL") ||
                   instructionType.equals("DIV")){ // ADD R1,R2,R3

               pattern = Pattern.compile(", *([A-Za-z0-9]+) *");
               matcher = pattern.matcher(instruction);

               if (matcher.find())
                    firstOperand = Integer.parseInt(matcher.group(1).substring(1));
               if (matcher.find())
                    secondOperand = Integer.parseInt(matcher.group(1).substring(1)) ;
          }
     }

     public String toString () {
          return ins;
     }
     public String getInstructionType() {
          return instructionType;
     }

     public int getDestinationRegister() {
          return destinationRegister;
     }

     public int getFirstOperand() {
          return firstOperand;
     }

     public int getSecondOperand() {
          return secondOperand;
     }

     public int getMemoryAddress() {
          return memoryAddress;
     }
}
