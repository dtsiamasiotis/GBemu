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

    public void scanLine()
    {
        Pixel pixel = pixelFIFO.removeFromFifo();

        if(pixel!=null)
        {

            int position = (LY*160)+x;

            try {
                gui.setPixelAtPosition(position, pixel.getBothBits());
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


        if(OAMtimer==80/4)
        {
            state = "PIXELTRANSFER";
            OAMtimer = 0;
        }
        if(HBLANKtimer+(pixelTransferCycles/4)==376/4)
        {
            state = "OAMSEARCH";
            HBLANKtimer = 0;
        }
        if(VBLANKtimer==4560/4)
        {
            state = "OAMSEARCH";
            VBLANKtimer = 0;
            resetLY();
            memoryUnit.writeData(0xFF0F,0);
        }
        if(state.equals("HBLANK"))
        {
            HBLANKtimer++;
            return;
        }
        if(state.equals("VBLANK"))
        {
            if(VBLANKtimer%(456/4)==0) {
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
            //System.out.println("VBLANK");
            memoryUnit.writeData(0xFF0F,1);
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
           // synchronized (this) {
          //      gui.refresh();
          //  }
        }

        if(state.equals("OAMSEARCH"))
        {
            if(OAMtimer==79/4)
                handleOAMSearch();

            OAMtimer++;
        }
        if(state.equals("PIXELTRANSFER")) {
            Sprite currentSprite = spriteInThisPosition(x);
            if(currentSprite!=null)
                fetcher.startFetchingSprite(currentSprite);
            scanLine();
            timer++;
        }
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
            if(sprite.getSpriteNumber()==0xa)
                System.out.println("sdffsfsdfs");
            if(sprite.getPositionX()!=0 && (LY + 16>=sprite.getPositionY()) && (LY + 16<sprite.getPositionY()+8))
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

}
