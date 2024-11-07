import java.awt.*;
public class Zombie {
    private double x, y; // ตำแหน่งของซอมบี้
    private int health; // พลังชีวิตซอมบี้
    private double speed; // ความเร็วของซอมบี้

    public Zombie(int x, int y, int health, double speed) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
    }

    public void moveTowards(Player player) {
        // คำนวณระยะห่างและทิศทางไปยังผู้เล่น
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        // เคลื่อนที่ในทิศทางของผู้เล่นแบบอิสระ
        if (distance > 0) {
            x += (dx / distance) * speed; // ปรับตำแหน่ง X
            y += (dy / distance) * speed; // ปรับตำแหน่ง Y
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, 20, 20);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }

    // เพิ่มเมธอด setX และ setY
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    // เพิ่มเมธอดสำหรับการเคลื่อนไหวในแต่ละแกน (สำหรับการตรวจสอบการชน)
    public double getMoveX(Player player) {
        double dx = player.getX() - x;
        double distance = Math.sqrt(dx * dx + (player.getY() - y) * (player.getY() - y));
        return (distance > 0) ? (dx / distance) * speed : 0;
    }
    
    public double getMoveY(Player player) {
        double dy = player.getY() - y;
        double distance = Math.sqrt((player.getX() - x) * (player.getX() - x) + dy * dy);
        return (distance > 0) ? (dy / distance) * speed : 0;
    }
}