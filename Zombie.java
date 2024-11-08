import java.awt.*;
public class Zombie {
    private double x, y; 
    private int health; 
    private double speed; 
    private Image[] walkingFrames; 
    private int currentFrame = 0; 
    private int animationDelay = 10; 
    private int frameCounter = 0; 


    public Zombie(int x, int y, int health, double speed, Image[] walkingFrames) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.speed = speed;
        this.walkingFrames = walkingFrames;
    }


    public void moveTowards(Player player) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        if (distance > 0) {
            x += (dx / distance) * speed; 
            y += (dy / distance) * speed; 
        }
        updateAnimation(); 
    }

    private void updateAnimation() {
        frameCounter++;
        if (frameCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % walkingFrames.length;
            frameCounter = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(walkingFrames[currentFrame], (int) x, (int) y, 20,  24, null);
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

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