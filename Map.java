import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Map {
    private ArrayList<Rectangle> walls;
    private Image brickImage;

    public Map() {
        walls = new ArrayList<>();
        
        // โหลดรูปภาพอิฐและปรับขนาดให้เหมาะสม
        ImageIcon brickIcon = new ImageIcon("brick.jpg"); // ใช้รูปภาพอิฐ (เช่น brick.png)
        brickImage = brickIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        // กำแพงขอบรอบด้าน
        walls.add(new Rectangle(0, 0, 1200, 40));      // ขอบบน
        walls.add(new Rectangle(0, 0, 40, 1200));      // ขอบซ้าย
        walls.add(new Rectangle(1160, 0, 40, 1200));   // ขอบขวา
        walls.add(new Rectangle(0, 1160, 1200, 40));   // ขอบล่าง

        // กำแพงภายในตามรูป
        walls.add(new Rectangle(80, 80, 200, 40));    // กำแพงมุมบนซ้าย
        walls.add(new Rectangle(80, 160, 40, 240));   // กำแพงแนวตั้งซ้าย
        walls.add(new Rectangle(200, 160, 200, 40));  // กำแพงแนวนอนบนซ้าย
        walls.add(new Rectangle(320, 240, 40, 200));  // กำแพงแนวตั้งกลางบน
        walls.add(new Rectangle(80, 480, 200, 40));   // กำแพงแนวนอนล่างซ้าย
        walls.add(new Rectangle(80, 560, 40, 320));   // กำแพงแนวตั้งซ้ายล่าง
        walls.add(new Rectangle(200, 640, 240, 40));  // กำแพงแนวนอนกลางซ้าย
        walls.add(new Rectangle(320, 720, 40, 240));  // กำแพงแนวตั้งกลางล่าง
        walls.add(new Rectangle(560, 80, 40, 880));   // กำแพงกลางแนวตั้งยาว
        walls.add(new Rectangle(400, 480, 400, 40));  // กำแพงกลางแนวนอน
        walls.add(new Rectangle(800, 80, 40, 800));   // กำแพงขวาบน
        walls.add(new Rectangle(880, 480, 240, 40));  // กำแพงขวากลาง
        walls.add(new Rectangle(680, 880, 400, 40));  // กำแพงขวาล่าง
        walls.add(new Rectangle(400, 1040, 400, 40)); // กำแพงล่างกลาง

        // คุณสามารถเพิ่ม Rectangle ตามลักษณะของภาพเพิ่มเติมได้ หากต้องการกำแพงมากกว่านี้

    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public void draw(Graphics g) {
        // วาดพื้นหลังสีเทาอ่อน
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1200, 1200);

        // วาดกำแพงเป็นรูปอิฐ
        for (Rectangle wall : walls) {
            for (int x = wall.x; x < wall.x + wall.width; x += 40) {
                for (int y = wall.y; y < wall.y + wall.height; y += 40) {
                    g.drawImage(brickImage, x, y, null);  // วาดอิฐที่ตำแหน่งกำแพง
                }
            }
        }
    }
}