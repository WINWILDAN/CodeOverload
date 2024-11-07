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
    private int health = 100;
    private int damagePerHit = 5;
    private boolean isGameOver = false;
    private boolean isVictory = false;
    private Map map;

    public GameEngine(int level) {
        setTitle("Code Overload: Rise of the Undead - Level " + level);
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.level = level;

        map = new Map();

        zombies = new ArrayList<>();
        spawnZombies();

        player = new Player(100, 100);
        bullets = new ArrayList<>();
        mousePosition = new Point(0, 0);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> dy = -10;
                    case KeyEvent.VK_S -> dy = 10;
                    case KeyEvent.VK_A -> dx = -10;
                    case KeyEvent.VK_D -> dx = 10;
                }

                if (!checkCollisionWithWalls(player.getX() + dx, player.getY() + dy)) {
                    player.move(dx, dy);  // ย้ายผู้เล่นถ้าไม่มีการชนกับกำแพง
                }
                repaint();
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                shootBullet(e.getPoint());
            }
        });

        gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
                repaint();
            }
        });

        Timer timer = new Timer(30, e -> {
            if (!isGameOver && !isVictory) {
                for (Zombie zombie : zombies) {
                    moveZombie(zombie);  // อัปเดตตำแหน่งซอมบี้แบบหลบกำแพง
                }
                updateBullets();
                checkCollision();
                repaint();
            }
        });
        timer.start();

        setVisible(true);
    }

    private void moveZombie(Zombie zombie) {
        // เรียก moveTowards ของซอมบี้ให้คำนวณทิศทาง
        double oldX = zombie.getX();
        double oldY = zombie.getY();
        zombie.moveTowards(player);
    
        // ตรวจสอบการชนกับกำแพง
        if (checkCollisionWithWalls(zombie.getX(), (int) oldY)) {
            zombie.setX(oldX); // คืนค่า X กลับถ้ามีการชนในแกน X
        }
        if (checkCollisionWithWalls((int) oldX, zombie.getY())) {
            zombie.setY(oldY); // คืนค่า Y กลับถ้ามีการชนในแกน Y
        }
    }

    private void spawnZombies() {
        zombies.clear();
        int numberOfZombies = (level == 1) ? 5 : 8;
        int zombieHealth = 100;
        double zombieSpeed = (level == 1) ? 0.5 : 1.0;

        for (int i = 0; i < numberOfZombies; i++) {
            zombies.add(new Zombie((int) (Math.random() * getWidth()), (int) (Math.random() * getHeight()), zombieHealth, zombieSpeed));
        }
    }

    private void shootBullet(Point target) {
        int dx = target.x - player.getX();
        int dy = target.y - player.getY();
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = (int) (dx / length * 5);
        dy = (int) (dy / length * 5);

        Bullet bullet = new Bullet(player.getX(), player.getY(), dx, dy);
        bullets.add(bullet);
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.move();
            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                it.remove();
            }
        }
    }

    private boolean checkCollisionWithWalls(int x, int y) {
        Rectangle futurePosition = new Rectangle(x, y, 20, 20);
        for (Rectangle wall : map.getWalls()) {
            if (futurePosition.intersects(wall)) {
                return true; // ชนกับกำแพง
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
                    showEndGameDialog("Game Over! Your score: " + score, "Game Over");
                }
            }
        }

        if (zombies.isEmpty() && !isGameOver) {
            isVictory = true;
            showEndGameDialog("You Win! Your score: " + score, "Victory");
        }
    }

    private void showEndGameDialog(String message, String title) {
        String[] options = (title.equals("Victory") && level == 1) ? new String[]{"Next Level", "Exit Game"} : new String[]{"Main Menu", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        dispose();

        if (choice == 0) {
            if (title.equals("Victory") && level == 1) {
                SwingUtilities.invokeLater(() -> new GameEngine(2).setVisible(true));
            } else {
                SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
            }
        } else if (choice == 1) {
            System.exit(0);
        }
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            player.draw(g);

            // วาดผนังและสิ่งกีดขวางจากแมพ
            g.setColor(Color.DARK_GRAY);
            for (Rectangle wall : map.getWalls()) {
                g.fillRect(wall.x, wall.y, wall.width, wall.height);
            }

            for (Zombie zombie : zombies) {
                zombie.draw(g);
            }

            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }

            // วาดพลังชีวิตของผู้เล่น
            g.setColor(Color.RED);
            g.fillRect(10, 10, health * 2, 10);
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, 200, 10);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameEngine(1).setVisible(true));
    }
}