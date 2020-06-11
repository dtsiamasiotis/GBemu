package ppu;

public class Pixel {
    private int firstBit;
    private int secondBit;
    private String type;
    private int id;

    public void setFirstBit(int firstBit) {
        this.firstBit = firstBit;
    }

    public void setSecondBit(int secondBit) {
        this.secondBit = secondBit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) { this.id = id; }

    public int getFirstBit() {
        return firstBit;
    }

    public int getSecondBit() {
        return secondBit;
    }

    public int getId() {
        return id;
    }

    public int getBothBits(){
        int first = firstBit<<1;
        return first|secondBit;
    }


}
