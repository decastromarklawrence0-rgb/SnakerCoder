import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private Image background = new ImageIcon(
            getClass().getResource("/images/menuBackground.png")
    ).getImage();

    private Image glow = new ImageIcon(
            getClass().getResource("/images/glow.png")
    ).getImage();

    private JButton play;

    // Glow animation variables
    private float alpha = 0.0f;
    private boolean increasing = true;

    public MenuPanel() {

        setLayout(null);

        // Smooth glow animation
        Timer glowTimer = new Timer(40, e -> {

            if (increasing) {
                alpha += 0.01f;   // Mas maliit = mas mabagal
                if (alpha >= 1f) {
                    alpha = 1f;
                    increasing = false;
                }
            } else {
                alpha -= 0.01f;
                if (alpha <= 0f) {
                    alpha = 0f;
                    increasing = true;
                }
            }

            repaint();
        });

        glowTimer.start();

        // Play button image
        ImageIcon playIcon = new ImageIcon(
                getClass().getResource("/images/playButton.png")
        );

        Image img = playIcon.getImage();
        Image resized = img.getScaledInstance(550, 410, Image.SCALE_SMOOTH);
        playIcon = new ImageIcon(resized);

        // Play button
        play = new JButton(playIcon);

        play.setBorderPainted(false);
        play.setContentAreaFilled(false);
        play.setFocusPainted(false);
        play.setOpaque(false);

        play.addActionListener(e -> {

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();

            new ChallengeScreen();

        });

        add(play);
    }

    @Override
    public void doLayout() {

        super.doLayout();

        int buttonWidth = 550;
        int buttonHeight = 410;

        play.setBounds(
                (getWidth() - buttonWidth) / 2,
                (getHeight() - buttonHeight) / 2 + 100,
                buttonWidth,
                buttonHeight
        );
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw background
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);

        // Draw glowing overlay
        g2.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        alpha
                )
        );

        g2.drawImage(glow, 0, 0, getWidth(), getHeight(), this);

        // Reset transparency
        g2.setComposite(AlphaComposite.SrcOver);
    }
}