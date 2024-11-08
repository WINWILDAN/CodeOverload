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
    private Map map;

    public GameEngine(int level) {
        setTitle("Code Overload: Rise of the Undead - Level " + level);
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.level = level;

        map = new Map();
        zombieImage = new ImageIcon("zombie.png").getImage();
        zombies = new ArrayList<>();
        spawnZombies();

        // กำหนดตำแหน่งผู้เล่นให้อยู่มุมขวาล่าง และตรวจสอบให้ไม่ชนกำแพง
        int playerStartX = getWidth() - 40;
        int playerStartY = getHeight() - 40;
        while (checkCollisionWithWalls(playerStartX, playerStartY)) {
            playerStartX -= 20;
            playerStartY -= 20;
        }

        // โหลดภาพเฟรมสำหรับผู้เล่น
        Image[] walkingFrames = {
            new ImageIcon("walk1.png").getImage().getScaledInstance(32, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk2.png").getImage().getScaledInstance(32, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk3.png").getImage().getScaledInstance(32, 40, Image.SCALE_SMOOTH),
            new ImageIcon("walk4.png").getImage().getScaledInstance(32, 40, Image.SCALE_SMOOTH)
        };

        player = new Player(100, 120, walkingFrames);
        bullets = new ArrayList<>();
        mousePosition = new Point(0, 0);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        // ภายใน GameEngine
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
                    player.move(dx, dy); // เคลื่อนที่ผู้เล่นเมื่อไม่ชนกำแพง
                    repaint();
                }
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
            int zombieX, zombieY;
            do {
                zombieX = (int) (Math.random() * (getWidth() - 40));
                zombieY = (int) (Math.random() * (getHeight() - 40));
            } while (checkCollisionWithWalls(zombieX, zombieY));

            // Pass zombieImage when creating a new Zombie instance
            zombies.add(new Zombie(zombieX, zombieY, zombieHealth, zombieSpeed, zombieImage));
        }
    }

    private void shootBullet(Point target) {
        // ขนาดภาพผู้เล่น
        int playerWidth = 32; // หรือกำหนดให้ตรงกับขนาดของภาพ
        int playerHeight = 40; // หรือกำหนดให้ตรงกับขนาดของภาพ
    
        // คำนวณจุดศูนย์กลางของผู้เล่น
        int playerCenterX = player.getX() + playerWidth / 2;
        int playerCenterY = player.getY() + playerHeight / 2;
    
        // คำนวณทิศทางของกระสุน
        int dx = target.x - playerCenterX;
        int dy = target.y - playerCenterY;
        double length = Math.sqrt(dx * dx + dy * dy);
        dx = (int) (dx / length * 5); // ปรับความเร็ว
        dy = (int) (dy / length * 5);
    
        // สร้างกระสุนที่จุดศูนย์กลางของผู้เล่น
        Bullet bullet = new Bullet(playerCenterX, playerCenterY, dx, dy);
        bullets.add(bullet);
    }

    private void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.move();
            
            // ตรวจสอบว่ากระสุนชนกับขอบหน้าต่าง
            if (bullet.getX() < 0 || bullet.getX() > getWidth() || bullet.getY() < 0 || bullet.getY() > getHeight()) {
                it.remove();
                continue;
            }
    
            // ตรวจสอบว่ากระสุนชนกับกำแพงหรือไม่
            Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), 5, 5); // กำหนดขนาดของกระสุน
            boolean hitWall = false;
            for (Rectangle wall : map.getWalls()) {
                if (bulletBounds.intersects(wall)) {
                    hitWall = true;
                    break;
                }
            }
    
            // ลบกระสุนออกจากลิสต์ถ้าชนกับกำแพง
            if (hitWall) {
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

            // วาดพื้นหลังเป็นสีเทาอ่อน
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // วาดกำแพงในแมพ
        map.draw(g);
        // วาดผู้เล่น
        player.draw(g);

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