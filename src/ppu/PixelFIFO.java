package ppu;

import java.util.LinkedList;
import java.util.Queue;

public class PixelFIFO {
    private LinkedList<Pixel> fifo;
    private boolean canRemovePixel;

    public PixelFIFO()
    {
        fifo = new LinkedList<Pixel>();
        canRemovePixel = false;
    }

    public void addToFifo(Pixel pixel)
    {
        fifo.add(pixel);
        if(fifo.size()>8)
            canRemovePixel = true;
    }

    public Pixel removeFromFifo()
    {
        if(canRemovePixel && fifo.size()>8)
            return fifo.poll();
        else
            return null;
    }

    public int getSize()
    {
        return fifo.size();
    }

    public void dropAll()
    {
        fifo.clear();
    }

    public void setCanRemovePixel(boolean canRemovePixel)
    {
        this.canRemovePixel = canRemovePixel;
    }

    public void mixPixel(int position,Pixel newPixel,boolean isOnTop)
    {
        if((newPixel.getBothBits()!=0) && (isOnTop||fifo.get(position).getBothBits()==0)) {
            fifo.remove(position);
            fifo.add(position,newPixel);
        }

    }
}
