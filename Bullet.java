import java.awt.*;

public class Bullet {
    private int x, y;
    private int dx, dy;

    public Bullet(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, 10, 10);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}