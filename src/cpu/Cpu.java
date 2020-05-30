package cpu;

import interrupts.InterruptManager;
import mmu.MemoryUnit;

import java.nio.ByteBuffer;

public class Cpu {
    //private byte A,B,C,D,E,H,L;
    private int[] registers = new int[7];
    private int Fregister;
    private int ZF,NF,HF,CF;
    private int pc;
    private MemoryUnit memUnit = new MemoryUnit();
    private int timer;
    private byte[] programToExecute;
    private instructionMapper mapOfInstructions =  new instructionMapper();
    private Instruction curInstruction;
    private boolean instrCompleted = true;
    private boolean finalStageOfInstr = false;
    private int moreCycles = 0;
    private InterruptManager interruptManager;

    public void setA(int a)
    {
       // A = a;
        registers[0] = a;
    }

    public void setB(int b)
    {
       // B = b;
        registers[1] = b;
    }

    public void setC(int c)
    {
       // C = c;
        registers[2] = c;
    }

    public void setD(int d)
    {
       // D = d;
        registers[3] = d;
    }

    public void setE(int e)
    {
      //  E = e;
        registers[4] = e;
    }

    public void setH(int h)
    {
      //  H = h;
       // byte[] tmpBytes = new byte[2];
        //tmpBytes[0] = h;
       // tmpBytes[1] = this.getL();
        registers[5] = h;
      //  this.setHL(tmpBytes);
    }

    public void setL(int l)
    {
      //  L = l;
       // byte[] tmpBytes = new byte[2];
        //tmpBytes[0] = this.getH();
        //tmpBytes[1] = l;
        registers[6] = l;

        //this.setHL(tmpBytes);
    }

   // public  void setHL(byte[] hl)
    //{
    //    HL=hl;
    //    this.setH(hl[0]);
    //    this.setL(hl[1]);
    //}

    public void setZF(int state){ZF=state;}

    public void setNF(int state){NF=state;}

    public void setHF(int state){HF=state;}

    public void setCF(int state){CF=state;}

    public void setFregister(int Fregister)
    {
        this.Fregister = Fregister;
        ZF = 1 & (Fregister >> 7);
        NF = 1 & (Fregister >> 6);
        HF = 1 & (Fregister >> 5);
        CF = 1 & (Fregister >> 4);
    }

    public int getZF(){return ZF;}
    public int getCF(){return CF;}


    public int getA()
    {
        return registers[0] & 0xFF;
    }

    public int getB()
    {
        return registers[1] & 0xFF;
    }

    public int getC()
    {
        return registers[2] & 0xFF;
    }

    public int getD()
    {
        return registers[3] & 0xFF;
    }

    public int getE()
    {
        return registers[4] & 0xFF;
    }

    public int getH()
    {
        return registers[5] & 0xFF;
    }

    public int getL()
    {
        return registers[6] & 0xFF;
    }

    public int getHL(){
        return ((this.getH()<<8)|this.getL()) & 0xFFFF;
    }

    public int getBC(){
        return ((this.getB()<<8)|this.getC()) & 0xFFFF;
    }

    public int getDE(){
        return ((this.getD()<<8)|this.getE()) & 0xFFFF ;
    }

    public void setPc(int programCounter){this.pc = programCounter;}

    public int getPc()
    {
        return pc;
    }

    public void writeRegister(int position,int value)
    {
        registers[position] = value;
    }

    public void setInterruptManager(InterruptManager interruptManager)
    {
        this.interruptManager = interruptManager;
    }


    public int findCorrectRegisterFromName(String registerName)
    {
        int position = 0;
        switch (registerName)
        {
            case "A":
                position = 0;
                break;
            case "B":
                position = 1;
                break;
            case "C":
                position =  2;
                break;
            case "D":
                position =  3;
                break;
            case "E":
                position = 4;
                break;
            case "H":
                position =  5;
                break;
            case "L":
                position =  6;
                break;
        }

        return position;
    }

