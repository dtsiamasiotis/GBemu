package emu;

import cpu.Cpu;
import cpu.disassembler;
import gpu.Gpu;
import gui.Gui;
import interrupts.InterruptManager;
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
        //int bootrom[] = reader.readBootRom();
        //memoryUnit.writeBootRom(bootrom);
        int data[] = reader.readFile();
        for(int i=0x100; i<data.length; i++)
            memoryUnit.writeData(i, data[i] & 0xFF);
        gpu.setGui(gui);
        gpu.setMemoryUnit(memoryUnit);
        gpu.setPixelFIFO(pixelFetcher.getPixelFIFO());
        pixelFetcher.setMemoryUnit(memoryUnit);
        cpu.setMemUnit(memoryUnit);
        pixelFetcher.setGpu(gpu);
        gpu.setFetcher(pixelFetcher);

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
        //cpu.setPc(0x100);
        //memoryUnit.setSp(0xFFFE);
        while(true)
        {
            //for(int i = 0; i<10; i++)
                cpu.tick();

            if(gpu.getState().equals("HBLANK")) {
                pixelFetcher.getPixelFIFO().dropAll();
                pixelFetcher.setState("READTILEID");
                pixelFetcher.resetTileInRow();
            }
            if(gpu.getState().equals("PIXELTRANSFER"))
                pixelFetcher.tick();
            gpu.tick();
            gpu.tick();
            //gpu.tick();
           // gpu.tick();

            if(cpu.getPc() == 0x100)
            {
                for(int i=0; i<0x100; i++)
                    memoryUnit.writeData(i, data[i] & 0xFF);
            }
        }

        //gui.refresh();
    }
}
