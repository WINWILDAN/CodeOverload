import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelectionMenu extends JFrame {
    public LevelSelectionMenu() {
        setTitle("Select Level");
        setSize(1200, 1200); // ตั้งขนาดหน้าต่างเป็น 1200x1200
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // ใช้ Layout แบบกำหนดตำแหน่งเอง

        // ปุ่มสำหรับเลือกเลเวล 1
        JButton level1Button = new JButton("LEVEL 1");
        level1Button.setFont(new Font("Arial", Font.PLAIN, 24));
        level1Button.setBounds(500, 400, 200, 80); // กำหนดตำแหน่งและขนาดปุ่ม
        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameEngine(1).setVisible(true); // เริ่มเกมที่เลเวล 1
                dispose(); // ปิดหน้าจอเลือกเลเวล
            }
        });

        // ปุ่มสำหรับเลือกเลเวล 2
        JButton level2Button = new JButton("LEVEL 2");
        level2Button.setFont(new Font("Arial", Font.PLAIN, 24));
        level2Button.setBounds(500, 500, 200, 80); // กำหนดตำแหน่งและขนาดปุ่ม
        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameEngine(2).setVisible(true); // เริ่มเกมที่เลเวล 2
                dispose(); // ปิดหน้าจอเลือกเลเวล
            }
        });

        // ปุ่ม Back ในมุมขวาบน
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 18));
        backButton.setBounds(1100, 20, 80, 40); // กำหนดตำแหน่งและขนาดปุ่ม
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu().setVisible(true); // กลับไปยังหน้าหลัก
                dispose();
            }
        });

        // เพิ่มปุ่มในหน้าต่าง
        add(level1Button);
        add(level2Button);
        add(backButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelSelectionMenu().setVisible(true));
    }
}