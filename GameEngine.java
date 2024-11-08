import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class GameEngine extends JFrame {
    private Player player;
    private ArrayList<Bullet> bullets;
    private Point mousePosition;
    private int score = 0;
    private int level;
    private ArrayList<Zombie> zombies;
    private Image zombieImage;
    private int health = 100;
    private int damagePerHit = 5;
    private boolean isGameOver = false;
    private boolean isVictory = false;
    private int timeRemaining; // Time remaining in seconds for the level
    private Timer gameTimer; // Timer for countdown
    private Map map;

    public GameEngine(int level) {
        setTitle("Code Overload: Rise of the Undead - Level " + level);
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.level = level;

        // Set countdown time based on the level
        if (level == 1) {
            timeRemaining = 70; // Level 1: 50 seconds
        } else if (level == 2) {
            timeRemaining = 100; // Level 2: 100 seconds (1 minute 40 seconds)
        }

         // สร้างแผนที่โดยส่งพารามิเตอร์ level
         map = new Map(level); // ส่งค่า level เพื่อเลือกแมพที่ถูกต้อง

        zombieImage = new ImageIcon("zombie.png").getImage();
        zombies = new ArrayList<>();
        spawnZombies();
        
        // กำหนดตำแหน่งเริ่มต้นของผู้เล่น
    int playerStartX = 100;  // กำหนดตำแหน่ง X ที่ต้องการ
    int playerStartY = 110;  // กำหนดตำแหน่ง Y ที่ต้องการ

    // ตรวจสอบว่าตำแหน่งนี้ชนกับกำแพงหรือไม่
    while (checkCollisionWithWalls(playerStartX, playerStartY)) {
        // เลื่อนตำแหน่งเล็กน้อยถ้าพบการชนกับกำแพง
        playerStartX += 10;  // ปรับค่าตำแหน่งหากมีการชน
        playerStartY += 10;
    }

        // Load walking frames for the player
        Image[] walkingFrames = {
            new ImageIcon("walk1.png").getImage().getScaledInstance(34, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk2.png").getImage().getScaledInstance(34, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk3.png").getImage().getScaledInstance(34, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk4.png").getImage().getScaledInstance(34, 40, Image.SCALE_SMOOTH)
        };

        player = new Player(playerStartX, playerStartY, walkingFrames);
        bullets = new ArrayList<>();
        mousePosition = new Point(0, 0);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // Add controls for player movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> dy = -1;
                    case KeyEvent.VK_S -> dy = 1;
                    case KeyEvent.VK_A -> dx = -1;
                    case KeyEvent.VK_D -> dx = 1;
                }
                
                if (!checkCollisionWithWalls(player.getX() + dx * player.getSpeed(), player.getY() + dy * player.getSpeed())) {
                    player.move(dx, dy); // Move player if no wall collision
                    repaint();
                }
            }
        });

        // Start the game timer for countdown
        startGameTimer();

        // Mouse listener for shooting
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shootBullet(e.getPoint());
            }
        });

        // Main game loop timer
        Timer mainTimer = new Timer(30, e -> {
            if (!isGameOver && !isVictory) {
                for (Zombie zombie : zombies) {
                    moveZombie(zombie);
                }
                updateBullets();
                checkCollision();
                repaint();
            }
        });
        mainTimer.start();

        setVisible(true);
    }

    private void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            if (timeRemaining > 0) {
                timeRemaining--;
                if (timeRemaining == 0) {
                    isGameOver = true;
                    showEndGameScreen(false); // แสดงหน้าจอ Game Over เมื่อเวลาหมด
                }
            }
        });
        gameTimer.start();
    }

    private void moveZombie(Zombie zombie) {
        double oldX = zombie.getX();
        double oldY = zombie.getY();
        zombie.moveTowards(player);

        // Check collision with walls
        if (checkCollisionWithWalls(zombie.getX(), (int) oldY)) {
            zombie.setX(oldX);
        }
        if (checkCollisionWithWalls((int) oldX, zombie.getY())) {
            zombie.setY(oldY);
        }
    }

    private void spawnZombies() {
        zombies.clear();
        int numberOfZombies = (level == 1) ? 5 : 8;
        int zombieHealth = 100;
        double zombieSpeed;
    
        // Increase speed by 0.5x for level 1
        if (level == 1) {
            zombieSpeed = 0.75; // 0.5 * 1.5 = 0.75
        } else {
            zombieSpeed = 1.0;
        }
    
        Image[] zombieFrames = {
            new ImageIcon("zombie1.png").getImage().getScaledInstance(38, 45, Image.SCALE_SMOOTH),
            new ImageIcon("zombie2.png").getImage().getScaledInstance(38, 45, Image.SCALE_SMOOTH),
            new ImageIcon("zombie3.png").getImage().getScaledInstance(38, 45, Image.SCALE_SMOOTH),
            new ImageIcon("zombie4.png").getImage().getScaledInstance(38, 45, Image.SCALE_SMOOTH)
        };
    
        for (int i = 0; i < numberOfZombies; i++) {
            int zombieX, zombieY;
            do {
                zombieX = (int) (Math.random() * (getWidth() - 40));
                zombieY = (int) (Math.random() * (getHeight() - 40));
            } while (checkCollisionWithWalls(zombieX, zombieY));
    
            zombies.add(new Zombie(zombieX, zombieY, zombieHealth, zombieSpeed, zombieFrames));
        }
    }

    private void shootBullet(Point target) {
        int playerCenterX = player.getX() + player.getWidth() / 2;
        int playerCenterY = player.getY() + player.getHeight() / 2;

        int dx = target.x - playerCenterX;
        int dy = target.y - playerCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = (int) (dx / length * 5);
        dy = (int) (dy / length * 5);

        Bullet bullet = new Bullet(playerCenterX, playerCenterY, dx, dy);
        bullets.add(bullet);
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.move();

            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                it.remove();
                continue;
            }

            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 5, 5);
            boolean hitWall = false;
            for (Rectangle wall : map.getWalls()) {
                if (bulletBounds.intersects(wall)) {
                    hitWall = true;
                    break;
                }
            }

            if (hitWall) {
                it.remove();
            }
        }
    }

    private boolean checkCollisionWithWalls(int x, int y) {
        Rectangle futurePosition = new Rectangle(x, y, 20, 20);
        for (Rectangle wall : map.getWalls()) {
            if (futurePosition.intersects(wall)) {
                return true;
            }
        }
        return false;
    }

    private void checkCollision() {
        if (isGameOver || isVictory) return;
    
        // ตรวจสอบการชนของกระสุนและซอมบี้ (เหมือนเดิม)
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            Iterator<Zombie> zombieIt = zombies.iterator();
            while (zombieIt.hasNext()) {
                Zombie zombie = zombieIt.next();
                if (Math.abs(bullet.getX() - zombie.getX()) < 20 && Math.abs(bullet.getY() - zombie.getY()) < 20) {
                    zombie.takeDamage(10);
                    it.remove();
                    if (zombie.isDead()) {
                        zombieIt.remove();
                        score += 10;
                    }
                    break;
                }
            }
        }
    
        // ตรวจสอบการชนของผู้เล่นกับซอมบี้
        for (Zombie zombie : zombies) {
            if (Math.abs(player.getX() - zombie.getX()) < 20 && Math.abs(player.getY() - zombie.getY()) < 20) {
                health -= damagePerHit;
                if (health <= 0) {
                    isGameOver = true;
                    showEndGameScreen(false); // เรียกหน้าจอ Game Over
                }
            }
        }
    
        // ตรวจสอบว่าซอมบี้ทั้งหมดถูกกำจัดแล้วหรือไม่
        if (zombies.isEmpty() && !isGameOver) {
            isVictory = true;
            showEndGameScreen(true); // เรียกหน้าจอ Victory
        }
    }

    private void showEndGameScreen(boolean isVictory) {
        gameTimer.stop(); // หยุดการจับเวลาของเกม
        new EndScreen(isVictory, level, score); // เปิดหน้าจอ EndScreen โดยส่งค่า isVictory, level และ score ไป
        dispose(); // ปิดหน้าต่างเกมปัจจุบัน
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());

            map.draw(g);
            player.draw(g);

            for (Zombie zombie : zombies) {
                zombie.draw(g);
            }

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            // Draw health
            g.setColor(Color.RED);
            g.fillRect(10, 10, health * 2, 10);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 200, 10);

            // Draw time remaining in the top-right corner
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String timeText = "Time Remaining: " + timeRemaining + "s";
            int textWidth = g.getFontMetrics().stringWidth(timeText);
            g.drawString(timeText, getWidth() - textWidth - 20, 50);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameEngine(1).setVisible(true));
    }
}