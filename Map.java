import java.awt.*;
import java.util.ArrayList;

public class Map {
    private ArrayList<Rectangle> walls;
    private ArrayList<Point> zombieSpawns;

    public Map() {
        walls = new ArrayList<>();
        zombieSpawns = new ArrayList<>();
        createMap();
    }

    private void createMap() {
        // สร้างผนังรอบขอบของแมพ
        walls.add(new Rectangle(0, 0, 1200, 20)); // ด้านบน
        walls.add(new Rectangle(0, 0, 20, 1200)); // ด้านซ้าย
        walls.add(new Rectangle(1180, 0, 20, 1200)); // ด้านขวา
        walls.add(new Rectangle(0, 1180, 1200, 20)); // ด้านล่าง

        // สร้างผนังและสิ่งกีดขวางภายในแมพ
        walls.add(new Rectangle(300, 300, 600, 20)); // ผนังกั้นแนวนอน
        walls.add(new Rectangle(300, 500, 20, 400)); // ผนังกั้นแนวตั้ง
        walls.add(new Rectangle(800, 500, 20, 400)); // ผนังกั้นแนวตั้งอีกด้าน

        // เพิ่มจุดเกิดของซอมบี้
        zombieSpawns.add(new Point(1000, 1000));
        zombieSpawns.add(new Point(200, 800));
        zombieSpawns.add(new Point(800, 200));
    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public ArrayList<Point> getZombieSpawns() {
        return zombieSpawns;
    }
}