package gui;

import joypad.Joypad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Gui extends JPanel {
    private GuiPanel guiPanel;
    private Joypad joypad;
    public Gui()
    {
        guiPanel = new GuiPanel();
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
        JFrame f = new JFrame("GBemu");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Keylistener joypadListener = new Keylistener();
        joypadListener.setJoypad(this.joypad);
        f.addKeyListener(joypadListener);
        f.add(guiPanel);
        f.pack();
        f.setVisible(true);
    }

    public void setPixelAtPosition(int position, int pixelValue, int paletteRegister)
    {
        guiPanel.updatePalette(paletteRegister);
        guiPanel.setPixelAtPosition(position,pixelValue);
    }

    public void refresh()
    {
        guiPanel.refresh();
    }

    public void setJoypad(Joypad joypad) {
        this.joypad = joypad;
    }
}
