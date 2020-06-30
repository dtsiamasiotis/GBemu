package serial;

import mmu.MemoryUnit;


public class SerialPort {

    private int sb;
    private int sc;
    boolean transferInProgress = false;
    private int divider = 0;
    private MemoryUnit memoryUnit;

    public void setMemoryUnit(MemoryUnit memoryUnit) {
        this.memoryUnit = memoryUnit;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public int getSb() {
        return sb;
    }

    public int getSc() {
        return sc;
    }

    public void tick()
    {
        if (!transferInProgress) {
            return;
        }
        if (++divider >= 4194304 / 8192 ) {
            transferInProgress = false;
            sb = 0xFF;

            memoryUnit.writeData(0xFF0F,memoryUnit.loadData(0xFF0F)|(1<<3));
        }
    }

    public void startTransfer() {
        transferInProgress = true;
        divider = 0;
    }
}
