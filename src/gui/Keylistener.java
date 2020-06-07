package gui;

import joypad.Joypad;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keylistener implements KeyListener {

    private Joypad joypad;

    public void setJoypad(Joypad joypad) {
        this.joypad = joypad;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("key pressed"+keyEvent.getKeyCode());
        joypad.handleKeyPress(keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        joypad.handleKeyRelease();
    }
}
