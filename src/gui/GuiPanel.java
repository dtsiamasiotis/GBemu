package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GuiPanel extends JPanel {
    private BufferedImage img;
    private int[] rgb;
    private int[] palette;
    private int[] availableColors;
    public GuiPanel(){
        setSize(160,144);
        setVisible(true);
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
                getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        img = gfxConfig.createCompatibleImage(160, 144);
        rgb = new int[160*144];
        palette = new int[4];//{0x9bbc0f,0x306230,0x8bac0f,0x0F380f};
        //availableColors = new int[]{0x9bbc0f,0x306230,0x8bac0f,0x0F380f};
        availableColors = new int[]{0xe0f8d0,0x88c070,0x346856,0x081820};
    }

    public void setPixelAtPosition(int position, int pixelValue)
    {
        rgb[position] = encodePixelToColorPallette(pixelValue);
    }

    public int encodePixelToColorPallette(int pixelValue)
    {

        return palette[pixelValue];

    }

    public void updatePalette(int paletteRegister) {
        int color0 = paletteRegister & 0b00000011;
        int color1 = (paletteRegister & 0b00001100) >> 2;
        int color2 = (paletteRegister & 0b00110000) >> 4;
        int color3 = (paletteRegister & 0b11000000) >> 6;
        palette[0] = availableColors[color0];
        palette[1] = availableColors[color1];
        palette[2] = availableColors[color2];
        palette[3] = availableColors[color3];
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
        g2d.drawImage(img, 0, 0, 160*2, 144*2, null);
        g2d.dispose();
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(160*2,144*2);
    }
}
