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
import serial.SerialPort;
import timer.Timer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        Gui gui = new Gui();
        disassembler reader = new disassembler();
        int cartridge[] = reader.readFile(args[1]);
        int mainMem[] = new int[65536];
        MemoryUnit memoryUnit = new MemoryUnit();

        Fetcher pixelFetcher = new Fetcher();
        Gpu gpu = new Gpu();
        Cpu cpu = new Cpu();
        Joypad joypad = new Joypad();
        SerialPort serialPort = new SerialPort();
        serialPort.setMemoryUnit(memoryUnit);
        memoryUnit.setSerialPort(serialPort);
        memoryUnit.setJoypad(joypad);
        InterruptManager interruptManager = new InterruptManager();
        Timer timer = new Timer();
        timer.setMemoryUnit(memoryUnit);
        interruptManager.setMemoryUnit(memoryUnit);
        cpu.setInterruptManager(interruptManager);

        int bootrom[] = reader.readBootRom(args[0]);
        memoryUnit.setBootrom(bootrom);
        memoryUnit.setCartridge(cartridge);
       // memoryUnit.writeBootRom(bootrom);
        /*int memSize = data.length + bootrom.length;
        int tempMem[];
        if(memSize < 65536)
            tempMem = new int[65536];
        else
            tempMem = new int[memSize];

        for(int i=0;i<=0xFF;i++)
            tempMem[i] = bootrom[i] & 0xFF;

        for(int i=0x100; i<data.length; i++)
            tempMem[i] = data[i] & 0xFF;

        memoryUnit.setMainMem(tempMem);*/
        //for(int i=0;i<=0xFF;i++)
            //mainMem[i] = bootrom[i] & 0xFF;
        memoryUnit.setMainMem(mainMem);
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
       /* int dividerCounter = 0;
        int timerCounter = 0;
        int k=0;*/

        Runnable task = new Runnable() {
            private int k=0;
            int dividerCounter = 0;
            int timerCounter = 0;

            public void run() {
                //while (true) {

                    if ((timerCounter == timer.cyclesToIncreaseCounter() - 1) && timer.isRunning()) {
                        timer.increaseCounter();
                        timerCounter = 0;
                    }

                    if (timer.isRunning())
                        timerCounter++;

                    if (dividerCounter == timer.cyclesToIncreaseDivider() - 1) {
                        timer.increaseDivider();
                        dividerCounter = 0;
                    }

                    dividerCounter++;

                    if (this.k == 100)
                        k = 0;


                    cpu.tick();
                    gpu.tick();
                    if (gpu.getState().equals("HBLANK")) {
                        pixelFetcher.getPixelFIFO().dropAll();
                        pixelFetcher.setState("READTILEID");
                        pixelFetcher.resetTileInRow();
                        pixelFetcher.setStartOfMap(0x9800);
                    }
                    if (gpu.getState().equals("PIXELTRANSFER") && k % 2 == 0)
                        pixelFetcher.tick();

                    k++;
                    serialPort.tick();
                    // if(cpu.getPc() == 0x100)
                    // {
                    //     for(int i=0; i<0x100; i++)
                    //        memoryUnit.writeData(i, cartridge[i] & 0xFF);
                    //}
               // System.out.println("end:"+System.nanoTime());
                //}
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
       // executor.execute(task);
        executor.scheduleAtFixedRate(task, 0, 238, TimeUnit.NANOSECONDS);
       /* while(true)
        {
            //for(int i = 0; i<10; i++)
            if((timerCounter == timer.cyclesToIncreaseCounter()-1) && timer.isRunning()) {
                timer.increaseCounter();
                timerCounter = 0;
            }

            if(timer.isRunning())
                timerCounter++;

            if(dividerCounter == timer.cyclesToIncreaseDivider()-1) {
                timer.increaseDivider();
                dividerCounter = 0;
            }

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

           // if(cpu.getPc() == 0x100)
           // {
           //     for(int i=0; i<0x100; i++)
            //        memoryUnit.writeData(i, cartridge[i] & 0xFF);
            //}
        }*/


    }


}
