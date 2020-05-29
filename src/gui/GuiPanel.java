package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GuiPanel extends JPanel {
    private BufferedImage img;
    private int[] rgb;
    private int[] pallette;
    public GuiPanel(){
        setSize(160,144);
        setVisible(true);
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        img = gfxConfig.createCompatibleImage(160, 144);
        rgb = new int[160*144];
        pallette = new int[]{0xFFFFFF,0x000000,0x000000,0x000000,};

    }

    public void setPixelAtPosition(int position, int pixelValue)
    {
        rgb[position] = encodePixelToColorPallette(pixelValue);
    }

    public int encodePixelToColorPallette(int pixelValue)
    {
        return pallette[pixelValue];
    }

    public void refresh()
    {
            img.setRGB(0, 0, 160, 144, rgb, 0, 160);
            validate();
            repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);// <-- let panel draw itself
        Graphics2D g2d = (Graphics2D) g.create();
        // Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(img, 0, 0, 160*4, 144*4, null);

    }

    public Dimension getPreferredSize()
    {
        return new Dimension(160*4,144*4);
    }
}
