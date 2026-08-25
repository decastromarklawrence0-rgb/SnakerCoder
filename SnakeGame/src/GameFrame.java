import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    // =========================
    // ERROR OVERLAY
    // =========================

    JPanel errorOverlay;

    // =========================
    // SUCCESS OVERLAY
    // =========================

    JPanel successOverlay;

    // =========================
    // SUCCESS NEON GLOW
    // =========================

    private Image glow = new ImageIcon(
            getClass().getResource("/images/glow.png")
    ).getImage();

    private float successGlowAlpha = 0.0f;
    private boolean successGlowIncreasing = true;

    private Timer successGlowTimer;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public GameFrame() {

        // =========================
        // GAME PANEL
        // =========================

        panel = new GamePanel(this);

        // =========================
        // MAIN BACKGROUND
        // =========================

        BackgroundPanel mainPanel =
                new BackgroundPanel();

        mainPanel.setLayout(
                new BorderLayout()
        );

        // =========================
        // CENTER GAME HOLDER
        // =========================

        JPanel gameHolder =
                new JPanel(
                        new GridBagLayout()
                );

        gameHolder.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.insets =
                new Insets(
                        20,
                        0,
                        20,
                        0
                );

        gameHolder.add(
                panel,
                gbc
        );

        // =========================
        // OUTPUT PANEL
        // =========================

        OutputBackgroundPanel outputPanel =
                new OutputBackgroundPanel();

        outputPanel.setPreferredSize(
                new Dimension(
                        0,
                        180
                )
        );

        // =========================
        // ADD PANELS
        // =========================

        mainPanel.add(
                gameHolder,
                BorderLayout.CENTER
        );

        mainPanel.add(
                outputPanel,
                BorderLayout.SOUTH
        );

        // =========================
        // FRAME SETTINGS
        // =========================

        setContentPane(mainPanel);

        setTitle("Snake Game");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        // =========================
        // FULLSCREEN
        // =========================

        setUndecorated(true);

        setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );

        // =========================
        // CREATE ERROR OVERLAY
        // =========================

        createErrorOverlay();

        // =========================
        // CREATE SUCCESS OVERLAY
        // =========================

        createSuccessOverlay();

        // =========================
        // SHOW FRAME
        // =========================

        setVisible(true);

        // =========================
        // HIDE OVERLAYS
        // =========================

        errorOverlay.setVisible(false);
        successOverlay.setVisible(false);

        // =========================
        // KEYBOARD FOCUS
        // =========================

        SwingUtilities.invokeLater(() -> {
            panel.requestFocusInWindow();
        });
    }

    // =====================================================
    // CREATE FULLSCREEN ERROR OVERLAY
    // =====================================================

    private void createErrorOverlay() {

        errorOverlay =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        // =========================
                        // CURRENT ERROR IMAGE
                        // =========================

                        Image currentError;

                        if (panel.showErrorImage1) {

                            currentError =
                                    panel.errorImage1;

                        } else {

                            currentError =
                                    panel.errorImage2;
                        }

                        // =========================
                        // FULLSCREEN ERROR IMAGE
                        // =========================

                        g2.drawImage(
                                currentError,
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                this
                        );

                        // =========================
                        // RESTART BUTTON
                        // =========================

                        int buttonWidth = 300;
                        int buttonHeight = 120;

                        int buttonX =
                                (
                                        getWidth()
                                        - buttonWidth
                                ) / 2;

                        int buttonY =
                                getHeight()
                                - buttonHeight
                                - 300;

                        g2.drawImage(
                                panel.restartButtonImage,
                                buttonX,
                                buttonY,
                                buttonWidth,
                                buttonHeight,
                                this
                        );

                        g2.dispose();
                    }
                };

        // =========================
        // OVERLAY SETTINGS
        // =========================

        errorOverlay.setLayout(null);
        errorOverlay.setOpaque(true);

        // =========================
        // RESTART CLICK
        // =========================

        errorOverlay.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        int buttonWidth = 300;
                        int buttonHeight = 120;

                        int buttonX =
                                (
                                        errorOverlay.getWidth()
                                        - buttonWidth
                                ) / 2;

                        int buttonY =
                                errorOverlay.getHeight()
                                - buttonHeight
                                - 300;

                        // =========================
                        // CHECK BUTTON
                        // =========================

                        if (
                                e.getX() >= buttonX &&
                                e.getX() <=
                                        buttonX + buttonWidth &&
                                e.getY() >= buttonY &&
                                e.getY() <=
                                        buttonY + buttonHeight
                        ) {

                            hideErrorScreen();

                            panel.restartGame();
                        }
                    }
                }
        );

        // =========================
        // ADD ABOVE EVERYTHING
        // =========================

        getLayeredPane().add(
                errorOverlay,
                JLayeredPane.POPUP_LAYER
        );

        // =========================
        // INITIAL SIZE
        // =========================

        errorOverlay.setBounds(
                0,
                0,
                getLayeredPane().getWidth(),
                getLayeredPane().getHeight()
        );
    }

    // =====================================================
    // SHOW ERROR SCREEN
    // =====================================================

    public void showErrorScreen() {

        // Hide success first

        if (successOverlay != null) {
            successOverlay.setVisible(false);
        }

        // Stop success glow

        stopSuccessGlow();

        errorOverlay.setBounds(
                0,
                0,
                getLayeredPane().getWidth(),
                getLayeredPane().getHeight()
        );

        errorOverlay.setVisible(true);

        errorOverlay.repaint();

        errorOverlay.revalidate();
    }

    // =====================================================
    // REFRESH ERROR SCREEN
    // =====================================================

    public void refreshErrorScreen() {

        if (errorOverlay != null) {
            errorOverlay.repaint();
        }
    }

    // =====================================================
    // HIDE ERROR SCREEN
    // =====================================================

    public void hideErrorScreen() {

        if (errorOverlay != null) {
            errorOverlay.setVisible(false);
        }

        panel.requestFocusInWindow();
    }

    // =====================================================
    // CREATE FULLSCREEN SUCCESS OVERLAY
    // =====================================================

    private void createSuccessOverlay() {

        successOverlay =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        // =========================
                        // CURRENT SUCCESS IMAGE
                        // =========================

                        Image currentSuccess;

                        if (panel.showSuccessImage1) {

                            currentSuccess =
                                    panel.successImage1;

                        } else {

                            currentSuccess =
                                    panel.successImage2;
                        }

                        // =========================
                        // SUCCESS IMAGE
                        // =========================

                        g2.drawImage(
                                currentSuccess,
                                0,
                                0,
                                getWidth(),
                                getHeight(),
                                this
                        );

                        // =================================================
                        // NEON GLOW
                        // =================================================

                        if (glow != null) {

                            Composite oldComposite =
                                    g2.getComposite();

                            // Glow transparency

                            g2.setComposite(
                                    AlphaComposite.getInstance(
                                            AlphaComposite.SRC_OVER,
                                            successGlowAlpha
                                    )
                            );

                            // =========================
                            // GLOW SIZE
                            // =========================

                            int glowWidth =
                                    getWidth();

                            int glowHeight =
                                    getHeight();

                            // =========================
                            // DRAW GLOW
                            // =========================

                            g2.drawImage(
                                    glow,
                                    0,
                                    0,
                                    glowWidth,
                                    glowHeight,
                                    this
                            );

                            // Restore

                            g2.setComposite(
                                    oldComposite
                            );
                        }

                        g2.dispose();
                    }
                };

        // =========================
        // OVERLAY SETTINGS
        // =========================

        successOverlay.setLayout(null);

        successOverlay.setOpaque(true);

        // =========================
        // CLICK SUCCESS SCREEN
        // =========================

        successOverlay.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e
                    ) {

                        panel.restartGame();
                    }
                }
        );

        // =========================
        // ADD ABOVE EVERYTHING
        // =========================

        getLayeredPane().add(
                successOverlay,
                JLayeredPane.POPUP_LAYER
        );

        // =========================
        // INITIAL SIZE
        // =========================

        successOverlay.setBounds(
                0,
                0,
                getLayeredPane().getWidth(),
                getLayeredPane().getHeight()
        );
    }

    // =====================================================
    // SHOW SUCCESS SCREEN
    // =====================================================

    public void showSuccessScreen() {

        // =========================
        // HIDE ERROR
        // =========================

        if (errorOverlay != null) {
            errorOverlay.setVisible(false);
        }

        // =========================
        // RESET GLOW
        // =========================

        successGlowAlpha = 0.0f;

        successGlowIncreasing = true;

        // =========================
        // START GLOW ANIMATION
        // =========================

        startSuccessGlow();

        // =========================
        // SHOW SUCCESS
        // =========================

        successOverlay.setBounds(
                0,
                0,
                getLayeredPane().getWidth(),
                getLayeredPane().getHeight()
        );

        successOverlay.setVisible(true);

        successOverlay.repaint();

        successOverlay.revalidate();
    }

    // =====================================================
    // START SUCCESS GLOW
    // =====================================================

    private void startSuccessGlow() {

        // Stop old timer

        if (successGlowTimer != null) {

            successGlowTimer.stop();
        }

        // =========================
        // CREATE GLOW TIMER
        // =========================

        successGlowTimer =
                new Timer(
                        20,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(
                                    ActionEvent e
                            ) {

                                // =========================
                                // INCREASING
                                // =========================

                                if (successGlowIncreasing) {

                                    successGlowAlpha += 0.03f;

                                    if (
                                            successGlowAlpha >= 1.0f
                                    ) {

                                        successGlowAlpha = 1.0f;

                                        successGlowIncreasing = false;
                                    }

                                }

                                // =========================
                                // DECREASING
                                // =========================

                                else {

                                    successGlowAlpha -= 0.03f;

                                    if (
                                            successGlowAlpha <= 0.0f
                                    ) {

                                        successGlowAlpha = 0.0f;

                                        successGlowIncreasing = true;
                                    }
                                }

                                // =========================
                                // REFRESH
                                // =========================

                                if (
                                        successOverlay != null &&
                                        successOverlay.isVisible()
                                ) {

                                    successOverlay.repaint();
                                }
                            }
                        }
                );

        successGlowTimer.start();
    }

    // =====================================================
    // STOP SUCCESS GLOW
    // =====================================================

    private void stopSuccessGlow() {

        if (successGlowTimer != null) {

            successGlowTimer.stop();

            successGlowTimer = null;
        }

        successGlowAlpha = 0.0f;

        successGlowIncreasing = true;
    }

    // =====================================================
    // REFRESH SUCCESS SCREEN
    // =====================================================

    public void refreshSuccessScreen() {

        if (successOverlay != null) {
            successOverlay.repaint();
        }
    }

    // =====================================================
    // HIDE SUCCESS SCREEN
    // =====================================================

    public void hideSuccessScreen() {

        // =========================
        // STOP GLOW
        // =========================

        stopSuccessGlow();

        // =========================
        // HIDE OVERLAY
        // =========================

        if (successOverlay != null) {

            successOverlay.setVisible(false);
        }

        panel.requestFocusInWindow();
    }

    // =====================================================
    // RESIZE OVERLAYS
    // =====================================================

    @Override
    public void doLayout() {

        super.doLayout();

        if (errorOverlay != null) {

            errorOverlay.setBounds(
                    0,
                    0,
                    getLayeredPane().getWidth(),
                    getLayeredPane().getHeight()
            );
        }

        if (successOverlay != null) {

            successOverlay.setBounds(
                    0,
                    0,
                    getLayeredPane().getWidth(),
                    getLayeredPane().getHeight()
            );
        }
    }
}