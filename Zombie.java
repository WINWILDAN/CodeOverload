import java.awt.*;
public class Zombie {
    private double x, y; // Position of the zombie
    private int health; // Health of the zombie
    private double speed; // Speed of the zombie
    private Image[] walkingFrames; // Array for animation frames
    private int currentFrame = 0; // Current frame index for animation
    private int animationDelay = 10; // Delay between frame updates (higher means slower animation)
    private int frameCounter = 0; // Counter to keep track of animation timing


    public Zombie(int x, int y, int health, double speed, Image[] walkingFrames) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
        this.walkingFrames = walkingFrames;
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
        updateAnimation(); // Update the animation frame each time the zombie moves
    }

    private void updateAnimation() {
        frameCounter++;
        if (frameCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % walkingFrames.length;
            frameCounter = 0;
        }
    }

    public void draw(Graphics g) {
        // Draw the current frame at the zombie's position
        g.drawImage(walkingFrames[currentFrame], (int) x, (int) y, 40,  48, null);
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