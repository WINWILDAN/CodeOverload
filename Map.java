import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Map {
    private ArrayList<Rectangle> walls;
    private Image brickImage;

    public Map(int level) {
        walls = new ArrayList<>();
        ImageIcon brickIcon = new ImageIcon("brick.jpg"); 
        brickImage = brickIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        if (level == 1) {
            createLevel1Map();  // ใช้แมพง่ายสำหรับเลเวล 1
        } else if (level == 2) {
            createLevel2Map();  // ใช้แมพที่ซับซ้อนสำหรับเลเวล 2
        }
    }

    private void createLevel1Map() {
        // แมพสำหรับเลเวล 1 (เหมือนโค้ดเดิมของคุณ)
        walls.add(new Rectangle(0, 0, 1200, 40));      // ขอบบน
        walls.add(new Rectangle(0, 0, 40, 1200));      // ขอบซ้าย
        walls.add(new Rectangle(1160, 0, 40, 1200));   // ขอบขวา
        walls.add(new Rectangle(0, 1160, 1200, 40));   // ขอบล่าง

        // กำแพงภายใน
        walls.add(new Rectangle(80, 80, 200, 40));
        walls.add(new Rectangle(80, 160, 40, 240));
        walls.add(new Rectangle(200, 160, 200, 40));
        walls.add(new Rectangle(320, 240, 40, 200));
        walls.add(new Rectangle(80, 480, 200, 40));
        walls.add(new Rectangle(80, 560, 40, 320));
        walls.add(new Rectangle(200, 640, 240, 40));
        walls.add(new Rectangle(320, 720, 40, 240));
        walls.add(new Rectangle(560, 80, 40, 880));
        walls.add(new Rectangle(400, 480, 400, 40));
        walls.add(new Rectangle(800, 80, 40, 800));
        walls.add(new Rectangle(880, 480, 240, 40));
        walls.add(new Rectangle(680, 880, 400, 40));
        walls.add(new Rectangle(400, 1040, 400, 40));
    }

    private void createLevel2Map() {
        // แมพสำหรับเลเวล 2 ที่ซับซ้อนและท้าทาย
        int gridSize = 40;  // ขนาดช่องกำแพง
        int width = 1200;
        int height = 1200;

        // สร้างขอบรอบแมพ
        walls.add(new Rectangle(0, 0, width, gridSize));
        walls.add(new Rectangle(0, 0, gridSize, height));
        walls.add(new Rectangle(width - gridSize, 0, gridSize, height));
        walls.add(new Rectangle(0, height - gridSize, width, gridSize));

        // เพิ่มกำแพงซับซ้อนภายใน
        for (int i = 2; i < height / gridSize - 2; i += 2) {
            for (int j = 2; j < width / gridSize - 2; j += 2) {
                if ((i + j) % 4 == 0) {  // เงื่อนไขสร้างกำแพงเป็นรูปแบบ
                    walls.add(new Rectangle(j * gridSize, i * gridSize, gridSize, gridSize));
                }
            }
        }

        // สร้างเส้นทางเดินซับซ้อนเพิ่มเติม
        walls.add(new Rectangle(3 * gridSize, 3 * gridSize, gridSize * 6, gridSize)); // แนวนอนบน
        walls.add(new Rectangle(7 * gridSize, 6 * gridSize, gridSize, gridSize * 5)); // แนวตั้งซ้าย
        walls.add(new Rectangle(10 * gridSize, 9 * gridSize, gridSize * 4, gridSize)); // แนวนอนกลาง
        walls.add(new Rectangle(6 * gridSize, 13 * gridSize, gridSize * 3, gridSize)); // แนวนอนล่าง

        // ทางเดินกลางแมพ
        for (int i = 5; i < height / gridSize - 5; i++) {
            if (i % 2 == 1) {
                walls.add(new Rectangle(5 * gridSize, i * gridSize, gridSize, gridSize));
            }
        }
        for (int i = 6; i < width / gridSize - 6; i++) {
            if (i % 3 == 0) {
                walls.add(new Rectangle(i * gridSize, 10 * gridSize, gridSize, gridSize));
            }
        }
    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1200, 1200);

        // วาดกำแพงด้วยอิฐ
        for (Rectangle wall : walls) {
            for (int x = wall.x; x < wall.x + wall.width; x += 40) {
                for (int y = wall.y; y < wall.y + wall.height; y += 40) {
                    g.drawImage(brickImage, x, y, null);
                }
            }
        }
    }
}