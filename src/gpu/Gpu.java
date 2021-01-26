package gpu;

import gui.Gui;
import mmu.MemoryUnit;
import ppu.Fetcher;
import ppu.Pixel;
import ppu.PixelFIFO;
import ppu.Sprite;

import java.util.ArrayList;

public class Gpu {
    private int LY = 0;
    private MemoryUnit memoryUnit;
    private PixelFIFO pixelFIFO;
    private Gui gui;
    private int timer = 0;
    private int x = 0;
    private String state = "OAMSEARCH";
    private int HBLANKtimer = 0;
    private int VBLANKtimer = 0;
    private int OAMtimer = 0;
    private ArrayList<Sprite> visibleSprites=new ArrayList<>();
    private Fetcher fetcher;
    private int pixelTransferCycles = 0;

    public void increaseLY()
    {
        LY++;
        memoryUnit.writeData(0xFF44, LY);
    }

    public void resetLY()
    {
        LY = 0;
        memoryUnit.writeData(0xFF44, LY);
    }

    public void setMemoryUnit(MemoryUnit memoryUnit)
    {
        this.memoryUnit = memoryUnit;
    }

    public void setGui(Gui gui)
    {
        this.gui = gui;
    }

    public void setPixelFIFO(PixelFIFO pixelFIFO){ this.pixelFIFO = pixelFIFO; }

    public int getX() { return x; }

    public void scanLine()
    {
        Pixel pixel = pixelFIFO.removeFromFifo();

        if(pixel!=null)
        {

            int position = (LY*160)+x;
            int paletteRegister = 0;
            if(pixel.getType().equals("BG"))
                paletteRegister = memoryUnit.loadData(0xFF47);
            else{
                if(pixel.getObjPaletteNumber() == 0)
                    paletteRegister = memoryUnit.loadData(0xFF48);
                if(pixel.getObjPaletteNumber() == 1)
                    paletteRegister = memoryUnit.loadData(0xFF49);
            }

            try {
                gui.setPixelAtPosition(position, pixel.getBothBits(), paletteRegister);
            }catch (java.lang.ArrayIndexOutOfBoundsException e){}
            finally {
            //    System.out.println("LY:"+LY+"x:"+x+","+position);
            }
            
            x++;

        }
    }

    public void tick()
    {
  //      if(!LCDisON())
    //        return;


        if(OAMtimer==80)
        {
            state = "PIXELTRANSFER";
            OAMtimer = 0;
            int LCDstat = memoryUnit.loadData(0xFF41);
            memoryUnit.writeData(0xFF41,(LCDstat|0x3));
        }
        if(HBLANKtimer+(pixelTransferCycles)==376)
        {
            state = "OAMSEARCH";
            HBLANKtimer = 0;
        }
        if(VBLANKtimer==4560)
        {
            state = "OAMSEARCH";
            VBLANKtimer = 0;
            resetLY();
            fetcher.setIsDrawingWindow(false);
         //   checkForOAMInterrupt();
            memoryUnit.writeData(0xFF0F,0);
            int LCDstat = memoryUnit.loadData(0xFF41);
            memoryUnit.writeData(0xFF41,((LCDstat & 0b11111110))|0x2);
        }
        if(state.equals("HBLANK"))
        {
            HBLANKtimer++;
            return;
        }
        if(state.equals("VBLANK"))
        {
            if(VBLANKtimer%(456)==0) {
                increaseLY();
            }

            VBLANKtimer++;
            return;
        }
       /* if(timer==172) {
            //increaseLY();
            x = 0;
            timer = 0;
            state = "HBLANK";
            synchronized (this) {
                gui.refresh();
            }
        }*/

        if(LY==144) {
            state = "VBLANK";
           // System.out.println("VBLANK:"+System.currentTimeMillis());
            memoryUnit.writeData(0xFF0F,1);
            int LCDstat = memoryUnit.loadData(0xFF41);
            memoryUnit.writeData(0xFF41,(LCDstat|0x1));
            synchronized (this) {
                gui.refresh();
            }
        }

        if(x==160) {
            x = 0;
            increaseLY();
            pixelTransferCycles = timer;
            timer = 0;
            state = "HBLANK";
            int LCDstat = memoryUnit.loadData(0xFF41);
            memoryUnit.writeData(0xFF41,(LCDstat & 0b11111100));
            //checkForLYCLYInterrupt();
            //checkForHBlankInterrupt();

        }

        if(state.equals("OAMSEARCH"))
        {
            if(OAMtimer==79)
                handleOAMSearch();

            OAMtimer++;
        }
        if(state.equals("PIXELTRANSFER")) {
            if(startOfWindow() && !fetcher.isDrawingWindow())
                fetcher.startFetchingWindow();
            Sprite currentSprite = spriteInThisPosition(x);
            if(currentSprite!=null)
                fetcher.startFetchingSprite(currentSprite);

            scanLine();
            timer++;
        }

        checkForLYCLYInterrupt();
    }

