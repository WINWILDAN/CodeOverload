import java.awt.*;
import javax.swing.*;

public class LevelSelectionMenu extends JFrame {
    private Image backgroundImage;

    public LevelSelectionMenu() {
        setTitle("Select Level");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("selecBG.png").getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        int levelButtonWidth = 150;
        int levelButtonHeight = 60;
        int backButtonWidth = 80;
        int backButtonHeight = 30;

        ImageIcon level1Icon = new ImageIcon(new ImageIcon("level1.png").getImage().getScaledInstance(levelButtonWidth, levelButtonHeight, Image.SCALE_SMOOTH));
        ImageIcon level2Icon = new ImageIcon(new ImageIcon("level2.png").getImage().getScaledInstance(levelButtonWidth, levelButtonHeight, Image.SCALE_SMOOTH));
        ImageIcon backIcon = new ImageIcon(new ImageIcon("back.png").getImage().getScaledInstance(backButtonWidth, backButtonHeight, Image.SCALE_SMOOTH));

        JButton level1Button = new JButton(level1Icon);
        level1Button.setBounds(225, 180, levelButtonWidth, levelButtonHeight);
        level1Button.setContentAreaFilled(false);
        level1Button.setBorderPainted(false);
        level1Button.setFocusPainted(false);
        level1Button.addActionListener(e -> {
            new GameEngine(1).setVisible(true);
            dispose();
        });

        JButton level2Button = new JButton(level2Icon);
        level2Button.setBounds(225, 270, levelButtonWidth, levelButtonHeight);
        level2Button.setContentAreaFilled(false);
        level2Button.setBorderPainted(false);
        level2Button.setFocusPainted(false);
        level2Button.addActionListener(e -> {
            new GameEngine(2).setVisible(true);
            dispose();
        });

        JButton backButton = new JButton(backIcon);
        backButton.setBounds(500, 20, backButtonWidth, backButtonHeight);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        backgroundPanel.add(level1Button);
        backgroundPanel.add(level2Button);
        backgroundPanel.add(backButton);

        setContentPane(backgroundPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelSelectionMenu().setVisible(true));
    }
}