    public void executeInstruction(Instruction instructionToExec, Integer pc)
    {
        System.out.println(instructionToExec.getDescription()+":"+String.format("%02X",pc));
if(pc==0x2cd) {

    int fromMem = memUnit.loadData(65346);
    System.out.println("addsdfsfsf");
}
        switch(instructionToExec.getDescription())
        {
            case "NOP":
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            case "LD": {
                if (instructionToExec.getOperand1().equals("SP")) {
                    int stackPointer = ((memUnit.loadData(pc + 2)<<8)|memUnit.loadData(pc + 1));// & 0xFFFF;
                    memUnit.setSp(stackPointer);
                } else if (instructionToExec.getOperand1().equals("HL")) {
                    if (instructionToExec.getOperand2().equals("d16")) {
                        this.setH(memUnit.loadData(pc + 2));
                        this.setL(memUnit.loadData(pc + 1));
                    }
                }else if(instructionToExec.getOperand1().equals("DE")){
                    if (instructionToExec.getOperand2().equals("d16")) {
                        this.setD(memUnit.loadData(pc + 2));
                        this.setE(memUnit.loadData(pc + 1));
                    }
                }
                else if(instructionToExec.getOperand1().equals("BC")){
                    if (instructionToExec.getOperand2().equals("d16")) {
                        this.setB(memUnit.loadData(pc + 2));
                        this.setC(memUnit.loadData(pc + 1));
                    }

                }
                else if (instructionToExec.getOperand1().equals("(HL-)")) {
                    if (instructionToExec.getOperand2().equals("A")) {
                        int address = this.getHL();
                        memUnit.writeData(address, this.getA());
                       /* byte[] arr = ByteBuffer.allocate(4).putInt((address - 1)).array();
                        addressInBytes[0] = arr[2];
                        addressInBytes[1] = arr[3];

                        this.setH(addressInBytes[0]);
                        this.setL(addressInBytes[1]);*/
                       int newHL = address - 1;
                       this.setH((newHL>>8) & 0xFF);
                       this.setL(newHL & 0xFF);

                    }
                }
                else if (instructionToExec.getOperand1().equals("(HL+)")) {
                    if (instructionToExec.getOperand2().equals("A")) {
                        int address = this.getHL();
                        memUnit.writeData(address, this.getA());
                        /*byte[] arr = ByteBuffer.allocate(4).putInt((address + 1)).array();
                        addressInBytes[0] = arr[2];
                        addressInBytes[1] = arr[3];

                        this.setH(addressInBytes[0]);
                        this.setL(addressInBytes[1]);*/
                        int newHL = address + 1;
                        this.setH((newHL>>8) & 0xFF);
                        this.setL(newHL & 0xFF);

                    }
                }
                else if (instructionToExec.getOperand1().equals("(HL)")) {
                    if (!instructionToExec.getOperand2().equals("d8")) {

                       int address = this.getHL();
                       int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
                       memUnit.writeData(address, registers[position]);

                    }
                } else if (instructionToExec.getOperand2().equals("d8"))
                {
                    int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    writeRegister(position,memUnit.loadData(pc+1));
                }
                else if(instructionToExec.getOpCode().equals("E2"))
                {
                    int valC = this.getC();
                    int address = valC + 65280;
                    memUnit.writeData(address, this.getA());

                }
                else if(instructionToExec.getOpCode().equals("E0"))
                {
                    int a8 = memUnit.loadData(pc+1);
                    int address = a8 + 65280;
                    memUnit.writeData(address, this.getA());
                }
                else if(instructionToExec.getOpCode().equals("F0"))
                {
                    int a8 = memUnit.loadData(pc+1);
                    int address = a8 + 65280;
                    this.setA(memUnit.loadData(address));
                }
                else if(instructionToExec.getOpCode().equals("1A"))
                {
                    int address = this.getDE();
                    int fromMem = memUnit.loadData(address);
                    this.setA(fromMem);
                }
                else if(instructionToExec.getOpCode().equals("12"))
                {
                    int address = this.getDE();
                    memUnit.writeData(address,this.getA());
                }
                else if(instructionToExec.getOpCode().equals("2A"))
                {
                    int address = this.getHL();
                    int fromMem = memUnit.loadData(address);
                    this.setA(fromMem);
                    int newHL = address + 1;
                    this.setH((newHL>>8) & 0xFF);
                    this.setL(newHL & 0xFF);

                }
                else if(instructionToExec.getOpCode().equals("EA"))
                {
                    int upperBits = memUnit.loadData(pc + 2);
                    int lowBits = memUnit.loadData(pc + 1);
                    int address = (upperBits<<8)|lowBits;
                    memUnit.writeData(address,this.getA());
                }
                else if(instructionToExec.getOpCode().equals("FA"))
                {
                    int upperBits = memUnit.loadData(pc + 2);
                    int lowBits = memUnit.loadData(pc + 1);
                    int address = (upperBits<<8)|lowBits;
                    int fromMem = memUnit.loadData(address);
                    this.setA(fromMem);
                    memUnit.writeData(address,this.getA());
                }
                else if(instructionToExec.getOperand1().length()==1 && instructionToExec.getOperand2().equals("(HL)"))
                {
                    int address = this.getHL();
                    int fromMem = memUnit.loadData(address);
                    int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    registers[destination] = fromMem;
                }
                else
                {
                    int source = findCorrectRegisterFromName(instructionToExec.getOperand2());
                    int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    registers[destination] = registers[source];
                }

                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }

            case "PREFIX CB":
            {
                int operand = memUnit.loadData(pc+1);
                String hexPrefixCode = String.format("%02X",operand);
                switch(hexPrefixCode)
                {
                    case "7C":
                    {
                        if((this.getH()>>7)==0b00000001)
                            this.setZF(0);
                        else if((this.getH()>>7)==0b00000000)
                            this.setZF(1);

                        this.setNF(0);
                        this.setHF(1);
                        break;
                    }
                    case "11": {
                        /*int position = findCorrectRegisterFromName("C");
                        byte rotatedReg = (byte) ((registers[position] << 1) | this.getCF());
                        if (((byte) (registers[position]) & (1 << 7)) == 0)
                            this.setCF(0);
                        else
                            this.setCF(1);

                        if(rotatedReg == 0)
                            this.setZF(1);
                        else
                            this.setZF(0);
                        this.setC(rotatedReg);
                        this.setHF(0);
                        this.setNF(0);*/
                        this.setC(rl(this.getC()));
                        break;
                    }
                    case "19":{
                        this.setC(rr(this.getC()));
                        break;
                    }
                    case "1A":{
                        this.setD(rr(this.getD()));
                        break;
                    }
                    case "37":{
                        this.setA(swap(this.getA()));
                        break;
                    }
                    case "38":{
                        this.setB(srl(this.getB()));
                        break;
                    }
                    case "87":{
                        this.setA(~(1<<0) & this.getA() & 0xFF);
                    }
                }
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "XOR":
            {
                String operand1 = instructionToExec.getOperand1();
                if(!operand1.equals("d8")) {
                    if (operand1.equals("A"))
                        this.setA((byte) (this.getA() ^ this.getA()));
                    else if (operand1.equals("B"))
                        this.setA((byte) (this.getB() ^ this.getA()));
                    else if (operand1.equals("C"))
                        this.setA((byte) (this.getC() ^ this.getA()));
                    else if (operand1.equals("D"))
                        this.setA((byte) (this.getD() ^ this.getA()));
                    else if (operand1.equals("E"))
                        this.setA((byte) (this.getE() ^ this.getA()));
                    else if (operand1.equals("L"))
                        this.setA((byte) (this.getL() ^ this.getA()));
                    else if (operand1.equals("H"))
                        this.setA((byte) (this.getH() ^ this.getA()));
                }
                else
                {
                    this.setA(this.getA() ^ memUnit.loadData(pc+1));
                }
                if(this.getA()==0)
                    this.setZF(1);
                else
                    this.setZF(0);

                this.setNF(0);
                this.setHF(0);
                this.setCF(0);

                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "OR":
            {
                String operand1 = instructionToExec.getOperand1();
                if(!operand1.equals("d8")) {
                    if (operand1.equals("A"))
                        this.setA((byte) (this.getA() | this.getA()));
                    else if (operand1.equals("B"))
                        this.setA((byte) (this.getB() | this.getA()));
                    else if (operand1.equals("C"))
                        this.setA((this.getC() | this.getA()) & 0xff);
                    else if (operand1.equals("D"))
                        this.setA((byte) (this.getD() | this.getA()));
                    else if (operand1.equals("E"))
                        this.setA((byte) (this.getE() | this.getA()));
                    else if (operand1.equals("L"))
                        this.setA((byte) (this.getL() | this.getA()));
                    else if (operand1.equals("H"))
                        this.setA((byte) (this.getH() | this.getA()));
                }
                else
                {
                    this.setA(this.getA() | memUnit.loadData(pc+1));
                }
                    if(this.getA()==0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    this.setNF(0);
                    this.setHF(0);
                    this.setCF(0);

                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "AND":
            {
                String operand1 = instructionToExec.getOperand1();
                if(!operand1.equals("d8")) {
                    if (operand1.equals("A"))
                        this.setA((this.getA() & this.getA()) & 0xFF);
                    else if (operand1.equals("B"))
                        this.setA((byte) (this.getB() & this.getA()));
                    else if (operand1.equals("C"))
                        this.setA((byte) (this.getC() & this.getA()));
                    else if (operand1.equals("D"))
                        this.setA((byte) (this.getD() & this.getA()));
                    else if (operand1.equals("E"))
                        this.setA((byte) (this.getE() & this.getA()));
                    else if (operand1.equals("L"))
                        this.setA((byte) (this.getL() & this.getA()));
                    else if (operand1.equals("H"))
                        this.setA((byte) (this.getH() & this.getA()));
                }
                else
                {
                    this.setA(this.getA() & memUnit.loadData(pc+1));
                }
                if(this.getA()==0)
                    this.setZF(1);
                else
                    this.setZF(0);

                this.setNF(0);
                this.setHF(1);
                this.setCF(0);

                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "JR":
            {
                if(instructionToExec.getOperand1().equals("NZ")) {
                    if(finalStageOfInstr) {
                        this.setPc(this.getPc() + (byte) memUnit.loadData(pc + 1) + instructionToExec.getByteLength());
                        instrCompleted = true;
                        moreCycles = 0;
                        finalStageOfInstr = false;
                        break;
                    }
                    if (this.getZF() == 0) {
                        moreCycles = 4;
                        finalStageOfInstr = true;
                        break;
                    } else if (this.getZF() == 1) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instrCompleted = true;
                    }
                }
                if(instructionToExec.getOperand1().equals("Z"))
                {
                    if (this.getZF() == 1) {
                        this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
                        instructionToExec.setCycles(12);

                    } else if (this.getZF() == 0) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instructionToExec.setCycles(8);
                    }
                }
                if(instructionToExec.getOperand1().equals("NC")) {
                    if (this.getCF() == 0) {
                        this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
                        instructionToExec.setCycles(12);

                    } else if (this.getCF() == 1) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instructionToExec.setCycles(8);
                    }
                }
                if(instructionToExec.getOperand1().equals("C"))
                {
                    if (this.getCF() == 1) {
                        this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
                        instructionToExec.setCycles(12);

                    } else if (this.getCF() == 0) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instructionToExec.setCycles(8);
                    }
                }
                if(instructionToExec.getOperand1().equals("r8"))
                {
                    this.setPc((this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength()));
                }
                break;
            }
            case "INC":
            {
                if(instructionToExec.getOperand1().length()==1) {
                    int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    int value = (registers[position] + 1) & 0xFF;
                    if((registers[position] & 0x0F) == 0)
                        this.setHF(1);
                    else
                        this.setHF(0);
                    writeRegister(position, value);
                    this.setNF(0);
                    if(value==0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                }
                else if(instructionToExec.getOperand1().length()==2)
                {
                    if(instructionToExec.getOperand1().equals("DE"))
                    {
                        int value = this.getDE() + 1;
                        this.setD((value>>8) & 0xFF);
                        this.setE(value & 0xFF);
                    }
                    if(instructionToExec.getOperand1().equals("HL"))
                    {
                        int value = this.getHL() + 1;
                        this.setH((value>>8) & 0xFF);
                        this.setL(value & 0xFF);

                    }
                    if(instructionToExec.getOperand1().equals("BC"))
                    {
                        int value = this.getBC() + 1;
                        this.setB((value>>8) & 0xFF);
                        this.setC(value & 0xFF);

                    }
                }
                if(instructionToExec.getOperand1().equals("(HL)"))
                {
                    int address = this.getHL();
                    int data = memUnit.loadData(address);
                    memUnit.writeData(address, (data+1) & 0xff);
                    if((data & 0x0f) == 0)
                        this.setHF(1);
                    else
                        this.setHF(0);

                    if (((data + 1) & 0xff) == 0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    this.setNF(0);
                }
                this.setPc(this.getPc() + instructionToExec.getByteLength());
                break;
            }
            case "CALL":
            {
                if(instructionToExec.getOperand2()==null)
                {
                    int upperBits = memUnit.loadData(pc + 2);
                    int lowBits = memUnit.loadData(pc + 1);
                    int address = (upperBits<<8)|lowBits;
                    this.setPc(address);
                    memUnit.pushToStack(pc+3);
                }else{
                    if(instructionToExec.getOperand1().equals("NZ"))
                    {
                        if(this.getZF()==0)
                        {
                            int upperBits = memUnit.loadData(pc + 2);
                            int lowBits = memUnit.loadData(pc + 1);
                            int address = (upperBits<<8)|lowBits;
                            this.setPc(address);
                            memUnit.pushToStack(pc+3);
                        }else{
                            this.setPc(this.getPc() + instructionToExec.getByteLength());
                        }
                    }
                    else if(instructionToExec.getOperand1().equals("Z"))
                    {
                        if(this.getZF()==1)
                        {
                            int upperBits = memUnit.loadData(pc + 2);
                            int lowBits = memUnit.loadData(pc + 1);
                            int address = (upperBits<<8)|lowBits;
                            this.setPc(address);
                            memUnit.pushToStack(pc+3);
                        }else{
                            this.setPc(this.getPc() + instructionToExec.getByteLength());
                        }
                    }
                    else if(instructionToExec.getOperand1().equals("NC"))
                    {
                        if(this.getCF()==0)
                        {
                            int upperBits = memUnit.loadData(pc + 2);
                            int lowBits = memUnit.loadData(pc + 1);
                            int address = (upperBits<<8)|lowBits;
                            this.setPc(address);
                            memUnit.pushToStack(pc+3);
                        }else{
                            this.setPc(this.getPc() + instructionToExec.getByteLength());
                        }
                    }
                    else if(instructionToExec.getOperand1().equals("C"))
                    {
                        if(this.getCF()==1)
                        {
                            int upperBits = memUnit.loadData(pc + 2);
                            int lowBits = memUnit.loadData(pc + 1);
                            int address = (upperBits<<8)|lowBits;
                            this.setPc(address);
                            memUnit.pushToStack(pc+3);
                        }else{
                            this.setPc(this.getPc() + instructionToExec.getByteLength());
                        }
                    }

                }
            }
            break;
            case "RET":
            {
                if(instructionToExec.getOperand1()==null)
                {
                    this.setPc(memUnit.popFromStack());
                }
                else if(instructionToExec.getOperand1().equals("C"))
                {
                    if(this.getCF()==1)
                        this.setPc(memUnit.popFromStack());
                    else
                        this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                else if(instructionToExec.getOperand1().equals("NC"))
                {
                    if(this.getCF()==0)
                        this.setPc(memUnit.popFromStack());
                    else
                        this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                else if(instructionToExec.getOperand1().equals("Z"))
                {
                    if(this.getZF()==1)
                        this.setPc(memUnit.popFromStack());
                    else
                        this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                else if(instructionToExec.getOperand1().equals("NZ"))
                {
                    if(this.getZF()==0)
                        this.setPc(memUnit.popFromStack());
                    else
                        this.setPc(this.getPc()+instructionToExec.getByteLength());
                }

            }
            break;
            case "RLA":
            {
                byte rotatedA = (byte)((this.getA()<<1)|this.getCF());
                if(((byte)(this.getA())&(1<<7))==0)
                    this.setCF(0);
                else
                    this.setCF(1);

                this.setZF(rotatedA == 0?1:0);
                this.setA(rotatedA);
                this.setPc(this.getPc()+instructionToExec.getByteLength());

            }
            break;
            case "RLCA":
            {
                byte result = (byte) ((this.getA() << 1) & 0xff);
                if ((this.getA() & (1<<7)) != 0) {
                    result |= 1;
                    this.setCF(1);
                } else {
                    this.setCF(0);
                }
                this.setZF(result == 0?1:0);
                this.setNF(0);
                this.setHF(0);
                this.setA(result);
                this.setPc(this.getPc()+instructionToExec.getByteLength());
            }
            break;
            case "RL":
            {
                int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
                byte rotatedReg = (byte)((registers[position]<<1)|this.getCF());
                if(((byte)(registers[position])&(1<<7))==0)
                    this.setCF(0);
                else
                    this.setCF(1);

                this.setA(rotatedReg);
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "DEC":
            {
                if(instructionToExec.getOperand1().length()==1) {
                    int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    if((registers[position] & 0x0f) == 0)
                        this.setHF(1);
                    else
                        this.setHF(0);
                    registers[position] = (registers[position] - 1) & 0xFF;
                    if (registers[position] == 0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    this.setNF(1);
                }

                else if(instructionToExec.getOperand1().length()==2)
                {
                    if(instructionToExec.getOperand1().equals("DE"))
                    {

                        int value = this.getDE() - 1;
                        this.setD((value>>8) & 0xFF);
                        this.setE(value & 0xFF);

                    }
                    if(instructionToExec.getOperand1().equals("HL"))
                    {
                        int value = this.getHL() - 1;
                        this.setH((value>>8) & 0xFF);
                        this.setL(value & 0xFF);

                    }
                    if(instructionToExec.getOperand1().equals("BC"))
                    {
                        int value = this.getBC() - 1;
                        this.setB((value>>8) & 0xFF);
                        this.setC(value & 0xFF);

                    }
                }
                if(instructionToExec.getOperand1().equals("(HL)"))
                {
                    int address = this.getHL();
                    int data = memUnit.loadData(address);
                    memUnit.writeData(address, (data-1) & 0xff);
                    if((data & 0x0f) == 0)
                        this.setHF(1);
                    else
                        this.setHF(0);

                    if (((data - 1) & 0xff) == 0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    this.setNF(1);
                }
                this.setPc(this.getPc() + instructionToExec.getByteLength());
                break;
            }
            case "PUSH":
            {
                if(instructionToExec.getOperand1().equals("BC"))
                {
                    int value = this.getBC();
                    memUnit.pushToStack(value);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("DE"))
                {
                    int value = this.getDE();
                    memUnit.pushToStack(value);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("HL"))
                {
                    int value = this.getHL();
                    memUnit.pushToStack(value);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("AF"))
                {
                    int value = ((this.getA()<<8) | constructFregister()) & 0xFFFF;
                    memUnit.pushToStack(value);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                break;
            }
            case "POP":
            {
                if(instructionToExec.getOperand1().equals("BC"))
                {
                    int fromStack = memUnit.popFromStack();
                    this.setB((fromStack>>8) & 0xFF);
                    this.setC(fromStack & 0xFF);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("DE"))
                {
                    int fromStack = memUnit.popFromStack();
                    this.setD((fromStack>>8) & 0xFF);
                    this.setE(fromStack & 0xFF);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("HL"))
                {
                    int fromStack = memUnit.popFromStack();
                    this.setH((fromStack>>8) & 0xFF);
                    this.setL(fromStack & 0xFF);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                if(instructionToExec.getOperand1().equals("AF"))
                {
                    int fromStack = memUnit.popFromStack();
                    byte[] arr = ByteBuffer.allocate(4).putInt(fromStack).array();
                    this.setA((fromStack>>8) & 0xFF);
                    this.setFregister(fromStack & 0xFF);
                    this.setPc(this.getPc()+instructionToExec.getByteLength());
                }
                break;
            }
            case "CP":
            {
                if(instructionToExec.getOperand1().equals("d8"))
                {
                    if(this.getA()-memUnit.loadData(pc+1)==0)
                        this.setZF(1);
                    else
                        this.setZF(0);
                    this.setNF(1);

                    if(memUnit.loadData(pc+1)>this.getA())
                        this.setCF(1);
                    else
                        this.setCF(0);

                    if((memUnit.loadData(pc+1)&0x0f)>(this.getA()&0x0f))
                        this.setHF(1);
                    else
                        this.setHF(0);

                }
                if(instructionToExec.getOperand1().equals("(HL)"))
                {
                    int address = this.getHL();
                    int HLvalue = memUnit.loadData(address);
                    if(((this.getA()-HLvalue) & 0xff)==0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    this.setNF(1);
                    if((HLvalue & 0x0f) > (this.getA() & 0x0f))
                        this.setHF(1);
                    else
                        this.setHF(0);

                    if(HLvalue > this.getA())
                        this.setCF(1);
                    else
                        this.setCF(0);
                }
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "SUB":
            {
                if(instructionToExec.getOperand1().equals("d8"))
                {
                    int d8 = memUnit.loadData(pc+1);
                    int value = (this.getA() - d8) & 0xFF;
                    this.setNF(1);
                    if (value == 0b00000000)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    if(d8 > this.getA())
                        this.setCF(1);
                    else
                        this.setCF(0);

                    if((0x0f & d8) > (0x0f & this.getA()))
                        this.setHF(1);
                    else
                        this.setHF(0);

                    this.setA(value);

                }
                else {
                    int toSubtract = findCorrectRegisterFromName(instructionToExec.getOperand1());
                    if((registers[0] & 0xFF) - (registers[toSubtract] & 0xFF) == 0)
                        this.setZF(1);
                    else
                        this.setZF(0);

                    if((registers[toSubtract] & 0x0F) > (registers[0] & 0x0F))
                        this.setHF(1);
                    else
                        this.setHF(0);

                    if(registers[toSubtract] > registers[0])
                        this.setCF(1);
                    else
                        this.setCF(0);

                    this.setNF(1);

                    registers[0] = (registers[0] - registers[toSubtract]) & 0xFF;

                }
                this.setPc(this.getPc() + instructionToExec.getByteLength());
                break;
            }
            case "JP":
            {
                if(instructionToExec.getOpCode().equals("E9"))
                {
                    this.setPc(this.getHL());
                }
                if(instructionToExec.getOpCode().equals("C3"))
                {
                    int upperBits = memUnit.loadData(pc + 2);
                    int lowBits = memUnit.loadData(pc + 1);
                    int address = (upperBits<<8)|lowBits;
                    this.setPc(address);
                }
                if(instructionToExec.getOpCode().equals("CA"))
                {
                    if (this.getZF() == 1) {
                        int upperBits = memUnit.loadData(pc + 2);
                        int lowBits = memUnit.loadData(pc + 1);
                        int address = (upperBits<<8)|lowBits;
                        this.setPc(address);

                    } else if (this.getZF() == 0) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instructionToExec.setCycles(8);
                    }
                }
                if(instructionToExec.getOpCode().equals("C2"))
                {
                    if (this.getZF() == 0) {
                        int upperBits = memUnit.loadData(pc + 2);
                        int lowBits = memUnit.loadData(pc + 1);
                        int address = (upperBits<<8)|lowBits;
                        this.setPc(address);

                    } else if (this.getZF() == 1) {
                        this.setPc(this.getPc() + instructionToExec.getByteLength());
                        instructionToExec.setCycles(8);
                    }
                }
                break;
            }
            case "DI":
            {
                this.interruptManager.setIME(0);
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "EI":
            {
                this.interruptManager.setIME(1);
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "CPL":
            {
                this.setA((0xFF ^ this.getA()) & 0xFF);
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                this.setNF(1);
                this.setHF(1);
                break;
            }
            case "RST":
            {
                int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
                this.setPc(addressToJump);
                memUnit.pushToStack(pc+curInstruction.getByteLength());
                break;
            }
            case "ADD":
            {
                if(instructionToExec.getOperand1().equals("HL") && (instructionToExec.getOperand2().equals("DE")||instructionToExec.getOperand2().equals("BC")||instructionToExec.getOperand2().equals("HL")))
                {
                    int secondOperand = 0;
                    if(instructionToExec.getOperand2().equals("DE"))
                        secondOperand = this.getDE();
                    else if(instructionToExec.getOperand2().equals("BC"))
                        secondOperand = this.getBC();
                    else if(instructionToExec.getOperand2().equals("HL"))
                        secondOperand = this.getHL();

                    int HLoperand = this.getHL();
                    int sum = HLoperand + secondOperand;

                    this.setH((sum>>8) & 0xFF);
                    this.setL(sum & 0xFF);
                    this.setNF(0);
                    if(sum>0xffff)
                        this.setCF(1);
                    else
                        this.setCF(0);
                    if(((HLoperand & 0x0FFF) + (secondOperand & 0x0FFF)) > 0x0FFF)
                        this.setHF(1);
                    else
                        this.setHF(0);
                }
                else {
                    if(instructionToExec.getOperand2().equals("d8"))
                    {
                        int value = (this.getA() + memUnit.loadData(pc+1)) & 0xff;
                        if(value == 0)
                            this.setZF(1);
                        else
                            this.setZF(0);
                        if(((this.getA() & 0x0F) + (memUnit.loadData(pc+1) & 0x0F)) > 0x0F)
                            this.setHF(1);
                        else
                            this.setHF(0);
                        if(this.getA() + memUnit.loadData(pc+1) > 0xFF)
                            this.setCF(1);
                        else
                            this.setCF(0);

                        this.setNF(0);
                        this.setA(value);

                    }else {
                        int destinationRegister = findCorrectRegisterFromName(instructionToExec.getOperand1());
                        int secondOperand = findCorrectRegisterFromName(instructionToExec.getOperand2());
                        int value = (registers[destinationRegister] + registers[secondOperand]) & 0xFF;
                        if (value == 0)
                            this.setZF(1);
                        else
                            this.setZF(0);
                        if (((registers[destinationRegister] & 0x0F) + (registers[secondOperand] & 0x0F)) > 0x0F)
                            this.setHF(1);
                        else
                            this.setHF(0);
                        if (registers[destinationRegister] + registers[secondOperand] > 0xFF)
                            this.setCF(1);
                        else
                            this.setCF(0);

                        this.setNF(0);
                        registers[destinationRegister] = value;
                    }

                }
                this.setPc(this.getPc()+instructionToExec.getByteLength());
                break;
            }
            case "RRA":
            {
                int newCF = 0;
                if((this.getA() & 1) != 0)
                    newCF = 1;

                this.setA((this.getA() >> 1) | (CF << 7));
                this.setCF(newCF);
                this.setZF(0);
                this.setNF(0);
                this.setHF(0);
                this.setPc(this.getPc()+instructionToExec.getByteLength());

                break;
            }
            case "ADC":
            {
                if(instructionToExec.getOperand2().equals("d8")) {
                    int oldCF = this.getCF();
                    this.setZF(((this.getA() + memUnit.loadData(pc+1) + this.getCF()) & 0xff) == 0?1:0);
                    this.setNF(0);
                    this.setHF((this.getA() & 0x0f) + (memUnit.loadData(pc+1) & 0x0f) + this.getCF() > 0x0f?1:0);
                    this.setCF(this.getA() + memUnit.loadData(pc+1) + this.getCF() > 0xff?1:0);
                    this.setA((this.getA() + memUnit.loadData(pc+1) + oldCF) & 0xff);
                }
                else{
                    int secondOperand = findCorrectRegisterFromName(instructionToExec.getOperand2());
                    int oldCF = this.getCF();
                    this.setZF(((this.getA() + registers[secondOperand] + this.getCF()) & 0xff) == 0?1:0);
                    this.setNF(0);
                    this.setHF((this.getA() & 0x0f) + (registers[secondOperand] & 0x0f) + this.getCF() > 0x0f?1:0);
                    this.setCF(this.getA() + registers[secondOperand] + this.getCF() > 0xff?1:0);
                    this.setA((this.getA() + registers[secondOperand] + oldCF) & 0xff);
                }
                this.setPc(this.getPc()+instructionToExec.getByteLength());
            }
            case "STOP":
            {
                this.setPc(this.getPc()+instructionToExec.getByteLength());
            }
            case "RETI":
            {
                this.interruptManager.setIME(1);
                this.setPc(memUnit.popFromStack());
            }
        }
    }

    public void setProgramToExecute(byte[] programToExecute)
    {
        this.programToExecute = programToExecute;
    }

    public void setMemUnit(MemoryUnit memUnit){ this.memUnit = memUnit; }

    public void tick()
    {
        if(timer==1) {
            curInstruction = fetchInstruction();

        }
        if(curInstruction!=null)
            if(timer==curInstruction.getCycles() + moreCycles) {
                executeInstruction(curInstruction, pc);
                if(instrCompleted) {
                    timer = 0;
                    checkAndHandleInterrupts();
                }
        }

        timer++;
    }

    public Instruction fetchInstruction()
    {
        String hexOpcode = String.format("%02X",memUnit.loadData(pc));
        return mapOfInstructions.findInstructionFromOpcode(hexOpcode);
    }

    public int constructFregister()
    {
        int F = 0;
        if(ZF==1)
            F = F | (1<<7);
        if(NF==1)
            F = F | (1<<6);
        if(HF==1)
            F = F | (1<<5);
        if(CF==1)
            F = F | (1<<4);

        return F & 0xFF;
    }

    public int srl(int registerValue)
    {
        if((registerValue & 1) !=0)
            this.setCF(1);
        else
            this.setCF(0);

        int shiftedReg = registerValue >> 1;
        if(shiftedReg == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setHF(0);
        this.setNF(0);

        return shiftedReg;
    }

    public int rr(int registerValue)
    {
        int oldCF = this.getCF();
        if((registerValue & 1) != 0)
            this.setCF(1);
        else
            this.setCF(0);

        int shiftedReg = (registerValue >> 1) | (oldCF << 7);
        if(shiftedReg == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setHF(0);
        this.setNF(0);

        return shiftedReg;
    }

    public int rl(int registerValue)
    {
        int oldCF = this.getCF();
        if((registerValue & (1 << 7)) != 0)
            this.setCF(1);
        else
            this.setCF(0);

        int shiftedReg = (registerValue << 1) | (oldCF);
        if(shiftedReg == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setHF(0);
        this.setNF(0);

        return shiftedReg;
    }

    public int swap(int registerValue)
    {
        if(registerValue == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        this.setCF(0);

        return (registerValue << 4) | (registerValue>> 4);
    }

    public void checkAndHandleInterrupts()
    {
        if(interruptManager.getIME() == 1) {
            if ((interruptManager.getIE() & interruptManager.getIF() & 0x1) == 1) {
                interruptManager.setIME(0);
                memUnit.pushToStack(this.pc);
                this.setPc(0x40);
            }
        }
    }
}