    private boolean startOfWindow() {
        boolean windowStart = false;
        int LCDregister = memoryUnit.loadData(0xFF40);
        if((LCDregister & (1<<5))==0x20)
        {
            int wy = memoryUnit.loadData(0xFF4A);
            int wx = memoryUnit.loadData(0xFF4B);
            if(fetcher.hasOverlayWindow()) {
                if ((wx - 7) == x)
                    return true;
            }
            else {
                if ((wy * 160 + (wx - 7)) == (LY * 160) + x)
                    return true;
            }
        }

        return windowStart;
    }

    private void checkForLYCLYInterrupt() {
        int LYC = memoryUnit.loadData(0xFF45);
        if(LY == LYC)
        {
            int LCDstat = memoryUnit.loadData(0xFF41);
            if((LCDstat & 0b01000000) == 0x40)
                memoryUnit.writeData(0xFF0F,2);
        }
    }

    private void checkForOAMInterrupt() {
        int LCDstat = memoryUnit.loadData(0xFF41);
        if((LCDstat & 0b00100000) == 0x20)
            memoryUnit.writeData(0xFF0F,2);
    }

    private void checkForHBlankInterrupt() {
        int LCDstat = memoryUnit.loadData(0xFF41);
        if((LCDstat & 0b00001000) == 0x8)
            memoryUnit.writeData(0xFF0F,2);
    }

    public String getState(){
        return this.state;
    }

    public int getLY(){
        return this.LY;
    }

    public void setFetcher(Fetcher fetcher)
    {
        this.fetcher = fetcher;
    }

    public void handleOAMSearch()
    {
        visibleSprites.clear();
        for(int i=0;i<40;i++)
        {
            Sprite sprite = new Sprite();
            sprite.setPositionY(memoryUnit.loadData(65024+(i*4)));
            sprite.setPositionX(memoryUnit.loadData(65025+(i*4))-8);
            sprite.setSpriteNumber(memoryUnit.loadData(65026+(i*4)));
            sprite.setOptions(memoryUnit.loadData(65027+(i*4)));

            if(sprite.getPositionX()!=0 && (LY + 16>=sprite.getPositionY()) && (LY + 16<sprite.getPositionY()+fetcher.spriteSize()))
                visibleSprites.add(sprite);
        }
    }

    public Sprite spriteInThisPosition(int x)
    {
        for(Sprite sprite:visibleSprites) {
            if(sprite.getPositionX()==x && !sprite.isAlreadyShown()) {
                sprite.setAlreadyShown(true);
                return sprite;
            }
        }

        return null;
    }

    public boolean LCDisON()
    {
        int lcdRegister = memoryUnit.loadData(0xFF40);
        if((lcdRegister & 0x1) == 0x1)
            return true;
        else
            return false;
    }

    public boolean spriteSizeIs8By16()
    {
        int lcdRegister = memoryUnit.loadData(0xFF40);
        if((lcdRegister & 0x4) == 0x4)
            return true;
        else
            return false;
    }
}
