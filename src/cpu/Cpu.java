package cpu;

import interrupts.InterruptManager;
import mmu.MemoryUnit;

import java.io.*;
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
        if(l>0xff)
            System.out.println("asfdd");
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
    public int getNF(){return NF;}
    public int getHF(){return HF;}

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

    public void executeInstruction(Instruction instructionToExec, Integer pc) {
        try {
            if(pc!=0xcc62)
            dumpInfoToFile(instructionToExec, pc);
        }catch(IOException e){}
     //   System.out.println(instructionToExec.getDescription()+":"+String.format("%02X",pc)+","+instructionToExec.getOpCode());
if(pc==0x3a0) {

    int fromMem = memUnit.loadData(65346);
    System.out.println("addsdfsfsf");
}
switch(instructionToExec.getOpCode())
{
    case "00":{
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "01":{
        this.setB(memUnit.loadData(pc + 2));
        this.setC(memUnit.loadData(pc + 1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "02":{
        int address = this.getBC();
        memUnit.writeData(address,this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "03":{
        int value = this.getBC() + 1;
        this.setB((value>>8) & 0xFF);
        this.setC(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "04":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "05":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "06":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "07":{
        int result = (this.getA() << 1) & 0xff;
        if ((this.getA() & (1<<7)) != 0) {
            result |= 1;
            this.setCF(1);
        } else {
            this.setCF(0);
        }
        this.setZF(0);
        this.setNF(0);
        this.setHF(0);
        this.setA(result);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "08":{
        int upperBits = memUnit.loadData(pc + 2);
        int lowBits = memUnit.loadData(pc + 1);
        int address = (upperBits<<8)|lowBits;
        memUnit.writeData(address,memUnit.getSp());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "09":{
        addToHL(this.getBC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0A":{
        int address = this.getBC();
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0B":{
        int value = this.getBC() - 1;
        this.setB((value>>8) & 0xFF);
        this.setC(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0C":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0D":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0E":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "0F":{
        this.setA(rrc(this.getA()));
        this.setZF(0);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "10":{
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "11":{
        this.setD(memUnit.loadData(pc + 2));
        this.setE(memUnit.loadData(pc + 1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "12":{
        int address = this.getDE();
        memUnit.writeData(address,this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "13":{
        int value = this.getDE() + 1;
        this.setD((value>>8) & 0xFF);
        this.setE(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "14":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "15":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "16":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "17":{
        this.setA(rl(this.getA()));
        this.setZF(0);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "18":{
        this.setPc((this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength()));
        break;
    }
    case "19":{
        addToHL(this.getDE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1A":{
        int address = this.getDE();
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1B":{
        int value = this.getDE() - 1;
        this.setD((value>>8) & 0xFF);
        this.setE(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1C":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1D":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1E":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "1F":{
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
    case "20":{
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
        break;
    }
    case "21":{
        this.setH(memUnit.loadData(pc + 2));
        this.setL(memUnit.loadData(pc + 1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "22":{
        int address = this.getHL();
        memUnit.writeData(address, this.getA());

        int newHL = address + 1;
        this.setH((newHL>>8) & 0xFF);
        this.setL(newHL & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "23":{
        int value = this.getHL() + 1;
        this.setH((value>>8) & 0xFF);
        this.setL(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "24":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "25":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "26":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "27":{
        int result = this.getA();
        if (this.getNF() == 1) {
            if (this.getHF()==1) {
                result = (result - 6) & 0xff;
            }
            if (this.getCF() == 1) {
                result = (result - 0x60) & 0xff;
            }
        } else {
            if (this.getHF()==1 || (result & 0xf) > 9) {
                result += 0x06;
            }
            if (this.getCF()==1 || result > 0x9f) {
                result += 0x60;
            }
        }
        this.setHF(0);
        if (result > 0xff) {
            this.setCF(1);
        }
        result &= 0xff;
        if(result == 0)
            this.setZF(1);
        else
            this.setZF(0);
        this.setA(result);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "28":{
        if (this.getZF() == 1) {
            this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
            instructionToExec.setCycles(12);

        } else if (this.getZF() == 0) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "29":{
        addToHL(this.getHL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2A":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        int newHL = address + 1;
        this.setH((newHL>>8) & 0xFF);
        this.setL(newHL & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2B":{
        int value = this.getHL() - 1;
        this.setH((value>>8) & 0xFF);
        this.setL(value & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2C":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2D":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2E":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "2F":{
        this.setA((0xFF ^ this.getA()) & 0xFF);
        this.setNF(1);
        this.setHF(1);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "30":{
        if (this.getCF() == 0) {
            this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
            instructionToExec.setCycles(12);

        } else if (this.getCF() == 1) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "31":{
        int stackPointer = ((memUnit.loadData(pc + 2) << 8) | memUnit.loadData(pc + 1));// & 0xFFFF;
        memUnit.setSp(stackPointer);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "32":{
        int address = this.getHL();
        memUnit.writeData(address, this.getA());

        int newHL = address - 1;
        this.setH((newHL>>8) & 0xFF);
        this.setL(newHL & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "33":{
        memUnit.setSp((memUnit.getSp() + 1) & 0xFFFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "34":{
        int address = this.getHL();
        int data = memUnit.loadData(address);
        memUnit.writeData(address, (data+1) & 0xff);
        if((data & 0x0f) == 0x0f)
            this.setHF(1);
        else
            this.setHF(0);

        if (((data + 1) & 0xff) == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "35":{
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
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "36":{
        int address = this.getHL();
        int data = memUnit.loadData(pc+1);
        memUnit.writeData(address, data);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "37":{
        this.setNF(0);
        this.setHF(0);
        this.setCF(1);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "38":{
        if (this.getCF() == 1) {
            this.setPc(this.getPc() + (byte)memUnit.loadData(pc+1) + instructionToExec.getByteLength());
            instructionToExec.setCycles(12);

        } else if (this.getCF() == 0) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "39":{
        addToHL(memUnit.getSp());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3A":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        int newHL = address - 1;
        this.setH((newHL>>8) & 0xFF);
        this.setL(newHL & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3B":{
        int value = (memUnit.getSp() - 1) & 0xFFFF;
        memUnit.setSp(value);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3C":{
        increaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3D":{
        decreaseRegister(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3E":{
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        writeRegister(position,memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "3F":{
        this.setNF(0);
        this.setHF(0);
        if(this.getCF()==1)
            this.setCF(0);
        else
            this.setCF(1);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "40":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "41":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "42":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "43":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "44":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "45":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "46":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "47":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "48":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "49":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4A":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4B":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4C":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4D":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "4F":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "50":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "51":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "52":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "53":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "54":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "55":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "56":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "57":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "58":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "59":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5A":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5B":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5C":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5D":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "5F":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "60":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "61":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "62":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "63":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "64":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "65":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "66":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "67":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "68":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "69":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6A":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6B":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6C":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6D":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "6F":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "70":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "71":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "72":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "73":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "74":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "75":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "76":{
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "77":{
        int address = this.getHL();
        int position = findCorrectRegisterFromName(instructionToExec.getOperand2());
        memUnit.writeData(address, registers[position]);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "78":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "79":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7A":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7B":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7C":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7D":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = fromMem;
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "7F":{
        loadRegToReg(instructionToExec);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "80":{
        addRegToReg(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "81":{
        addRegToReg(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "82":{
        addRegToReg(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "83":{
        addRegToReg(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "84":{
        addRegToReg(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "85":{
        addRegToReg(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "86":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        addRegToReg(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "87":{
        addRegToReg(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "88":{
        adcRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "89":{
        adcRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8A":{
        adcRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8B":{
        adcRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8C":{
        adcRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8D":{
        adcRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        adcRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "8F":{
        adcRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "90":{
        subRegister(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "91":{
        subRegister(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "92":{
        subRegister(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "93":{
        subRegister(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "94":{
        subRegister(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "95":{
        subRegister(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "96":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        subRegister(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "97":{
        subRegister(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "98":{
        sbcRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "99":{
        sbcRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9A":{
        sbcRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9B":{
        sbcRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9C":{
        sbcRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9D":{
        sbcRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9E":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        sbcRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "9F":{
        sbcRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A0":{
        andRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A1":{
        andRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A2":{
        andRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A3":{
        andRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A4":{
        andRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A5":{
        andRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A6":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        andRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A7":{
        andRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A8":{
        xorRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "A9":{
        xorRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AA":{
        xorRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AB":{
        xorRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AC":{
        xorRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AD":{
        xorRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AE":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        xorRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "AF":{
        xorRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B0":{
        orRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B1":{
        orRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B2":{
        orRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B3":{
        orRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B4":{
        orRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B5":{
        orRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B6":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        orRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B7":{
        orRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B8":{
        cpRegisterWithRegisterA(this.getB());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "B9":{
        cpRegisterWithRegisterA(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BA":{
        cpRegisterWithRegisterA(this.getD());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BB":{
        cpRegisterWithRegisterA(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BC":{
        cpRegisterWithRegisterA(this.getH());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BD":{
        cpRegisterWithRegisterA(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BE":{
        int address = this.getHL();
        int fromMem = memUnit.loadData(address);
        cpRegisterWithRegisterA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "BF":{
        cpRegisterWithRegisterA(this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C0":{
        if(this.getZF()==0)
            this.setPc(memUnit.popWordFromStack());
        else
            this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C1":{
        this.setC(memUnit.popByteFromStack());
        this.setB(memUnit.popByteFromStack());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C2":{
        if (this.getZF() == 0) {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);

        } else if (this.getZF() == 1) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "C3":{
        int upperBits = memUnit.loadData(pc + 2);
        int lowBits = memUnit.loadData(pc + 1);
        int address = (upperBits<<8)|lowBits;
        this.setPc(address);
        break;
    }
    case "C4":{
        if(this.getZF()==0)
        {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);
            memUnit.pushWordToStack(pc+3);
        }else{
            this.setPc(this.getPc() + instructionToExec.getByteLength());
        }
        break;
    }
    case "C5":{
        memUnit.pushByteToStack(this.getB());
        memUnit.pushByteToStack(this.getC());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C6":{
        addRegToReg(memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C7":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        this.setPc(addressToJump);
        break;
    }
    case "C8":{
        if(this.getZF()==1)
            this.setPc(memUnit.popWordFromStack());
        else
            this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "C9":{
        this.setPc(memUnit.popWordFromStack());
        break;
    }
    case "CA":{
        if (this.getZF() == 1) {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);

        } else if (this.getZF() == 0) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "CB":{
        prefix(instructionToExec);
        this.setPc(this.getPc() + instructionToExec.getByteLength());
        break;
    }
    case "CC":{
        if(this.getZF()==1)
        {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);
            memUnit.pushWordToStack(pc+3);
        }else{
            this.setPc(this.getPc() + instructionToExec.getByteLength());
        }
        break;
    }
    case "CD":{
        int upperBits = memUnit.loadData(pc + 2);
        int lowBits = memUnit.loadData(pc + 1);
        int address = (upperBits<<8)|lowBits;
        this.setPc(address);
        memUnit.pushWordToStack(pc+3);
        break;
    }
    case "CE":{
        int value = memUnit.loadData(pc + 1);
        adcRegisterWithRegisterA(value);
        this.setPc(this.getPc() + instructionToExec.getByteLength());
        break;
    }
    case "CF":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        this.setPc(addressToJump);
        break;
    }
    case "D0":{
        if(this.getCF()==0)
            this.setPc(memUnit.popWordFromStack());
        else
            this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "D1":{
        this.setE(memUnit.popByteFromStack());
        this.setD(memUnit.popByteFromStack());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "D2":{
        if (this.getCF() == 0) {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);

        } else if (this.getCF() == 1) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "D3":{

    }
    case "D4":{
        if(this.getCF()==0)
        {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);
            memUnit.pushWordToStack(pc+3);
        }else{
            this.setPc(this.getPc() + instructionToExec.getByteLength());
        }
        break;
    }
    case "D5":{
        memUnit.pushByteToStack(this.getD());
        memUnit.pushByteToStack(this.getE());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "D6":{
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
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "D7":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        this.setPc(addressToJump);
        break;
    }
    case "D8":{
        if(this.getCF()==1)
            this.setPc(memUnit.popWordFromStack());
        else
            this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "D9":{
        this.interruptManager.setIME(1);
        this.setPc(memUnit.popWordFromStack());
        break;
    }
    case "DA":{
        if (this.getCF() == 1) {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);

        } else if (this.getCF() == 0) {
            this.setPc(this.getPc() + instructionToExec.getByteLength());
            instructionToExec.setCycles(8);
        }
        break;
    }
    case "DB":{

    }
    case "DC":{
        if(this.getCF()==1)
        {
            int upperBits = memUnit.loadData(pc + 2);
            int lowBits = memUnit.loadData(pc + 1);
            int address = (upperBits<<8)|lowBits;
            this.setPc(address);
            memUnit.pushWordToStack(pc+3);
        }else{
            this.setPc(this.getPc() + instructionToExec.getByteLength());
        }
        break;
    }
    case "DD":{

    }
    case "DE":{
        sbcRegisterWithRegisterA(memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "DF":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        this.setPc(addressToJump);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        break;
    }
    case "E0":{
        int a8 = memUnit.loadData(pc+1);
        int address = a8 + 65280;
        memUnit.writeData(address, this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E1":{
        this.setL(memUnit.popByteFromStack());
        this.setH(memUnit.popByteFromStack());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E2":{
        int valC = this.getC();
        int address = valC + 65280;
        memUnit.writeData(address, this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E3":{

    }
    case "E4":{

    }
    case "E5":{
        memUnit.pushByteToStack(this.getH());
        memUnit.pushByteToStack(this.getL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E6":{
        andValueWithRegisterA();
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E7":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        this.setPc(addressToJump);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        break;
    }
    case "E8":{
        int r8 = memUnit.loadData(pc+1);
        int result = (memUnit.getSp() + r8) & 0xFFFF;
        this.setZF(0);
        if ((((memUnit.getSp() & 0x0F) + (r8 & 0x0F)) & 0x10) !=0)
            this.setHF(1);
        else
            this.setHF(0);
        if ((((memUnit.getSp() & 0x0F) + (r8 & 0x0F)) & 0x100) !=0)
            this.setCF(1);
        else
            this.setCF(0);

        this.setNF(0);
        memUnit.setSp(result);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "E9":{
        this.setPc(this.getHL());
        break;
    }
    case "EA":{
        int upperBits = memUnit.loadData(pc + 2);
        int lowBits = memUnit.loadData(pc + 1);
        int address = (upperBits<<8)|lowBits;
        memUnit.writeData(address,this.getA());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "EB":{
        System.out.println("NOT IMPLEMENTED");
    }
    case "EC":{

    }
    case "ED":{

    }
    case "EE":{
        xorValueWithRegisterA();
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "EF":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        this.setPc(addressToJump);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        break;
    }
    case "F0":{
        int a8 = memUnit.loadData(pc+1);
        int address = a8 + 65280;
        this.setA(memUnit.loadData(address));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F1":{
        this.setFregister(memUnit.popByteFromStack());
        this.setA(memUnit.popByteFromStack());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F2":{
        int address = 0xFF00 + this.getC();
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F3":{
        this.interruptManager.setIME(0);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F4":{

    }
    case "F5":{
        memUnit.pushByteToStack(this.getA());
        memUnit.pushByteToStack(constructFregister());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F6":{
        orValueWithRegisterA();
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F7":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        this.setPc(addressToJump);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        break;
    }
    case "F8":{
        int r8 = memUnit.loadData(pc+1);
        int result = (memUnit.getSp() + r8) & 0xFFFF;
        this.setZF(0);
        if ((((memUnit.getSp() & 0x0F) + (r8 & 0x0F)) & 0x10) !=0)
            this.setHF(1);
        else
            this.setHF(0);
        if ((((memUnit.getSp() & 0x0F) + (r8 & 0x0F)) & 0x100) !=0)
            this.setCF(1);
        else
            this.setCF(0);

        this.setNF(0);
        this.setH((result>>8) & 0xFF);
        this.setL(result & 0xFF);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "F9":{
        memUnit.setSp(this.getHL());
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "FA":{
        int upperBits = memUnit.loadData(pc + 2);
        int lowBits = memUnit.loadData(pc + 1);
        int address = (upperBits<<8)|lowBits;
        int fromMem = memUnit.loadData(address);
        this.setA(fromMem);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "FB":{
        this.interruptManager.setIME(1);
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "FC":{

    }
    case "FD":{

    }
    case "FE":{
        cpRegisterWithRegisterA(memUnit.loadData(pc+1));
        this.setPc(this.getPc()+instructionToExec.getByteLength());
        break;
    }
    case "FF":{
        int addressToJump = Integer.parseInt(instructionToExec.getOperand1(),16);
        memUnit.pushWordToStack(pc+curInstruction.getByteLength());
        this.setPc(addressToJump);
        break;
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

        int shiftedReg = (registerValue >> 1) & 0xFF;
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

        int shiftedReg = ((registerValue << 1) | (oldCF)) & 0xff;
        if(shiftedReg == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setHF(0);
        this.setNF(0);

        return shiftedReg;
    }

    public int rlc(int registerValue)
    {
        int result = (registerValue << 1) & 0xff;
        if ((registerValue & (1<<7)) != 0) {
            result |= 1;
            this.setCF(1);
        } else {
            this.setCF(0);
        }

        if(result == 0)
            this.setZF(1);
        else
            this.setZF(0);
        this.setNF(0);
        this.setHF(0);
        return result;
    }

    public int rrc(int registerValue)
    {
        int result = registerValue >> 1;
        if ((registerValue & 1) == 1) {
            result |= (1 << 7);
            this.setCF(1);
        } else {
            this.setCF(0);
        }
        if(result == 0)
            this.setZF(1);
        else
            this.setZF(0);
        this.setNF(0);
        this.setHF(0);
        return result;
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

    public void increaseRegister(Instruction instructionToExec)
    {
        int position = findCorrectRegisterFromName(instructionToExec.getOperand1());
        int value = (registers[position] + 1) & 0xFF;
        if((registers[position] & 0x0F) == 0x0F)
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

    public void decreaseRegister(Instruction instructionToExec)
    {
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

    public void addToHL(int secondOperand)
    {
        int HLoperand = this.getHL();
        int sum = HLoperand + secondOperand;

        this.setNF(0);
        if(sum>0xffff)
            this.setCF(1);
        else
            this.setCF(0);
        if(((HLoperand & 0x0FFF) + (secondOperand & 0x0FFF)) > 0x0FFF)
            this.setHF(1);
        else
            this.setHF(0);

        sum = sum & 0xFFFF;
        this.setH((sum>>8) & 0xFF);
        this.setL(sum & 0xFF);
    }

    public void loadRegToReg(Instruction instructionToExec)
    {
        int source = findCorrectRegisterFromName(instructionToExec.getOperand2());
        int destination = findCorrectRegisterFromName(instructionToExec.getOperand1());
        registers[destination] = registers[source];
    }

    public void addRegToReg(int valueToAdd)
    {
        int value = (registers[0] + valueToAdd) & 0xFF;
        if (value == 0)
            this.setZF(1);
        else
            this.setZF(0);
        if (((registers[0] & 0x0F) + (valueToAdd & 0x0F)) > 0x0F)
            this.setHF(1);
        else
            this.setHF(0);
        if (registers[0] + valueToAdd > 0xFF)
            this.setCF(1);
        else
            this.setCF(0);

        this.setNF(0);
        registers[0] = value;
    }

    public void subRegister(int value)
    {
        if((registers[0] & 0xFF) - (value & 0xFF) == 0)
            this.setZF(1);
        else
            this.setZF(0);

        if((value & 0x0F) > (registers[0] & 0x0F))
            this.setHF(1);
        else
            this.setHF(0);

        if(value > registers[0])
            this.setCF(1);
        else
            this.setCF(0);

        this.setNF(1);

        registers[0] = (registers[0] - value) & 0xFF;
    }

    public void andRegisterWithRegisterA(int registerValue)
    {
        this.setA((registerValue & this.getA()) & 0xFF);
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(1);
        this.setCF(0);

    }

    public void andValueWithRegisterA()
    {
        this.setA(this.getA() & memUnit.loadData(pc+1));
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(1);
        this.setCF(0);

    }

    public void orRegisterWithRegisterA(int registerValue)
    {
        this.setA((registerValue | this.getA()) & 0xFF);
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        this.setCF(0);
    }

    public void orValueWithRegisterA()
    {
        this.setA(this.getA() | memUnit.loadData(pc+1));
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        this.setCF(0);
    }

    public void xorRegisterWithRegisterA(int registerValue)
    {
        this.setA((registerValue ^ this.getA()) & 0xFF);
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        this.setCF(0);
    }

    public void xorValueWithRegisterA()
    {
        this.setA(this.getA() ^ memUnit.loadData(pc+1));
        if(this.getA()==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        this.setCF(0);
    }

    public void sbcRegisterWithRegisterA(int registerValue)
    {
        int carry = this.getCF();
        int res = this.getA() - registerValue - carry;
        if((res & 0xff) == 0 )
            this.setZF(1);
        else
            this.setZF(0);
        this.setNF(1);
        if(((this.getA() ^ registerValue ^ (res & 0xff)) & (1 << 4)) != 0)
            this.setHF(1);
        else
            this.setHF(0);
        if(res<0)
            this.setCF(1);
        else
            this.setCF(0);

        this.setA(res);
    }

    public void adcRegisterWithRegisterA(int value)
    {
        int oldCF = this.getCF();
        this.setZF(((this.getA() + value + this.getCF()) & 0xff) == 0?1:0);
        this.setNF(0);
        this.setHF((this.getA() & 0x0f) + (value & 0x0f) + this.getCF() > 0x0f?1:0);
        this.setCF(this.getA() + value + this.getCF() > 0xff?1:0);
        this.setA((this.getA() + value + oldCF) & 0xff);
    }

    public void cpRegisterWithRegisterA(int registerValue)
    {
        if(((this.getA() - registerValue) & 0xFF) == 0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(1);
        if((0x0f & registerValue) > (0x0f & this.getA()))
            this.setHF(1);
        else
            this.setHF(0);
        if(registerValue>this.getA())
            this.setCF(1);
        else
            this.setCF(0);

    }

    public void prefix(Instruction instructionToExec)
    {
        int opcode = memUnit.loadData(pc+1);
        String hexPrefixCode = String.format("%02X",opcode);
        if(opcode>=0x00 && opcode<=0x07)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x06)
            {
                int rotatedValue = rlc(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),rotatedValue);
            }
            else {
                registers[position] = rlc(registers[position]);
            }
        }
        if(opcode>=0x08 && opcode<=0x0f)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x0E)
            {
                int rotatedValue = rrc(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),rotatedValue);
            }
            else {
                registers[position] = rrc(registers[position]);
            }
        }

        if(opcode>=0x10 && opcode<=0x17)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x16)
            {
                int rotatedValue = rl(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),rotatedValue);
            }
            else {
                registers[position] = rl(registers[position]);
            }
        }
        if(opcode>=0x18 && opcode<=0x1F)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x1E)
            {
                int rotatedValue = rr(memUnit.loadData(getHL()));
                memUnit.writeData(this.getHL(),rotatedValue);
            }
            else {
                registers[position] = rr(registers[position]);
            }
        }
        if(opcode>=0x20 && opcode<=0x27)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x26)
            {
                int shiftedValue = sla(memUnit.loadData(getHL()));
                memUnit.writeData(this.getHL(),shiftedValue);
            }
            else {
                registers[position] = sla(registers[position]);
            }
        }
        if(opcode>=0x28 && opcode<=0x2f)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x2E)
            {
                int shiftedValue = sra(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),shiftedValue);
            }
            else {
                registers[position] = sra(registers[position]);
            }
        }
        if(opcode>=0x30 && opcode<=0x37)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x36)
            {
                int swappedValue = swap(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),swappedValue);
            }
            else {
                registers[position] = swap(registers[position]);
            }
        }
        if(opcode>=0x38 && opcode<=0x3f)
        {
            int position = prefixSecondOperand(opcode%8);

            if(opcode==0x3E)
            {
                int rotatedValue = srl(memUnit.loadData(this.getHL()));
                memUnit.writeData(this.getHL(),rotatedValue);
            }
            else {
                registers[position] = srl(registers[position]);
            }
        }
        if(opcode>=0x40 && opcode<=0x7f)
        {
            if(opcode==0x46||opcode==0x56||opcode==0x66||opcode==0x76||opcode==0x4E||opcode==0x5E||opcode==0x6E||opcode==0x7E) {
                int position = (opcode - 0x40) / 8;
                prefixBIT(position, memUnit.loadData(this.getHL()));
            }
            else{
                int position = (opcode - 0x40) / 8;
                int regPos = prefixSecondOperand(opcode % 8);
                prefixBIT(position, registers[regPos]);
            }

        }
        if(opcode>=0x80 && opcode<=0xbf)
        {
            int position = prefixSecondOperand(opcode%8);
            int firstOperand = (opcode-0x80)/8;
            if(opcode==0x86||opcode==0x96||opcode==0xA6||opcode==0xB6||opcode==0x8E||opcode==0x9E||opcode==0xAE||opcode==0xBE)
            {
                int resettedValue = (~(1<<firstOperand) & memUnit.loadData(this.getHL()) & 0xFF);
                memUnit.writeData(this.getHL(),resettedValue);
            }
            else {
                registers[position] = (~(1<<firstOperand) & registers[position] & 0xFF);
            }

        }
        if(opcode>=0xC0 && opcode<=0xFF)
        {
            int position = prefixSecondOperand(opcode%8);
            int firstOperand = (opcode-0xC0)/8;

            if(opcode==0xC6||opcode==0xD6||opcode==0xE6||opcode==0xF6||opcode==0xCE||opcode==0xDE||opcode==0xEE||opcode==0xFE)
            {
                int settedValue = ((1<<firstOperand)|(memUnit.loadData(this.getHL()))) & 0xFF;
                memUnit.writeData(this.getHL(),settedValue);
            }
            else {
                registers[position] = ((1<<firstOperand)|(registers[position])) & 0xFF;
            }

        }

    }

    public void prefixBIT(int firstOperand, int secondOperand)
    {
        if((secondOperand & (1<<firstOperand))!=0)
            this.setZF(0);
        else if((secondOperand & (1<<firstOperand))==0)
            this.setZF(1);

        this.setNF(0);
        this.setHF(1);
    }

    public void prefixRES(int firstOperand,int secondOperand)
    {
        registers[secondOperand] = (~(1<<firstOperand) & registers[secondOperand] & 0xFF);
    }

    public void prefixSET(int firstOperand,int secondOperand)
    {

    }

    public int sla(int registerValue)
    {
        int result = (registerValue << 1) & 0xff;
        if((registerValue & (1<<7)) != 0)
            this.setCF(1);
        else
            this.setCF(0);
        if(result==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        return result;
    }

    public int sra(int registerValue)
    {
        int result = ((registerValue >> 1) | (registerValue & (1 << 7))) & 0xFF;
        if((registerValue & 1) != 0)
            this.setCF(1);
        else
            this.setCF(0);
        if(result==0)
            this.setZF(1);
        else
            this.setZF(0);

        this.setNF(0);
        this.setHF(0);
        return result;
    }
    public void checkAndHandleInterrupts()
    {
        if(interruptManager.getIME() == 1) {
            if ((interruptManager.getIE() & interruptManager.getIF() & 0x1) == 1) {
                interruptManager.setIME(0);
                memUnit.pushWordToStack(this.pc);
                this.setPc(0x40);
            }
            if ((interruptManager.getIE() & interruptManager.getIF() & 0x10) == 0x10) {
                interruptManager.setIME(0);
                memUnit.pushWordToStack(this.pc);
                this.setPc(0x60);
            }
        }
    }

    public int prefixSecondOperand(int position)
    {
        if(position == 0x7)
        {
            return 0;
        }
        return position+1;
    }

    public void dumpInfoToFile(Instruction instructionToExec, Integer pc) throws IOException
    {
        String value = instructionToExec.getDescription()+":"+String.format("%02X",pc)+","+instructionToExec.getOpCode();
        value += " A:"+String.format("%02X",this.getA())+" B:"+String.format("%02X",this.getB())+" C:"+String.format("%02X",this.getC())+" D:"+String.format("%02X",this.getD())+" E:"+String.format("%02X",this.getE())+" H:"+String.format("%02X",this.getH())+" L:"+String.format("%02X",this.getL())+" ZF:"+this.getZF()+" NF:"+this.getNF()+" HF:"+this.getHF()+" CF:"+this.getCF();
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/dimitris/gbemu.txt",true));
        writer.append("\n");
        writer.append(value);

        writer.close();
    }
}

