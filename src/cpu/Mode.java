package cpu;

public abstract class Mode {
    protected int duration;
    protected int remainingCycles;
    protected String description;
    public abstract int handleMode();

}
