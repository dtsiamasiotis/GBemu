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
    private boolean isFetchingSprite = false;
    private boolean isFetchingBg = true;
    private boolean enqueueSpriteFetching = false;
    private Sprite spriteToShow;


    public int readTileNumber()
    {
        int tileNumber = memoryUnit.loadData(mapAddress);
        return tileNumber;
    }

    public int readData0(int tileNumber)
    {
        int tileNumberInt = (tileNumber & 0xFF);
        int tileLine = ((gpu.getLY()+getSCY())%256)%8;
        int tileNumberAddress = 0x8000 + (tileNumberInt * 2 * 8) + (tileLine * 2);
        return memoryUnit.loadData(tileNumberAddress);
    }

    public int readData1(int tileNumber)
    {
        int tileNumberInt = (tileNumber & 0xFF);
        int tileLine = ((gpu.getLY()+getSCY())%256)%8;
        int tileNumberAddress = 0x8000 + (tileNumberInt * 2 * 8) + (tileLine * 2) + 1;
        return memoryUnit.loadData(tileNumberAddress);
    }

    public int readSpriteData0(int spriteNumber)
    {
        int spriteLine = gpu.getLY()+16 - spriteToShow.getPositionY();
        int spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2);
        return memoryUnit.loadData(spriteNumberAddress);
    }

    public int readSpriteData1(int spriteNumber)
    {
        int spriteLine = gpu.getLY()+16 - spriteToShow.getPositionY();
        int spriteNumberAddress = 0x8000 + (spriteNumber * 2 * 8) + (spriteLine * 2) + 1;
        return memoryUnit.loadData(spriteNumberAddress);
    }

    public void setMapAddress(int mapAddress) {
        this.mapAddress = mapAddress;
    }

    public void setMemoryUnit(MemoryUnit memoryUnit){
        this.memoryUnit = memoryUnit;
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
                mapAddress = 0x9800 + ((((gpu.getLY()+getSCY())%256)/8)*32) + tileInRow;
                curTileNumber = readTileNumber();
                if(curTileNumber==0)
                    System.out.println("asfasfa");
                tileInRow++;
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
                timer++;
                state = "READSPRITEDATA1";
                break;
            case "READSPRITEDATA1":
                data1 = readSpriteData1(spriteToShow.getSpriteNumber());
                for(int i=7;i>=0;i--)
                {
                    Pixel pixel = createAPixel(data0,data1,i+1);
                    pixel.setType("S0");

                    tempPixels[7-i] = pixel;

                }
                state = "PUSHPIXELS";
                break;
            case "PUSHPIXELS":
                if(isFetchingSprite) {
                    for (int j = 0; j < 8; j++) {
                        if (tempPixels[j] != null) {
                            pixelFIFO.mixPixel(j, tempPixels[j]);
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

    public int getSCY()
    {
        this.SCY = memoryUnit.loadData(65346);
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
