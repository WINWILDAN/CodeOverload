import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EndScreen extends JFrame {
    public EndScreen(boolean isVictory, int level, int score) {
        setTitle(isVictory ? "Victory" : "Game Over");
        setSize(600, 600); // กำหนดขนาดหน้าต่างเป็น 600x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // ทำให้หน้าต่างอยู่กึ่งกลางจอ
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // ลดระยะห่างให้น้อยลง
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Title Label
        JLabel titleLabel = new JLabel(isVictory ? "You WIN" : "Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48)); // ลดขนาดฟอนต์ให้เหมาะสมกับขนาดหน้าต่าง
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        // Score Label
        gbc.gridy++;
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // ลดขนาดฟอนต์ให้เหมาะสม
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, gbc);

        // Buttons
        gbc.gridy++;
        if (isVictory && level == 1) {
            JButton nextLevelButton = createButton("Next Level", e -> {
                dispose();
                new GameEngine(2).setVisible(true); // เปิดเลเวลถัดไป
            });
            add(nextLevelButton, gbc);

            gbc.gridy++;
            JLabel orLabel = new JLabel("or");
            orLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // ลดขนาดฟอนต์
            orLabel.setForeground(Color.WHITE);
            add(orLabel, gbc);

            gbc.gridy++;
        }

        JButton mainMenuButton = createButton("Main Menu", e -> {
            dispose();
            new MainMenu().setVisible(true); // กลับไปหน้าหลัก
        });
        add(mainMenuButton, gbc);

        // เพิ่มปุ่ม "Play Again" หากเป็น Game Over หรือ Victory ที่เลเวล 2
        if (!isVictory || level == 2) {
            gbc.gridy++;
            JLabel orLabel = new JLabel("or");
            orLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // ลดขนาดฟอนต์
            orLabel.setForeground(Color.WHITE);
            add(orLabel, gbc);

            gbc.gridy++;
            JButton playAgainButton = createButton("Play Again", e -> {
                dispose();
                new GameEngine(level).setVisible(true); // เล่นเลเวลเดิมอีกครั้ง
            });
            add(playAgainButton, gbc);
        }

        setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40)); // ลดขนาดปุ่มให้เหมาะสม
        button.setFont(new Font("Arial", Font.PLAIN, 18)); // ลดขนาดฟอนต์ให้เหมาะสม
        button.setFocusPainted(false); // ลบเส้นกรอบสีน้ำเงิน
        button.addActionListener(action);
        return button;
    }
}