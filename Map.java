import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Map {
    private ArrayList<Rectangle> walls;
    private Image brickImage;

    public Map(int level) {
        walls = new ArrayList<>();
        ImageIcon brickIcon = new ImageIcon("brick.jpg"); 
        brickImage = brickIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH); 

        if (level == 1) {
            createLevel1Map();  
        } else if (level == 2) {
            createLevel2Map();  
        }
    }

    private void createLevel1Map() {
        walls.add(new Rectangle(0, 0, 600, 20));      
        walls.add(new Rectangle(0, 0, 20, 600));      
        walls.add(new Rectangle(580, 0, 20, 600));   
        walls.add(new Rectangle(0, 580, 600, 20));   

        walls.add(new Rectangle(40, 40, 100, 20));
        walls.add(new Rectangle(40, 80, 20, 120));
        walls.add(new Rectangle(100, 80, 100, 20));
        walls.add(new Rectangle(160, 120, 20, 100));
        walls.add(new Rectangle(40, 240, 100, 20));
        walls.add(new Rectangle(40, 280, 20, 160));
        walls.add(new Rectangle(100, 320, 120, 20));
        walls.add(new Rectangle(160, 360, 20, 120));
        walls.add(new Rectangle(280, 40, 20, 440));
        walls.add(new Rectangle(200, 240, 200, 20));
        walls.add(new Rectangle(400, 40, 20, 400));
        walls.add(new Rectangle(440, 240, 120, 20));
        walls.add(new Rectangle(340, 440, 200, 20));
        walls.add(new Rectangle(200, 520, 200, 20));
    }

    private void createLevel2Map() {
        int gridSize = 20; 
        int width = 600;
        int height = 600;

        walls.add(new Rectangle(0, 0, width, gridSize));
        walls.add(new Rectangle(0, 0, gridSize, height));
        walls.add(new Rectangle(width - gridSize, 0, gridSize, height));
        walls.add(new Rectangle(0, height - gridSize, width, gridSize));

        for (int i = 2; i < height / gridSize - 2; i += 2) {
            for (int j = 2; j < width / gridSize - 2; j += 2) {
                if ((i + j) % 4 == 0) {  
                    walls.add(new Rectangle(j * gridSize, i * gridSize, gridSize, gridSize));
                }
            }
        }

        walls.add(new Rectangle(3 * gridSize, 3 * gridSize, gridSize * 3, gridSize)); 
        walls.add(new Rectangle(4 * gridSize, 3 * gridSize, gridSize, gridSize * 3)); 
        walls.add(new Rectangle(5 * gridSize, 5 * gridSize, gridSize * 2, gridSize)); 
        walls.add(new Rectangle(3 * gridSize, 8 * gridSize, gridSize * 2, gridSize)); 

        for (int i = 3; i < height / gridSize - 3; i++) {
            if (i % 2 == 1) {
                walls.add(new Rectangle(2 * gridSize, i * gridSize, gridSize, gridSize));
            }
        }
        for (int i = 3; i < width / gridSize - 3; i++) {
            if (i % 3 == 0) {
                walls.add(new Rectangle(i * gridSize, 5 * gridSize, gridSize, gridSize));
            }
        }
    }

    public ArrayList<Rectangle> getWalls() {
        return walls;
    }

    public void draw(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 600, 600); 

        for (Rectangle wall : walls) {
            for (int x = wall.x; x < wall.x + wall.width; x += 20) {
                for (int y = wall.y; y < wall.y + wall.height; y += 20) {
                    g.drawImage(brickImage, x, y, null);
                }
            }
        }
    }
}