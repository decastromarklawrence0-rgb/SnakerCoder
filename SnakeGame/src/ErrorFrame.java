import java.awt.*;
import javax.swing.*;

public class ErrorFrame extends JFrame {

    private JLabel errorLabel;

    private ImageIcon error1;
    private ImageIcon error2;

    private JButton restartButton;

    private boolean showingFirst = true;

    private Timer blinkTimer;


    public ErrorFrame() {

        // =========================
        // FULL SCREEN
        // =========================

        setTitle("ERROR");

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setUndecorated(true);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );


        // =========================
        // LOAD ERROR IMAGES
        // =========================

        error1 = new ImageIcon(
                getClass().getResource(
                        "/images/error.png"
                )
        );

        error2 = new ImageIcon(
                getClass().getResource(
                        "/images/error2.png"
                )
        );


        // =========================
        // MAIN PANEL
        // =========================

        JPanel panel = new JPanel(
                new BorderLayout()
        );

        panel.setBackground(Color.BLACK);


        // =========================
        // ERROR IMAGE
        // =========================

        errorLabel = new JLabel(error1);

        errorLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        errorLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );


        panel.add(
                errorLabel,
                BorderLayout.CENTER
        );


        // =========================
        // RESTART BUTTON
        // =========================

        ImageIcon restartIcon =
                new ImageIcon(
                        getClass().getResource(
                                "/images/restartButton.png"
                        )
                );


        Image restartImage =
                restartIcon.getImage()
                        .getScaledInstance(
                                250,
                                100,
                                Image.SCALE_SMOOTH
                        );


        restartIcon =
                new ImageIcon(
                        restartImage
                );


        restartButton =
                new JButton(restartIcon);


        restartButton.setBorderPainted(false);

        restartButton.setContentAreaFilled(false);

        restartButton.setFocusPainted(false);

        restartButton.setOpaque(false);


        restartButton.addActionListener(e -> {

            if (blinkTimer != null) {
                blinkTimer.stop();
            }

            dispose();

            new GameFrame();

        });


        // =========================
        // BUTTON PANEL
        // =========================

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.setBackground(
                Color.BLACK
        );

        buttonPanel.add(
                restartButton
        );


        panel.add(
                buttonPanel,
                BorderLayout.SOUTH
        );


        // =========================
        // ADD PANEL
        // =========================

        setContentPane(panel);


        // =========================
        // BLINK ERROR IMAGE
        // =========================

        blinkTimer = new Timer(
                300,
                e -> {

                    if (showingFirst) {

                        errorLabel.setIcon(
                                error2
                        );

                        showingFirst = false;

                    } else {

                        errorLabel.setIcon(
                                error1
                        );

                        showingFirst = true;
                    }
                }
        );


        blinkTimer.start();


        // =========================
        // SHOW FULL SCREEN
        // =========================

        setVisible(true);
    }
}