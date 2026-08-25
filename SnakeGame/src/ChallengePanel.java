import javax.swing.*;
import java.awt.*;

public class ChallengePanel extends JPanel {

    Image background;
    JButton start;

    public ChallengePanel() {

        setLayout(null);

        // Background
        background = new ImageIcon(
            getClass().getResource("/images/MissionBackground.png")
        ).getImage();

        // Output
        JLabel output = new JLabel(
            "<html><center>Output:<br>Hello World</center></html>"
        );

        output.setBounds(500, 400, 400, 200);
        output.setFont(new Font("Consolas", Font.BOLD, 50));
        output.setForeground(Color.WHITE);

        add(output);

        // Start Button
        ImageIcon startIcon = new ImageIcon(
            getClass().getResource("/images/startButton.png")
        );

        Image img = startIcon.getImage();

        Image resized = img.getScaledInstance(
            350,
            250,
            Image.SCALE_SMOOTH
        );

        startIcon = new ImageIcon(resized);

        start = new JButton(startIcon);

        start.setBorderPainted(false);
        start.setContentAreaFilled(false);
        start.setFocusPainted(false);
        start.setOpaque(false);

        // Start button action
        start.addActionListener(e -> {

            JFrame frame =
                (JFrame) SwingUtilities.getWindowAncestor(this);

            frame.dispose();

            try {
                new GameFrame();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        add(start);
    }

    // Automatically center the Start button
    @Override
    public void doLayout() {

        super.doLayout();

        int buttonWidth = 350;
        int buttonHeight = 150;

        start.setBounds(
            (getWidth() - buttonWidth) / 2,
            (getHeight() - buttonHeight) / 2 + 250,
            buttonWidth,
            buttonHeight
        );
    }

    // Background
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
            background,
            0,
            0,
            getWidth(),
            getHeight(),
            this
        );
    }
}