import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;

public class GameEngine extends JFrame {
    private Player player;
    private ArrayList<Bullet> bullets;
    private int score = 0;
    private int level;
    private ArrayList<Zombie> zombies;
    private int health = 100;
    private boolean isGameOver = false;
    private boolean isVictory = false;
    private int timeRemaining;
    private Timer gameTimer;
    private Map map;
    private int damagePerHit = 10; // จำนวนพลังชีวิตที่ลดเมื่อผู้เล่นชนกับซอมบี้

    public GameEngine(int level) {
        setTitle("Code Overload: Rise of the Undead - Level " + level);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.level = level;
        timeRemaining = (level == 1) ? 70 : 100;
        map = new Map(level);

        zombies = new ArrayList<>();
        spawnZombies();

        int playerStartX = 50, playerStartY = 50;
        while (checkCollisionWithWalls(playerStartX, playerStartY)) {
            playerStartX += 10;
            playerStartY += 10;
        }

        Image[] walkingFrames = {
            new ImageIcon("walk1.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk2.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk3.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk4.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH)
        };
        player = new Player(playerStartX, playerStartY, walkingFrames);
        bullets = new ArrayList<>();

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        setupKeyListener();
        startGameTimer();

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shootBullet(e.getPoint());
            }
        });

        new Timer(30, e -> gameLoop()).start();
        setVisible(true);
    }

    private void setupKeyListener() {
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
                    player.move(dx, dy);
                    repaint();
                }
            }
        });
    }

    private void startGameTimer() {
        gameTimer = new Timer(1000, e -> {
            if (--timeRemaining <= 0) {
                isGameOver = true;
                showEndGameScreen(false);
            }
        });
        gameTimer.start();
    }

    private void spawnZombies() {
        zombies.clear();
        int numberOfZombies = (level == 1) ? 5 : 8;
        Image[] zombieFrames = {
            new ImageIcon("zombie1.png").getImage().getScaledInstance(24, 20, Image.SCALE_SMOOTH),
            new ImageIcon("zombie2.png").getImage().getScaledInstance(24, 20, Image.SCALE_SMOOTH),
            new ImageIcon("zombie3.png").getImage().getScaledInstance(24, 20, Image.SCALE_SMOOTH),
            new ImageIcon("zombie4.png").getImage().getScaledInstance(24, 20, Image.SCALE_SMOOTH)
        };
        for (int i = 0; i < numberOfZombies; i++) {
            int zombieX, zombieY;
            do {
                zombieX = (int) (Math.random() * (getWidth() - 40));
                zombieY = (int) (Math.random() * (getHeight() - 40));
            } while (checkCollisionWithWalls(zombieX, zombieY));
            zombies.add(new Zombie(zombieX, zombieY, 100, (level == 1) ? 0.5 : 0.75, zombieFrames));
        }
    }

    private void shootBullet(Point target) {
        int playerCenterX = player.getX() + 10; // Half width (20/2)
        int playerCenterY = player.getY() + 12; // Half height (24/2)
        int dx = target.x - playerCenterX, dy = target.y - playerCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);
        bullets.add(new Bullet(playerCenterX, playerCenterY, (int)(dx / length * 4), (int)(dy / length * 4)));
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.move();
    
            // ตรวจสอบว่ากระสุนหลุดออกจากขอบหน้าจอหรือไม่
            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                it.remove(); // ลบกระสุนหากหลุดขอบ
                continue;
            }
    
            // ตรวจสอบการชนของกระสุนกับกำแพง
            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 4, 4);
            boolean hitWall = false;
            for (Rectangle wall : map.getWalls()) {
                if (bulletBounds.intersects(wall)) {
                    hitWall = true;
                    break;
                }
            }
    
            if (hitWall) {
                it.remove(); // ลบกระสุนเมื่อชนกำแพง
            }
        }
    }

    private boolean checkCollisionWithWalls(int x, int y) {
        Rectangle futurePosition = new Rectangle(x, y, 20, 20);
        return map.getWalls().stream().anyMatch(wall -> wall.intersects(futurePosition));
    }

    private void checkCollision() {
        if (isGameOver || isVictory) return;
    
        // ตรวจสอบการชนของกระสุนกับซอมบี้
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Zombie> zombieIterator = zombies.iterator();
            while (zombieIterator.hasNext()) {
                Zombie zombie = zombieIterator.next();
                if (Math.abs(bullet.getX() - zombie.getX()) < 20 && Math.abs(bullet.getY() - zombie.getY()) < 20) {
                    zombie.takeDamage(10);
                    bulletIterator.remove();
                    if (zombie.isDead()) {
                        zombieIterator.remove();
                        score += 10;
                    }
                    break;
                }
            }
        }
    
        // ตรวจสอบการชนของผู้เล่นกับซอมบี้
        for (Zombie zombie : zombies) {
            if (Math.abs(player.getX() - zombie.getX()) < 20 && Math.abs(player.getY() - zombie.getY()) < 20) {
                health -= damagePerHit; // ลดเลือดของผู้เล่น
                if (health <= 0) {
                    isGameOver = true;
                    showEndGameScreen(false); // แสดงหน้าจอ Game Over เมื่อสุขภาพหมด
                }
            }
        }
    
        // ตรวจสอบว่าซอมบี้ทั้งหมดถูกกำจัดแล้วหรือไม่
        if (zombies.isEmpty() && !isGameOver) {
            isVictory = true;
            showEndGameScreen(true);
        }
    }

    private void gameLoop() {
        if (!isGameOver && !isVictory) {
            zombies.forEach(this::moveZombie);
            updateBullets();
            checkCollision();
            repaint();
        }
    }

    private void moveZombie(Zombie zombie) {
        double oldX = zombie.getX(), oldY = zombie.getY();
        zombie.moveTowards(player);
        if (checkCollisionWithWalls(zombie.getX(), (int) oldY)) zombie.setX(oldX);
        if (checkCollisionWithWalls((int) oldX, zombie.getY())) zombie.setY(oldY);
    }

    private void showEndGameScreen(boolean isVictory) {
        gameTimer.stop();
        new EndScreen(isVictory, level, score);
        dispose();
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            map.draw(g);
            player.draw(g);
            zombies.forEach(zombie -> zombie.draw(g));
            bullets.forEach(bullet -> bullet.draw(g));

            g.setColor(Color.RED);
            g.fillRect(10, 10, health * 2, 10);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 200, 10);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            String timeText = "Time Remaining: " + timeRemaining + "s";
            int textWidth = g.getFontMetrics().stringWidth(timeText);
            g.drawString(timeText, getWidth() - textWidth - 20, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameEngine(1).setVisible(true));
    }
}