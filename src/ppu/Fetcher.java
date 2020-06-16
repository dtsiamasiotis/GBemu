package ppu;

import gpu.Gpu;
import mmu.MemoryUnit;

public class Fetcher {
    private int mapAddress;
    private MemoryUnit memoryUnit;
    private PixelFIFO pixelFIFO = new PixelFIFO();
    private Pixel[] tempPixels = new Pixel[8];
    private int timer = 0;
    private int curTileNumber;
    private int tileLine;
    private int data0;
    private int data1;
    private Gpu gpu;
    private int tileInRow;
    private String state = "READTILEID";
    private int previousLY=0;
    private int SCY = 0;
    private int SCX = 0;
    private boolean isFetchingSprite = false;
    private boolean isFetchingBg = true;
    private boolean enqueueSpriteFetching = false;
    private Sprite spriteToShow;

    public int getLCDControlRegister()
    {
        return memoryUnit.loadData(0xFF40);
    }

    public int readTileNumber()
    {
        int tileNumber = memoryUnit.loadData(mapAddress);
        return tileNumber;
    }

    public int findTileDataSetAddress()
    {
        int lcdControlRegister = getLCDControlRegister();

        if((lcdControlRegister & 0x10) == 0)
            return 0x9000;
        else
            return 0x8000;
    }

    public boolean isSignedTileId()
    {
        return ((getLCDControlRegister() & 0x10) == 0);
    }

    public int idToSigned(int value)
    {
        if((value & (1<<7)) == 0)
            return value;
        else
            return value - 0x100;
    }

    public int readData0(int tileNumber)
    {
        int tileNumberInt = 0;
        if(isSignedTileId())
            tileNumberInt = idToSigned(tileNumber);
        else
            tileNumberInt = (tileNumber & 0xFF);
        int tileLine = ((gpu.getLY()+getSCY())%256)%8;
        int tileNumberAddress = findTileDataSetAddress() + (tileNumberInt * 2 * 8) + (tileLine * 2);
        return memoryUnit.loadData(tileNumberAddress);
    }

    public int readData1(int tileNumber)
    {
        int tileNumberInt = 0;
        if(isSignedTileId())
            tileNumberInt = idToSigned(tileNumber);
        else
            tileNumberInt = (tileNumber & 0xFF);
        int tileLine = ((gpu.getLY()+getSCY())%256)%8;
        int tileNumberAddress = findTileDataSetAddress() + (tileNumberInt * 2 * 8) + (tileLine * 2) + 1;
        return memoryUnit.loadData(tileNumberAddress);
    }

