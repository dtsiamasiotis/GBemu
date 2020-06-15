package ppu;

public class Sprite {
    private int positionX;
    private int positionY;
    private int spriteNumber;
    private int priority;
    private boolean flipX;
    private boolean flipY;
    private int palette;
    private boolean alreadyShown = false;
    private int options;

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public void setSpriteNumber(int spriteNumber) {
        this.spriteNumber = spriteNumber;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setOptions(int options) { this.options = options; }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setAlreadyShown(boolean alreadyShown){
        this.alreadyShown = alreadyShown;
    }

    public boolean isAlreadyShown() {
        return alreadyShown;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }

    public boolean isXFlipped() {
        if((options & (1<<5)) == 0x20)
            return true;
        else
            return false;
    }

    public boolean isYFlipped() {
        if((options & (1<<6)) == 0x40)
            return true;
        else
            return false;
    }

    public boolean isOnTopOfBackground() {
        if((options & (1<<7)) == 0x80)
            return false;
        else
            return true;
    }
}
