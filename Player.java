import java.awt.*;

public class Player {
    private int x, y;
    private int speed = 10; // กำหนดความเร็วของผู้เล่น
    private CharacterSprite sprite;

    public Player(int x, int y, Image[] frames) {
        this.x = x;
        this.y = y;
        this.sprite = new CharacterSprite(frames);
    }

    public void move(int dx, int dy) {
        x += dx * speed;
        y += dy * speed;
        sprite.update(); // อัปเดตเฟรมของภาพเมื่อมีการเคลื่อนไหว
    }

    public void draw(Graphics g) {
        g.drawImage(sprite.getCurrentFrame(), x, y, null);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; } // ให้ getter สำหรับความเร็ว
    public int getWidth() { return 64; } // ขนาดของผู้เล่น
    public int getHeight() { return 64; }
}