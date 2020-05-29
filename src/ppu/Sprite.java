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
}
