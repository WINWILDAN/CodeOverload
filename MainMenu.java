import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Code Overload: Rise of the Undead - Main Menu");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 600));

        ImageIcon backgroundIcon = new ImageIcon("start.png");
        JLabel background = new JLabel(new ImageIcon(backgroundIcon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH)));
        background.setBounds(0, 0, 600, 600);
        layeredPane.add(background, Integer.valueOf(0)); 

        JButton startButton = createButton("start_button.png", 160, 420, 280, 80, e -> openLevelSelection());

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(480, 20, 80, 40);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(new LineBorder(Color.WHITE, 2));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));

        layeredPane.add(startButton, Integer.valueOf(1));
        layeredPane.add(exitButton, Integer.valueOf(1));

        add(layeredPane);
        pack();
    }

    private JButton createButton(String iconPath, int x, int y, int width, int height, ActionListener action) {
        JButton button = new JButton(new ImageIcon(iconPath));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private void openLevelSelection() {
        LevelSelectionMenu levelSelection = new LevelSelectionMenu();
        levelSelection.setSize(600, 600);
        levelSelection.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}