    public int readSpriteData0(int spriteNumber)
    {
        int spriteLine = 0;
        int spriteNumberAddress = 0x8000;
        if(spriteToShow.isYFlipped()) {
            spriteLine = 7 - (gpu.getLY() + 16 - spriteToShow.getPositionY());
            spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2);
        }
        else
        {
            spriteLine = gpu.getLY() + 16 - spriteToShow.getPositionY();
            spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2);
        }
        return memoryUnit.loadData(spriteNumberAddress);
    }

    public int readSpriteData1(int spriteNumber)
    {
        int spriteLine = 0;
        int spriteNumberAddress = 0x8000;
        if(spriteToShow.isYFlipped()) {
            spriteLine = 7 - (gpu.getLY() + 16 - spriteToShow.getPositionY());
            spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2) + 1;
        }
        else
        {
            spriteLine = gpu.getLY() + 16 - spriteToShow.getPositionY();
            spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2) + 1;
        }
        return memoryUnit.loadData(spriteNumberAddress);
    }

    public void setMapAddress(int mapAddress) {
        this.mapAddress = mapAddress;
    }

    public void setMemoryUnit(MemoryUnit memoryUnit){
        this.memoryUnit = memoryUnit;
      //  memoryUnit.writeData(0xFF40,0x91);
    }

    public Pixel createAPixel(int data0,int data1,int bitPos)
    {
        int firstBit = (data0&(1<<(bitPos-1)))!=0?1:0;
        int secondBit = (data1&(1<<(bitPos-1)))!=0?1:0;
        Pixel pixel = new Pixel();
        pixel.setFirstBit(firstBit);
        pixel.setSecondBit(secondBit);
        return pixel;
    }

    public PixelFIFO getPixelFIFO()
    {
        return this.pixelFIFO;
    }

    public void setGpu(Gpu gpu){ this.gpu = gpu; }

    public void tick()
    {

        switch(state){
            case "READTILEID":
                mapAddress = 0x9800 + ((((gpu.getLY()+getSCY())%256)/8)*32) + (getSCX()/0x08) + tileInRow;
                curTileNumber = readTileNumber();
                timer++;
                state = "READTILEDATA0";
                break;
            case "READTILEDATA0":
                data0 = readData0(curTileNumber);
                timer++;
                state = "READTILEDATA1";
                break;
            case "READTILEDATA1":
                data1 = readData1(curTileNumber);
                for(int i=7;i>=0;i--)
                {
                    Pixel pixel = createAPixel(data0,data1,i+1);
                    pixel.setType("BG");
                    pixel.setId(curTileNumber);
                    tempPixels[7-i] = pixel;

                }
                state = "PUSHPIXELS";
                break;
            case "READSPRITEID":
               // mapAddress = 38912 + ((((gpu.getLY()+getSCY())%256)/8)*32);
               // curTileNumber = readTileNumber();
                timer++;
                state = "READSPRITEDATA0";
                break;
            case "READSPRITEDATA0":
                data0 = readSpriteData0(spriteToShow.getSpriteNumber());
                if(spriteToShow.getSpriteNumber()==0x7c)
                    System.out.println("sfsdfsdfs");
                timer++;
                state = "READSPRITEDATA1";
                break;
            case "READSPRITEDATA1":
                data1 = readSpriteData1(spriteToShow.getSpriteNumber());
                for(int i=7;i>=0;i--)
                {
                    Pixel pixel = createAPixel(data0,data1,i+1);
                    pixel.setType("S0");
                    pixel.setId(spriteToShow.getSpriteNumber());
                    tempPixels[7-i] = pixel;

                }
                state = "PUSHPIXELS";
                break;
            case "PUSHPIXELS":
                if(isFetchingSprite) {
                    if(!spriteToShow.isXFlipped()) {
                        for (int j = 0; j < 8; j++) {
                            if (tempPixels[j] != null) {
                                pixelFIFO.mixPixel(j, tempPixels[j],spriteToShow.isOnTopOfBackground());
                            }
                        }
                    }
                    else
                    {
                        for (int j = 7; j >= 0; j--) {
                            if (tempPixels[j] != null) {
                                pixelFIFO.mixPixel(7-j, tempPixels[j],spriteToShow.isOnTopOfBackground());
                            }
                        }
                    }
                }
                else{
                    if (pixelFIFO.getSize() <= 8) {
                        for (int j = 0; j < 8; j++) {
                            if (tempPixels[j] != null) {
                                pixelFIFO.addToFifo(tempPixels[j]);
                            }
                        }
                    }
                    tileInRow++;
                }
                    //if(isFetchingSprite)
                        startFetchingBg();

                }
                timer = 0;

        }

    public void setState(String state)
    {
        this.state = state;
    }

    public void resetTileInRow()
    {
        tileInRow = 0;
    }

    public int getSCX()
    {
        this.SCX = memoryUnit.loadData(0xFF43);
        return this.SCX;
    }

    public int getSCY()
    {
        this.SCY = memoryUnit.loadData(0xFF42);
        return this.SCY;
    }

    public void startFetchingSprite(Sprite spriteToFetch)
    {
        if(isFetchingSprite)
            return;

        isFetchingSprite = true;
        isFetchingBg = false;
        pixelFIFO.setCanRemovePixel(false);
        state = "READSPRITEID";
        spriteToShow = spriteToFetch;
    }

    public void startFetchingBg()
    {
        isFetchingSprite = false;
        isFetchingBg = true;
        pixelFIFO.setCanRemovePixel(true);
        state = "READTILEID";
    }

    public void enqueueSpriteFetching()
    {
        //pixelFIFO.setCanRemovePixel(false);
        enqueueSpriteFetching = true;
    }

    public void setSpriteToShow(Sprite spriteToShow)
    {
        this.spriteToShow = spriteToShow;
    }
}
