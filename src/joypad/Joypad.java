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
        if(keycode==10)
        {
           buttons.add(10);
        }

        memoryUnit.writeData(0xFF0F,0x4);
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
            if(button==10 && temp==0b00010000)
            {
              result = temp & (0xFF & ~0x4);
              found = true;
            }
        }
        if(found)
            buttons.clear();

        return result;
    }
}
