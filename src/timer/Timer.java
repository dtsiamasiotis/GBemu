package timer;

import mmu.MemoryUnit;

public class Timer {
    private MemoryUnit memoryUnit;

    public void setMemoryUnit(MemoryUnit memoryUnit) {
        this.memoryUnit = memoryUnit;
    }

    public void increaseCounter()
    {
        int[] memory = memoryUnit.getMainMem();

        if(memory[0xFF05]>=255) {
            memory[0xFF05] = memory[0xFF06];
            memoryUnit.writeData(0xFF0F,4);
        }
        else
            memory[0xFF05] = memory[0xFF05]+1;

        memoryUnit.setMainMem(memory);
    }

    public boolean isRunning()
    {
        int control = memoryUnit.loadData(0xFF07);
        int running = control & 0x4;
        if(running==0)
            return false;

        return true;
    }

    public int cyclesToIncreaseCounter()
    {
        int control = memoryUnit.loadData(0xFF07);
        int speed = control & 0x3;
        switch (speed)
        {
            case 0:
                return 1024;
            case 1:
                return 16;
            case 2:
                return 64;
            case 3:
                return 255;
            default:
                return 0;
        }
    }


}
