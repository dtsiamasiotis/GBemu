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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws IOException {
        Gui gui = new Gui();
        MemoryUnit memoryUnit = new MemoryUnit();
        Fetcher pixelFetcher = new Fetcher();
        Gpu gpu = new Gpu();
        Cpu cpu = new Cpu();
        InterruptManager interruptManager = new InterruptManager();
        interruptManager.setMemoryUnit(memoryUnit);
        cpu.setInterruptManager(interruptManager);
        disassembler reader = new disassembler();
        int bootrom[] = reader.readBootRom(args[0]);
        memoryUnit.writeBootRom(bootrom);
        int data[] = reader.readFile(args[1]);
        for(int i=0x100; i<data.length; i++)
            memoryUnit.writeData(i, data[i] & 0xFF);
        gpu.setGui(gui);
        gpu.setMemoryUnit(memoryUnit);
        gpu.setPixelFIFO(pixelFetcher.getPixelFIFO());
        pixelFetcher.setMemoryUnit(memoryUnit);
        cpu.setMemUnit(memoryUnit);
        pixelFetcher.setGpu(gpu);
        gpu.setFetcher(pixelFetcher);
        Joypad joypad = new Joypad();
        gui.setJoypad(joypad);
        gui.runGui();
        memoryUnit.setJoypad(joypad);
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
        int k=0;
        while(true)
        {
            //for(int i = 0; i<10; i++)
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
