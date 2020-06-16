package emu;

import cpu.Cpu;
import cpu.disassembler;
import gpu.Gpu;
import gui.Gui;
import interrupts.InterruptManager;
import joypad.Joypad;
import mmu.MemoryUnit;
import ppu.Fetcher;
import ppu.Pixel;
import timer.Timer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        Gui gui = new Gui();
        disassembler reader = new disassembler();
        int data[] = reader.readFile(args[1]);
        MemoryUnit memoryUnit = new MemoryUnit(data.length);
        Fetcher pixelFetcher = new Fetcher();
        Gpu gpu = new Gpu();
        Cpu cpu = new Cpu();
        Joypad joypad = new Joypad();
        memoryUnit.setJoypad(joypad);
        InterruptManager interruptManager = new InterruptManager();
        Timer timer = new Timer();
        timer.setMemoryUnit(memoryUnit);
        interruptManager.setMemoryUnit(memoryUnit);
        cpu.setInterruptManager(interruptManager);

        int bootrom[] = reader.readBootRom(args[0]);
        memoryUnit.setCartridge(data);
       // memoryUnit.writeBootRom(bootrom);
        int memSize = data.length + bootrom.length;
        int tempMem[];
        if(memSize < 65536)
            tempMem = new int[65536];
        else
            tempMem = new int[memSize];

        for(int i=0;i<=0xFF;i++)
            tempMem[i] = bootrom[i] & 0xFF;

        for(int i=0x100; i<data.length; i++)
            tempMem[i] = data[i] & 0xFF;

        memoryUnit.setMainMem(tempMem);
        gpu.setGui(gui);
        gpu.setMemoryUnit(memoryUnit);
        gpu.setPixelFIFO(pixelFetcher.getPixelFIFO());
        pixelFetcher.setMemoryUnit(memoryUnit);
        cpu.setMemUnit(memoryUnit);
        pixelFetcher.setGpu(gpu);
        gpu.setFetcher(pixelFetcher);

        gui.setJoypad(joypad);
        gui.runGui();

        joypad.setMemoryUnit(memoryUnit);

        pixelFetcher.setMapAddress(38912);
        //BIOS SETUP
       /* for(int i=0;i<256;i++)
        {
            memoryUnit.writeByte(i,data[i]);
        }*/
       /* for(int i=0;i<48;i++)
        {
            memoryUnit.writeData(260+i,bootrom[168+i] & 0xFF);
        }*/


        //for(int i=0;i<20000;i++)
        cpu.setPc(0x0);
        //memoryUnit.setSp(0xFFFE);
        int dividerCounter = 0;
        int timerCounter = 0;
        int k=0;
        while(true)
        {
            //for(int i = 0; i<10; i++)
            if((timerCounter == timer.cyclesToIncreaseCounter()-1) && timer.isRunning()) {
                timer.increaseCounter();
                timerCounter = 0;
            }

            if(timer.isRunning())
                timerCounter++;

            if(dividerCounter==255)
            {
               int[] memory = memoryUnit.getMainMem();

               if(memory[0xFF04]>=255)
                   memory[0xFF04] = 0;
               else
                   memory[0xFF04] = memory[0xFF04]+1;

               memoryUnit.setMainMem(memory);
               dividerCounter = 0;
            }
            else
                dividerCounter++;

            if(k==100)
                k=0;


            cpu.tick();
            gpu.tick();
            if(gpu.getState().equals("HBLANK")) {
                pixelFetcher.getPixelFIFO().dropAll();
                pixelFetcher.setState("READTILEID");
                pixelFetcher.resetTileInRow();
            }
            if(gpu.getState().equals("PIXELTRANSFER") && k%2==0)
                pixelFetcher.tick();

            k++;

            if(cpu.getPc() == 0x100)
            {
                for(int i=0; i<0x100; i++)
                    memoryUnit.writeData(i, data[i] & 0xFF);
            }
        }



        //gui.refresh();
    }


}
