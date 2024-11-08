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
        setSize(600, 600); // ปรับขนาดหน้าต่างเป็น 600x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.level = level;

        // Set countdown time based on the level
        if (level == 1) {
            timeRemaining = 70;
        } else if (level == 2) {
            timeRemaining = 100;
        }

        map = new Map(level);

        zombieImage = new ImageIcon("zombie.png").getImage();
        zombies = new ArrayList<>();
        spawnZombies();

        int playerStartX = 50; 
        int playerStartY = 50;

        // ตรวจสอบว่าตำแหน่งนี้ชนกับกำแพงหรือไม่
        while (checkCollisionWithWalls(playerStartX, playerStartY)) {
            playerStartX += 10;
            playerStartY += 10;
        }

        // Load walking frames for the player
        Image[] walkingFrames = {
            new ImageIcon("walk1.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk2.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk3.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH),
            new ImageIcon("walk4.png").getImage().getScaledInstance(20, 24, Image.SCALE_SMOOTH)
        };

        player = new Player(playerStartX, playerStartY, walkingFrames);
        bullets = new ArrayList<>();
        mousePosition = new Point(0, 0);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

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

        startGameTimer();

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shootBullet(e.getPoint());
            }
        });

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
                    showEndGameScreen(false);
                }
            }
        });
        gameTimer.start();
    }

    private void moveZombie(Zombie zombie) {
        double oldX = zombie.getX();
        double oldY = zombie.getY();
        zombie.moveTowards(player);

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
        double zombieSpeed = (level == 1) ? 0.5 : 0.75;

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

            zombies.add(new Zombie(zombieX, zombieY, zombieHealth, zombieSpeed, zombieFrames));
        }
    }

    private void shootBullet(Point target) {
        int playerWidth = 20;  // กำหนดความกว้างของผู้เล่นตรงนี้
        int playerHeight = 24; // กำหนดความสูงของผู้เล่นตรงนี้
    
        int playerCenterX = player.getX() + playerWidth / 2;
        int playerCenterY = player.getY() + playerHeight / 2;
    
        System.out.println("Player Center X: " + playerCenterX + ", Y: " + playerCenterY); // Debugging line
    
        int dx = target.x - playerCenterX;
        int dy = target.y - playerCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = (int) (dx / length * 4);
        dy = (int) (dy / length * 4);
    
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

            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 4, 4);
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

        for (Zombie zombie : zombies) {
            if (Math.abs(player.getX() - zombie.getX()) < 20 && Math.abs(player.getY() - zombie.getY()) < 20) {
                health -= damagePerHit;
                if (health <= 0) {
                    isGameOver = true;
                    showEndGameScreen(false);
                }
            }
        }

        if (zombies.isEmpty() && !isGameOver) {
            isVictory = true;
            showEndGameScreen(true);
        }
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