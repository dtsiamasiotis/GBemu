package interrupts;

import mmu.MemoryUnit;

public class InterruptManager {
    private MemoryUnit memoryUnit;
    private int IME;

    public void setMemoryUnit(MemoryUnit memoryUnit) {
        this.memoryUnit = memoryUnit;
    }

    public int getIE()
    {
        return this.memoryUnit.loadData(0xFFFF);
    }

    public int getIF()
    {
        return this.memoryUnit.loadData(0xFF0F);
    }

    public int getIME() {
        return IME;
    }

    public void setIME(int IME)
    {
        this.IME = IME;
    }
}
