import java.awt.*;

public class Player {
    private int x, y;
    private int speed = 10; 
    private CharacterSprite sprite;

    public Player(int x, int y, Image[] frames) {
        this.x = x;
        this.y = y;
        this.sprite = new CharacterSprite(frames);
    }

    public void move(int dx, int dy) {
        x += dx * speed;
        y += dy * speed;
        sprite.update(); 
    }

    public void draw(Graphics g) {
        g.drawImage(sprite.getCurrentFrame(), x, y, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; } 
    public int getWidth() { return 64; } 
    public int getHeight() { return 64; }
}