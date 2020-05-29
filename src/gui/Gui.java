package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Gui extends JPanel {
    private GuiPanel guiPanel;
    public Gui()
    {
        guiPanel = new GuiPanel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(guiPanel);
        f.pack();
        f.setVisible(true);
    }

    public void setPixelAtPosition(int position, int pixelValue)
    {
        guiPanel.setPixelAtPosition(position,pixelValue);
    }

    public void refresh()
    {
        guiPanel.refresh();
    }
}
