package tileViewer;

import gui.Keylistener;
import joypad.Joypad;
import mmu.MemoryUnit;
import ppu.Pixel;

import javax.swing.*;

public class TileViewer extends JPanel {
    private TileViewerPanel tileViewerPanel;
    private MemoryUnit tilesMemory;

    public TileViewer()
    {
        tileViewerPanel = new TileViewerPanel();
       // SwingUtilities.invokeLater(new Runnable() {
       //     public void run() {
        //        createAndShowGUI();
       //     }
      //  });
    }
    public void runGui()
    {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                 }
              });
    }

    private void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Tileviewer");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(tileViewerPanel);
        f.pack();
        f.setVisible(true);
    }

    public void setPixelAtPosition(int position, int pixelValue, int paletteRegister)
    {
        tileViewerPanel.updatePalette(paletteRegister);
        tileViewerPanel.setPixelAtPosition(position,pixelValue);
    }

    public void refresh(int address)
    {
        int k = 0;
        int tileLines = 0;
        int tileId = 0;
        int offset = 0;
        //for(int i=0x8000; i<=0x9800-0x1; i+=2) {
        for(int i=0x8000; i<=0x9800-0x1; i+=2) {
            if(tileLines==8) {
                tileLines = 0;
                tileId++;
                if(tileId % 16 == 0) {
                    offset++;
                    tileId = 0;
                }
            }

            int data0 = tilesMemory.loadData(i);
            int data1 = tilesMemory.loadData(i+1);
            k=0;
            for(int j=7;j>=0;j--)
            {
                Pixel pixel = createAPixel(data0,data1,j+1);
                int pixelPosition = (offset*(128*8))+(tileLines*128)+(tileId*8)+k;
                setPixelAtPosition(pixelPosition,pixel.getBothBits(),tilesMemory.loadData(0xFF47));
                k++;

            }
            tileLines++;

        }
        tileViewerPanel.refresh();
    }

    public void setTilesMemory(MemoryUnit tilesMemory) {
        this.tilesMemory = tilesMemory;
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
}
