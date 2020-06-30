package ppu;

public class Pixel {
    private int firstBit;
    private int secondBit;
    private String type;
    private int id;
    private int objPaletteNumber;

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

    public void setObjPaletteNumber(int objPaletteNumber) {
        this.objPaletteNumber = objPaletteNumber;
    }

    public int getFirstBit() {
        return firstBit;
    }

    public int getSecondBit() {
        return secondBit;
    }

    public int getId() {
        return id;
    }

    public int getObjPaletteNumber() {
        return objPaletteNumber;
    }

    public String getType() {
        return type;
    }

    public int getBothBits(){
       // int first = firstBit<<1;
       // return first|secondBit;
        int second = secondBit<<1;
        return second|firstBit;

    }


}
