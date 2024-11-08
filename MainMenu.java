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
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ใช้ JLayeredPane เพื่อจัดการเลเยอร์ของพื้นหลังและปุ่ม
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 600));

        // โหลดและปรับขนาดภาพพื้นหลังให้ตรงกับหน้าต่าง
        ImageIcon backgroundIcon = new ImageIcon("start.png");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(backgroundImage));
        background.setBounds(0, 0, 600, 600);
        layeredPane.add(background, Integer.valueOf(0)); // เพิ่มภาพพื้นหลังในเลเยอร์ล่างสุด

        // ปุ่ม Start
        startButton = new JButton(new ImageIcon("start_button.png"));
        startButton.setBounds(160, 420, 280, 80); // ปรับตำแหน่งและขนาดให้เหมาะสมกับหน้าต่างใหม่
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
        exitButton.setBounds(480, 20, 80, 40); // ปรับขนาดและตำแหน่งให้พอดีกับหน้าต่าง 600x600
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
        levelSelection.setSize(600, 600); // กำหนดขนาดหน้าจอเลือกเลเวลเป็น 600x600
        levelSelection.setVisible(true);
        dispose(); // ปิดหน้าจอเมนูหลัก
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}