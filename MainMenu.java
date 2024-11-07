import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainMenu extends JFrame {
    private JButton startButton;
    private JButton exitButton;

    public MainMenu() {
        setTitle("Code Overload: Rise of the Undead - Main Menu");
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ใช้ JLayeredPane เพื่อจัดการเลเยอร์ของพื้นหลังและปุ่ม
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 1200));

        // โหลดและปรับขนาดภาพพื้นหลังให้ตรงกับหน้าต่าง
        ImageIcon backgroundIcon = new ImageIcon("start.png");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(1200, 1200, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(backgroundImage));
        background.setBounds(0, 0, 1200, 1200);
        layeredPane.add(background, Integer.valueOf(0)); // เพิ่มภาพพื้นหลังในเลเยอร์ล่างสุด

        // ปุ่ม Start
        startButton = new JButton(new ImageIcon("start_button.png"));
        startButton.setBounds(460, 900, 280, 80); // ตำแหน่งให้ปุ่มอยู่ตรงกลางและใกล้ด้านล่าง
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLevelSelection();
            }
        });

        // ปุ่ม Exit อยู่มุมขวาบน
        exitButton = new JButton("Exit");
        exitButton.setBounds(1050, 20, 100, 50); // กำหนดตำแหน่งให้ปุ่ม Exit อยู่มุมขวาบน
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(new LineBorder(Color.WHITE, 2)); // กำหนดกรอบสีขาวให้ปุ่ม Exit
        exitButton.setForeground(Color.WHITE); // ตั้งค่าสีตัวอักษรของปุ่มเป็นสีขาว
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // เพิ่มปุ่มในเลเยอร์บนสุด
        layeredPane.add(startButton, Integer.valueOf(1));
        layeredPane.add(exitButton, Integer.valueOf(1));

        // เพิ่ม layeredPane ลงใน JFrame
        add(layeredPane);
        pack(); // ปรับขนาดหน้าต่างให้พอดีกับเนื้อหาทั้งหมด
    }

    private void openLevelSelection() {
        LevelSelectionMenu levelSelection = new LevelSelectionMenu();
        levelSelection.setVisible(true);
        dispose(); // ปิดหน้าจอเมนูหลัก
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}