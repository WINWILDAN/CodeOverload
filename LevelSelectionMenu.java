import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LevelSelectionMenu extends JFrame {
    private Image backgroundImage;

    public LevelSelectionMenu() {
        setTitle("Select Level");
        setSize(600, 600); // Set window size to 600x600
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load and resize the background image
        backgroundImage = new ImageIcon("selecBG.png").getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);

        // Create the custom panel with the resized background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null); // Set layout to null for custom positioning

        // Adjusted button sizes and positions for 600x600 layout
        int levelButtonWidth = 150; // Adjusted width
        int levelButtonHeight = 60;  // Adjusted height
        int backButtonWidth = 80;    // Adjusted width for back button
        int backButtonHeight = 30;   // Adjusted height for back button

        // Load and resize button images
        ImageIcon level1Icon = new ImageIcon(new ImageIcon("level1.png").getImage().getScaledInstance(levelButtonWidth, levelButtonHeight, Image.SCALE_SMOOTH));
        ImageIcon level2Icon = new ImageIcon(new ImageIcon("level2.png").getImage().getScaledInstance(levelButtonWidth, levelButtonHeight, Image.SCALE_SMOOTH));
        ImageIcon backIcon = new ImageIcon(new ImageIcon("back.png").getImage().getScaledInstance(backButtonWidth, backButtonHeight, Image.SCALE_SMOOTH));

        // Button for Level 1
        JButton level1Button = new JButton(level1Icon);
        level1Button.setBounds(225, 180, levelButtonWidth, levelButtonHeight); // Positioned for smaller screen
        level1Button.setContentAreaFilled(false);
        level1Button.setBorderPainted(false);
        level1Button.setFocusPainted(false); // Prevent blue focus outline
        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameEngine(1).setVisible(true); // Start game at level 1
                dispose(); // Close level selection screen
            }
        });

        // Button for Level 2
        JButton level2Button = new JButton(level2Icon);
        level2Button.setBounds(225, 270, levelButtonWidth, levelButtonHeight); // Positioned for smaller screen
        level2Button.setContentAreaFilled(false);
        level2Button.setBorderPainted(false);
        level2Button.setFocusPainted(false); // Prevent blue focus outline
        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameEngine(2).setVisible(true); // Start game at level 2
                dispose(); // Close level selection screen
            }
        });

        // Back button positioned in the top right
        JButton backButton = new JButton(backIcon);
        backButton.setBounds(500, 20, backButtonWidth, backButtonHeight); // Positioned for smaller screen
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false); // Prevent blue focus outline
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainMenu().setVisible(true); // Go back to main menu
                dispose();
            }
        });

        // Add buttons to the background panel
        backgroundPanel.add(level1Button);
        backgroundPanel.add(level2Button);
        backgroundPanel.add(backButton);

        // Add background panel to the frame
        setContentPane(backgroundPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelSelectionMenu().setVisible(true));
    }
}