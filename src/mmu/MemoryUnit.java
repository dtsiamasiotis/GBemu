package mmu;

import java.util.Stack;

public class MemoryUnit {
    private Stack<Integer> stack= new Stack<Integer>();
    private int[] mainMem = new int[65536];
    private int sp = 0xFFFE;

    public void setSp(int stackPointer)
    {
        sp = stackPointer;
    }

    public int getSp() {
        return sp;
    }

    public void writeData(int address, int b){
        if(b>0xFF)
        {
            mainMem[address] = b & 0xFF;
            mainMem[address+1] = (b>>8) & 0xFF;
        }
        else
            mainMem[address]=b;

    //TETRIS hack
    if(address==0xFF80 && b==0xff) {
       // mainMem[address]=0;
     //   System.out.print("edw");
    }
    if(b>0xff)
    {
       System.out.println("sdfrfgerge");
    }
    if(address==0xFF46)
        DMATransfer(b);
    }

    public int loadData(int address){ return mainMem[address]; }

    public void pushToStack(Integer pc)
    {
        if(pc==0xdefb) {
            System.out.println("sdfrfgerge");
        }
        //stack.push(pc);
        //this.setSp(sp-2);

        writeData(sp,pc);
        this.setSp(sp-1);
    }

    public Integer popFromStack()
    {
        //this.setSp(sp+2);
        //return stack.pop();
          this.setSp(sp+1);
          return loadData(sp);
    }

    public void writeBootRom(int data[])
    {
        for(int i=0;i<=0xFF;i++)
        {
            writeData(i,data[i] & 0xFF);
        }
    }

    public void writeRomBank1(int data[])
    {
        for(int i=0x100;i<=0x3fff;i++)
        {
            writeData(i,data[i] & 0xFF);
        }
    }

    public void writeRomBank2(int data[])
    {
        for(int i=0x4000;i<=0x7FFF;i++)
        {
            writeData(i,data[i] & 0xFF);
        }
    }

    public void DMATransfer(int data)
    {
        int address = data << 8;
        for(int i = 0; i<0xA0; i++)
        {
            writeData(0xFE00+i, loadData(address+i));
        }
    }

    public void pushByteToStack(int value)
    {
        sp--;
        mainMem[sp] = value & 0xFF;
    }

    public int popByteFromStack()
    {
        int value = mainMem[sp] & 0xFF;
        sp++;
        return value;
    }

    public void pushWordToStack(int value)
    {
        if(value==0xff26)
        {
            System.out.println("edw");
        }
        pushByteToStack((value>>8) & 0xFF);
        pushByteToStack(value & 0xFF);
    }

    public int popWordFromStack()
    {
        int value1 = popByteFromStack();
        int value2 = popByteFromStack();

        return (value2<<8)|value1;
    }
}
