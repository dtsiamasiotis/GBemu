package cpu;

public class Instruction {
    private String description;
    private String opCode;
    private String operand1;
    private String operand2;
    private int byteLength;
    private int cycles;

    public void setDescription(String str)
    {
        description = str;
    }

    public void setOpCode(String str)
    {
        opCode = str;
    }

    public void setOperand1(String str)
    {
        operand1 = str;
    }

    public void setOperand2(String str)
    {
        operand2 = str;
    }

    public void setByteLength(int l)
    {
        byteLength = l;
    }

    public void setCycles(int c){ cycles = c; }

    public String getDescription() {
        return description;
    }

    public String getOpCode(){
        return opCode;
    }

    public String getOperand1(){
        return operand1;
    }

    public String getOperand2(){
        return operand2;
    }

    public int getByteLength()
    {
        return byteLength;
    }

    public int getCycles(){ return cycles; }
}
