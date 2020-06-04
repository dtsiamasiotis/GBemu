package mmu;

import java.util.Stack;

public class MemoryUnit {
    private Stack<Integer> stack= new Stack<Integer>();
    private int[] mainMem = new int[65536];
    private int sp = 0;

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
    if(address >= 0xFF00 && address <= 0xFF7F){
            if(address == 0xFF02 && b == 0x81){
                /* "send" byte from 0xFF01 */
                System.out.println((char) mainMem[0xFF01 - 0xFF00]);
                /* flush the output buffer */

            } }
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
        stack.push(pc);
        this.setSp(sp-2);
    }

    public Integer popFromStack()
    {
        this.setSp(sp+2);
        return stack.pop();

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
}
