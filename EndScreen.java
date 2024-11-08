import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class EndScreen extends JFrame {
    public EndScreen(boolean isVictory, int level, int score) {
        setTitle(isVictory ? "Victory" : "Game Over");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel(isVictory ? "You WIN" : "Game Over");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        gbc.gridy++;
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, gbc);

        gbc.gridy++;
        if (isVictory && level == 1) {
            add(createButton("Next Level", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new GameEngine(2).setVisible(true);
                }
            }), gbc);

            gbc.gridy++;
            add(createLabel("or"), gbc);
            gbc.gridy++;
        }

        add(createButton("Main Menu", e -> {
            dispose();
            new MainMenu().setVisible(true);
        }), gbc);

        if (!isVictory || level == 2) {
            gbc.gridy++;
            add(createLabel("or"), gbc);
            gbc.gridy++;
            add(createButton("Play Again", e -> {
                dispose();
                new GameEngine(level).setVisible(true);
            }), gbc);
        }

        setVisible(true);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        return label;
    }
}