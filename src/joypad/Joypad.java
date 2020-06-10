package joypad;

import mmu.MemoryUnit;

import java.util.ArrayList;

public class Joypad {

    private MemoryUnit memoryUnit;
    private ArrayList<Integer> buttons = new ArrayList<>();
    private int temp;

    public void setMemoryUnit(MemoryUnit memoryUnit) {
        this.memoryUnit = memoryUnit;
    }

    public void handleKeyPress(int keycode)
    {
        buttons.add(keycode);

        memoryUnit.writeData(0xFF0F,memoryUnit.loadData(0xFF0F)|(1<<4));
    }

    public void handleKeyRelease()
    {
        buttons.clear();
    }

    public void setTemp(int value)
    {
        temp = value & 0b00110000;
    }
    public int getJoypadRegister()
    {
        boolean found = false;
        int result = temp | 0b11001111;
        for(Integer button:buttons)
        {

            if(button==16 && temp==0b00010000) //SELECT
            {
              result = result & (0xFF & ~0x4);
              found = true;
            }
            if(button==10 && temp==0b00010000) //START
            {
                result = result & (0xFF & ~0x8);
                found = true;
            }
            if(button==65 && temp==0b00010000) //A
            {
                result = result & (0xFF & ~0x1);
                found = true;
            }
            if(button==83 && temp==0b00010000) //B
            {
                result = result & (0xFF & ~0x2);
                found = true;
            }
            if(button==37 && temp==0b00100000) //LEFT
            {
                result = result & (0xFF & ~0x2);
                found = true;
            }
            if(button==38 && temp==0b00100000) //UP
            {
                result = result & (0xFF & ~0x4);
                found = true;
            }
            if(button==39 && temp==0b00100000) //RIGHT
            {
                result = result & (0xFF & ~0x1);
                found = true;
            }
            if(button==40 && temp==0b00100000) //DOWN
            {
                result = result & (0xFF & ~0x8);
                found = true;
            }


        }
       // if(found)
           // buttons.clear();

        return result;
    }
}
