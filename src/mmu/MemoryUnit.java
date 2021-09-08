package mmu;

import joypad.Joypad;
import serial.SerialPort;
import tileViewer.TileViewer;

import java.util.Stack;

public class MemoryUnit {
    private Stack<Integer> stack= new Stack<Integer>();
    private int[] mainMem;
    private int sp = 0xFFFE;
    private Joypad joypad;
    private boolean MBC1 = false;
    private int currentRomBank = 0;
    private int bankRegister1 = 0;
    private int bankRegister2 = 0;
    private int mode = 0;
    private int[] cartridge;
    private int[] bootRom;
    private SerialPort serialPort;
    private boolean enableExternalRam = false;
    private TileViewer tileViewer;
   /* public MemoryUnit(int romSize)
    {
        if(romSize < 65536)
            mainMem = new int[65536];
        else
            mainMem = new int[romSize];
    }*/

    public void setCartridge(int[] cartridgeRom)
    {
        this.cartridge = cartridgeRom;
    }

    public int[] getMainMem() {
        return mainMem;
    }

    public void setMainMem(int[] mainMem) {
        this.mainMem = mainMem;
    }

    public void setBootrom(int[] bootRom) {
        this.bootRom = bootRom;
    }

    public void setJoypad(Joypad joypad) {
        this.joypad = joypad;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void setSp(int stackPointer)
    {
        sp = stackPointer;
    }

    public int getSp() {
        return sp;
    }

    public void setTileViewer(TileViewer tileViewer) {
        this.tileViewer = tileViewer;
    }

    public void writeData(int address, int b) {
       // if(address == 0x98c3)
           // System.out.println("afdsdf");


        if(address >= 0x0000 && address <= 0x1FFF && isMBC1())
        {
            if(((b & 00001111) & 0b1010) == 0b1010)
                enableExternalRam = true;
            else
                enableExternalRam = false;
            return;
        }
        if(address>=0xA000 && address<=0xBFFF && isMBC1() && enableExternalRam)
        {
            if(mode == 0)
            {
                int finalAddress = address & 0b1111111111111;
                mainMem[finalAddress] = b;
            }
            if(mode == 1)
            {
                int finalAddress = address & 0b1111111111111;
                finalAddress = (bankRegister2 << 13) | finalAddress;
                mainMem[finalAddress] = b;
            }
        }
        if (address == 0xFF46) {
            DMATransfer(b);
            return;
        }
        if(address >= 0x2000 && address <= 0x3FFF && isMBC1())
        {
            if(b == 0)
                bankRegister1 = 0x0;
            else {
                bankRegister1 = b & 0b00011111;
            }
            return;
        }
        if(address >= 0x4000 && address <= 0x5FFF && isMBC1())
        {
            bankRegister2 = b & 0b00000011;
            return;
        }
        if(address >= 0x6000 && address <= 0x7FFF && isMBC1())
        {
            mode = b & 0b00000001;
            return;
        }

        if(address==0x2000) {
            mainMem[address] = 0x20;
            return;
        }

        if (b > 0xFF) {
            mainMem[address] = b & 0xFF;
            mainMem[address + 1] = (b >> 8) & 0xFF;
        } else
            mainMem[address] = b;

        //TETRIS hack
        //if ((address == 0xFF80 || address == 0xFF81) && b == 0xff) {
          //  mainMem[address] = b;
            //   System.out.print("edw");
        //}
        if (address == 0xFF04)
            mainMem[address] = 0;

        if (address == 0xFF00)
            joypad.setTemp(b);

        if (address == 0xFF01)
            serialPort.setSb(b);
        if (address == 0xFF02) {
            serialPort.setSc(b);
            if ((serialPort.getSc() & (1 << 7)) != 0) {
                serialPort.startTransfer();
            }
        }
        //if(address == 0xFF40)
            //System.out.print("edw");

        if(address >=0x8000 && address <= 0x9800){
                  //synchronized (this) {
            tileViewer.refresh(address);
               //  }
        }
    }

    public int loadData(int address){
        if(address == 0xFF00)
        {
            return joypad.getJoypadRegister();
        }
        if(address >=0x0 && address <= 0xFF && mainMem[0xFF50] != 1)
        {
            return bootRom[address] & 0xFF;
        }
        if(address >=0x0 && address <= 0x3FFF)
        {
            return cartridge[address];
        }
        if(address >=0x4000 && address <= 0x7FFF)
        {
            currentRomBank = findCurrentRomBank(address);
            if(currentRomBank == 0)
                currentRomBank++;

            int finalAddress = (address-0x4000) + (currentRomBank * 0x4000);
            return cartridge[finalAddress];
        }
        if(address>=0xA000 && address<=0xBFFF && isMBC1() && enableExternalRam)
        {
            if(mode == 0)
            {
                int finalAddress = address & 0b1111111111111;
                return mainMem[finalAddress];
            }
            if(mode == 1)
            {
                int finalAddress = address & 0b1111111111111;
                finalAddress = (bankRegister2 << 13) | finalAddress;
                return mainMem[finalAddress];
            }
        }
        //SMURFS
        if(address == 0xff8c)
           return 0xc;

        if (address == 0xff01) {
            return serialPort.getSb();
        } else if (address == 0xff02) {
            return serialPort.getSc() | 0b01111110;
        }

        return mainMem[address];
    }

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
            mainMem[0xFE00+i] = loadData(address+i);
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

    public boolean isMBC1() {
        if(cartridge[0x0147] >= 0x1 && cartridge[0x0147] <= 0x3)
            return true;
        else
            return false;
    }

    public int findCurrentRomBank(int address)
    {
        int romBankNumber = 0;
        if(address >= 0x0 && address <= 0x3FFF && mode==0)
        {
            return romBankNumber;
        }
        else if(address >= 0x0 && address <= 0x3FFF && mode==1)
        {
            return (bankRegister2 << 5);
        }
        else if (address >= 0x4000 && address <= 0x7FFF)
        {
            return (bankRegister2 << 5) | bankRegister1;
        }

        currentRomBank = romBankNumber;
        return romBankNumber;
    }
}
