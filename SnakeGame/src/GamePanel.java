import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // =====================================================
    // GAME FRAME
    // =====================================================

    GameFrame gameFrame;

    // =====================================================
    // IMAGES
    // =====================================================

    Image background = new ImageIcon(
            getClass().getResource("/images/gameBackground.png")
    ).getImage();

    Image headUp = new ImageIcon(
            getClass().getResource("/images/HeadUp.png")
    ).getImage();

    Image headDown = new ImageIcon(
            getClass().getResource("/images/HeadDown.png")
    ).getImage();

    Image headLeft = new ImageIcon(
            getClass().getResource("/images/HeadLeft.png")
    ).getImage();

    Image headRight = new ImageIcon(
            getClass().getResource("/images/HeadRight.png")
    ).getImage();

    Image bodyVertical = new ImageIcon(
            getClass().getResource("/images/snakeBodyVertical.png")
    ).getImage();

    Image bodyHorizontal = new ImageIcon(
            getClass().getResource("/images/snakeBodyHorizontal.png")
    ).getImage();

    Image tailUp = new ImageIcon(
            getClass().getResource("/images/snakeTailUp.png")
    ).getImage();

    Image tailDown = new ImageIcon(
            getClass().getResource("/images/snakeTailDown.png")
    ).getImage();

    Image tailLeft = new ImageIcon(
            getClass().getResource("/images/snakeTailLeft.png")
    ).getImage();

    Image tailRight = new ImageIcon(
            getClass().getResource("/images/snakeTailRight.png")
    ).getImage();

    Image bodyVerticalGlow = new ImageIcon(
            getClass().getResource("/images/BodyVerticalGlow.png")
    ).getImage();

    Image bodyHorizontalGlow = new ImageIcon(
            getClass().getResource("/images/BodyHorizontalGlow.png")
    ).getImage();

    Image heartImage = new ImageIcon(
            getClass().getResource("/images/heart.png")
    ).getImage();

    Image heartOutlineImage = new ImageIcon(
            getClass().getResource("/images/heartOutline.png")
    ).getImage();

    // =====================================================
    // NEON GLOW IMAGE
    // =====================================================

    Image glow = new ImageIcon(
            getClass().getResource("/images/glow.png")
    ).getImage();

    // =====================================================
    // ERROR SCREEN IMAGES
    // =====================================================

    Image errorImage1 = new ImageIcon(
            getClass().getResource("/images/error1.png")
    ).getImage();

    Image errorImage2 = new ImageIcon(
            getClass().getResource("/images/error2.png")
    ).getImage();

    Image restartButtonImage = new ImageIcon(
            getClass().getResource("/images/restartButton.png")
    ).getImage();

    // =====================================================
    // SUCCESS SCREEN IMAGES
    // =====================================================

    Image successImage1 = new ImageIcon(
            getClass().getResource("/images/success1.png")
    ).getImage();

    Image successImage2 = new ImageIcon(
            getClass().getResource("/images/success2.png")
    ).getImage();

    boolean showSuccessImage1 = true;
    boolean showSuccessScreen = false;

    // =====================================================
    // SUCCESS NEON GLOW
    // =====================================================

    private float successGlowAlpha = 0.0f;
    private boolean successGlowIncreasing = true;

    Timer successBlinkTimer;

    // =====================================================
    // GAME SETTINGS
    // =====================================================

    int lives = 3;

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 25;

    static final int GAME_UNITS =
            (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    static final int DELAY = 100;

    // =====================================================
    // SNAKE
    // =====================================================

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;

    char direction = 'R';

    // =====================================================
    // GAME STATE
    // =====================================================

    boolean running = false;
    boolean paused = false;
    boolean victory = false;

    // =====================================================
    // ERROR STATE
    // =====================================================

    boolean showErrorScreen = false;
    boolean showErrorImage1 = true;

    // =====================================================
    // TIMERS
    // =====================================================

    Timer timer;
    Timer errorBlinkTimer;

    Random random;

    // =====================================================
    // TWO CODE BALLS
    // =====================================================

    int appleX1;
    int appleY1;

    int appleX2;
    int appleY2;

    // =====================================================
    // CODE SEQUENCE
    // =====================================================

    String[] codeBlocks = {

        "public class HelloWorld {",
        "public static void main(String[] args) {",
        "System.out.println(\"Hello World\");",
        "}",
        "}"

    };

    // =====================================================
    // NEXT REQUIRED CODE
    // =====================================================

    int nextCode = 0;

    // =====================================================
    // CODE STORED IN BALLS
    // =====================================================

    int appleCode1;
    int appleCode2;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    GamePanel(GameFrame gameFrame) {

        this.gameFrame = gameFrame;

        random = new Random();

        setPreferredSize(
                new Dimension(
                        SCREEN_WIDTH,
                        SCREEN_HEIGHT
                )
        );

        setBackground(Color.BLACK);

        setFocusable(true);
        addKeyListener(this);

        setLayout(null);

        startGame();
    }

    // =====================================================
    // START GAME
    // =====================================================

    public void startGame() {

        newApple();

        running = true;
        paused = false;

        showErrorScreen = false;
        showErrorImage1 = true;

        showSuccessScreen = false;
        showSuccessImage1 = true;

        successGlowAlpha = 0.0f;
        successGlowIncreasing = true;

        timer = new Timer(
                DELAY,
                this
        );

        timer.start();

        requestFocusInWindow();
    }

    // =====================================================
    // CREATE TWO CODE BALLS
    // =====================================================

    public void newApple() {

        // FIRST BALL = CORRECT CODE

        appleCode1 = nextCode;

        appleX1 =
                random.nextInt(
                        SCREEN_WIDTH / UNIT_SIZE
                ) * UNIT_SIZE;

        appleY1 =
                random.nextInt(
                        SCREEN_HEIGHT / UNIT_SIZE
                ) * UNIT_SIZE;

        // SECOND BALL = WRONG CODE

        do {

            appleCode2 =
                    random.nextInt(
                            codeBlocks.length
                    );

        } while (
                appleCode2 == nextCode
        );

        // SECOND BALL POSITION

        do {

            appleX2 =
                    random.nextInt(
                            SCREEN_WIDTH / UNIT_SIZE
                    ) * UNIT_SIZE;

            appleY2 =
                    random.nextInt(
                            SCREEN_HEIGHT / UNIT_SIZE
                    ) * UNIT_SIZE;

        } while (
                appleX2 == appleX1 &&
                appleY2 == appleY1
        );
    }

    // =====================================================
    // PAINT COMPONENT
    // =====================================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        draw(g);
    }

    // =====================================================
    // DRAW
    // =====================================================

    public void draw(Graphics g) {

        // BACKGROUND

        g.drawImage(
                background,
                0,
                0,
                getWidth(),
                getHeight(),
                this
        );

        // =================================================
        // RUNNING GAME
        // =================================================

        if (running) {

            // HEARTS

            for (int i = 0; i < 3; i++) {

                if (i < lives) {

                    g.drawImage(
                            heartImage,
                            SCREEN_WIDTH - 100 + (i * 30),
                            10,
                            25,
                            25,
                            this
                    );

                } else {

                    g.drawImage(
                            heartOutlineImage,
                            SCREEN_WIDTH - 100 + (i * 30),
                            10,
                            25,
                            25,
                            this
                    );
                }
            }

            // =================================================
            // CODE BALLS
            // =================================================

            Graphics2D g2 =
                    (Graphics2D) g.create();

            // FIRST BALL OUTER GLOW

            g2.setColor(
                    new Color(
                            0,
                            180,
                            255,
                            40
                    )
            );

            g2.fillOval(
                    appleX1 - 8,
                    appleY1 - 8,
                    UNIT_SIZE + 16,
                    UNIT_SIZE + 16
            );

            // FIRST BALL INNER GLOW

            g2.setColor(
                    new Color(
                            0,
                            200,
                            255,
                            70
                    )
            );

            g2.fillOval(
                    appleX1 - 5,
                    appleY1 - 5,
                    UNIT_SIZE + 10,
                    UNIT_SIZE + 10
            );

            // FIRST BALL

            g2.setColor(
                    new Color(
                            80,
                            220,
                            255
                    )
            );

            g2.fillOval(
                    appleX1,
                    appleY1,
                    UNIT_SIZE,
                    UNIT_SIZE
            );

            // FIRST BALL SHINE

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    appleX1 + 7,
                    appleY1 + 5,
                    8,
                    8
            );

            // SECOND BALL OUTER GLOW

            g2.setColor(
                    new Color(
                            0,
                            180,
                            255,
                            40
                    )
            );

            g2.fillOval(
                    appleX2 - 8,
                    appleY2 - 8,
                    UNIT_SIZE + 16,
                    UNIT_SIZE + 16
            );

            // SECOND BALL INNER GLOW

            g2.setColor(
                    new Color(
                            0,
                            200,
                            255,
                            70
                    )
            );

            g2.fillOval(
                    appleX2 - 5,
                    appleY2 - 5,
                    UNIT_SIZE + 10,
                    UNIT_SIZE + 10
            );

            // SECOND BALL

            g2.setColor(
                    new Color(
                            80,
                            220,
                            255
                    )
            );

            g2.fillOval(
                    appleX2,
                    appleY2,
                    UNIT_SIZE,
                    UNIT_SIZE
            );

            // SECOND BALL SHINE

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    appleX2 + 7,
                    appleY2 + 5,
                    8,
                    8
            );

            g2.dispose();

            // =================================================
            // CODE LABELS
            // =================================================

            g.setFont(
                    new Font(
                            "Consolas",
                            Font.BOLD,
                            12
                    )
            );

            drawCodeLabel(
                    g,
                    codeBlocks[appleCode1],
                    appleX1,
                    appleY1
            );

            drawCodeLabel(
                    g,
                    codeBlocks[appleCode2],
                    appleX2,
                    appleY2
            );

            // =================================================
            // SNAKE
            // =================================================

            for (
                    int i = 0;
                    i < bodyParts;
                    i++
            ) {

                // HEAD

                if (i == 0) {

                    switch (direction) {

                        case 'U':

                            g.drawImage(
                                    headUp,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'D':

                            g.drawImage(
                                    headDown,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'L':

                            g.drawImage(
                                    headLeft,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'R':

                            g.drawImage(
                                    headRight,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;
                    }

                }

                // TAIL

                else if (
                        i == bodyParts - 1
                ) {

                    switch (direction) {

                        case 'U':

                            g.drawImage(
                                    tailUp,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'D':

                            g.drawImage(
                                    tailDown,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'L':

                            g.drawImage(
                                    tailLeft,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;

                        case 'R':

                            g.drawImage(
                                    tailRight,
                                    x[i],
                                    y[i],
                                    UNIT_SIZE,
                                    UNIT_SIZE,
                                    this
                            );

                            break;
                    }
                }

                // BODY

                else {

                    if (
                            direction == 'U' ||
                            direction == 'D'
                    ) {

                        g.drawImage(
                                bodyVerticalGlow,
                                x[i] - 4,
                                y[i] - 4,
                                UNIT_SIZE + 8,
                                UNIT_SIZE + 8,
                                this
                        );

                        g.drawImage(
                                bodyVertical,
                                x[i],
                                y[i],
                                UNIT_SIZE,
                                UNIT_SIZE,
                                this
                        );

                    } else {

                        g.drawImage(
                                bodyHorizontalGlow,
                                x[i] - 4,
                                y[i] - 4,
                                UNIT_SIZE + 8,
                                UNIT_SIZE + 8,
                                this
                        );

                        g.drawImage(
                                bodyHorizontal,
                                x[i],
                                y[i],
                                UNIT_SIZE,
                                UNIT_SIZE,
                                this
                        );
                    }
                }
            }

            // SCORE

            g.setColor(Color.WHITE);

            g.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            25
                    )
            );

            g.drawString(
                    "Score: " + applesEaten,
                    10,
                    30
            );

            // PAUSED

            if (paused) {

                g.setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                40
                        )
                );

                g.drawString(
                        "PAUSED",
                        220,
                        300
                );
            }

        } else {

            // =================================================
            // VICTORY
            // =================================================

            if (victory && !showSuccessScreen) {

                victory(g);

            }

            // =================================================
            // ERROR SCREEN
            // =================================================

            else if (showErrorScreen) {

                return;

            }

            // =================================================
            // SUCCESS SCREEN
            // =================================================

            else if (showSuccessScreen) {

                return;

            }

            // =================================================
            // NORMAL GAME OVER
            // =================================================

            else {

                gameOver(g);
            }
        }
    }

    // =====================================================
    // DRAW CODE LABEL
    // =====================================================

    public void drawCodeLabel(
            Graphics g,
            String code,
            int ballX,
            int ballY
    ) {

        int labelX =
                ballX + 32;

        int labelY =
                ballY + 18;

        FontMetrics fm =
                g.getFontMetrics();

        int textWidth =
                fm.stringWidth(code);

        int boxWidth =
                textWidth + 12;

        int boxHeight = 22;

        g.setColor(
                new Color(
                        0,
                        0,
                        0,
                        180
                )
        );

        g.fillRoundRect(
                labelX,
                ballY - 2,
                boxWidth,
                boxHeight,
                8,
                8
        );

        g.setColor(Color.WHITE);

        g.drawString(
                code,
                labelX + 6,
                labelY
        );
    }

    // =====================================================
    // WRONG ANSWER
    // =====================================================

    public void wrongAnswer() {

        if (lives > 0) {

            lives--;
        }

        if (lives > 0) {

            repaint();

            return;
        }

        running = false;
        victory = false;

        if (timer != null) {

            timer.stop();
            timer = null;
        }

        showErrorScreen = true;
        showErrorImage1 = true;

        if (gameFrame != null) {

            gameFrame.showErrorScreen();
        }

        if (errorBlinkTimer != null) {

            errorBlinkTimer.stop();
            errorBlinkTimer = null;
        }

        errorBlinkTimer =
                new Timer(
                        300,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(
                                    ActionEvent e
                            ) {

                                if (!showErrorScreen) {

                                    Timer t =
                                            (Timer) e.getSource();

                                    t.stop();

                                    return;
                                }

                                showErrorImage1 =
                                        !showErrorImage1;

                                if (gameFrame != null) {

                                    gameFrame.refreshErrorScreen();
                                }

                                repaint();
                            }
                        }
                );

        errorBlinkTimer.setRepeats(true);
        errorBlinkTimer.start();

        repaint();
    }

    // =====================================================
    // SHOW GAME ERROR
    // =====================================================

    public void showGameError() {

        if (showErrorScreen) {

            return;
        }

        running = false;
        paused = false;
        victory = false;

        if (timer != null) {

            timer.stop();
            timer = null;
        }

        showErrorScreen = true;
        showErrorImage1 = true;

        if (errorBlinkTimer != null) {

            errorBlinkTimer.stop();
            errorBlinkTimer = null;
        }

        if (gameFrame != null) {

            gameFrame.showErrorScreen();
        }

        errorBlinkTimer =
                new Timer(
                        300,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(
                                    ActionEvent e
                            ) {

                                if (!showErrorScreen) {

                                    Timer t =
                                            (Timer) e.getSource();

                                    t.stop();

                                    return;
                                }

                                showErrorImage1 =
                                        !showErrorImage1;

                                if (gameFrame != null) {

                                    gameFrame.refreshErrorScreen();
                                }

                                repaint();
                            }
                        }
                );

        errorBlinkTimer.setRepeats(true);
        errorBlinkTimer.start();

        repaint();
    }

    // =====================================================
    // SHOW SUCCESS SCREEN
    // =====================================================

    public void showSuccessScreen() {

        running = false;
        victory = true;

        // STOP GAME TIMER

        if (timer != null) {

            timer.stop();
            timer = null;
        }

        showSuccessScreen = true;
        showSuccessImage1 = true;

        // =================================================
        // RESET NEON GLOW
        // =================================================

        successGlowAlpha = 0.0f;
        successGlowIncreasing = true;

        // =================================================
        // STOP OLD SUCCESS TIMER
        // =================================================

        if (successBlinkTimer != null) {

            successBlinkTimer.stop();
            successBlinkTimer = null;
        }

        // =================================================
        // SUCCESS ANIMATION
        // =================================================

        successBlinkTimer =
                new Timer(
                        40,
                        new ActionListener() {

                            @Override
                            public void actionPerformed(
                                    ActionEvent e
                            ) {

                                // =================================
                                // NEON GLOW PULSE
                                // =================================

                                if (successGlowIncreasing) {

                                    successGlowAlpha += 0.01f;

                                    if (
                                            successGlowAlpha >= 1.0f
                                    ) {

                                        successGlowAlpha = 1.0f;

                                        successGlowIncreasing =
                                                false;
                                    }

                                } else {

                                    successGlowAlpha -= 0.01f;

                                    if (
                                            successGlowAlpha <= 0.0f
                                    ) {

                                        successGlowAlpha = 0.0f;

                                        successGlowIncreasing =
                                                true;
                                    }
                                }

                                // =================================
                                // SUCCESS IMAGE BLINK
                                // =================================

                                if (
                                        (System.currentTimeMillis() / 300)
                                                % 2 == 0
                                ) {

                                    showSuccessImage1 = true;

                                } else {

                                    showSuccessImage1 = false;
                                }

                                // =================================
                                // REFRESH FULLSCREEN SUCCESS
                                // =================================

                                if (gameFrame != null) {

                                    gameFrame.refreshSuccessScreen();
                                }

                                repaint();
                            }
                        }
                );

        successBlinkTimer.setRepeats(true);
        successBlinkTimer.start();

        // =================================================
        // SHOW FULLSCREEN SUCCESS
        // =================================================

        if (gameFrame != null) {

            gameFrame.showSuccessScreen();
        }

        repaint();
    }

    // =====================================================
    // GETTERS FOR GAMEFRAME
    // =====================================================

    public Image getGlowImage() {

        return glow;
    }

    public float getSuccessGlowAlpha() {

        return successGlowAlpha;
    }

    public Image getSuccessImage1() {

        return successImage1;
    }

    public Image getSuccessImage2() {

        return successImage2;
    }

    public boolean isShowingSuccessImage1() {

        return showSuccessImage1;
    }

    // =====================================================
    // RESTART GAME
    // =====================================================

    public void restartGame() {

        // STOP SUCCESS TIMER

        if (successBlinkTimer != null) {

            successBlinkTimer.stop();
            successBlinkTimer = null;
        }

        showSuccessScreen = false;
        showSuccessImage1 = true;

        if (gameFrame != null) {

            gameFrame.hideSuccessScreen();
        }

        // STOP ERROR TIMER

        if (errorBlinkTimer != null) {

            errorBlinkTimer.stop();
            errorBlinkTimer = null;
        }

        if (gameFrame != null) {

            gameFrame.hideErrorScreen();
        }

        // RESET LIVES

        lives = 3;

        // RESET SNAKE

        bodyParts = 6;

        // RESET SCORE

        applesEaten = 0;

        // RESET CODE

        nextCode = 0;

        // RESET DIRECTION

        direction = 'R';

        // RESET STATE

        victory = false;
        paused = false;

        showErrorScreen = false;
        showErrorImage1 = true;

        showSuccessScreen = false;
        showSuccessImage1 = true;

        successGlowAlpha = 0.0f;
        successGlowIncreasing = true;

        // RESET SNAKE POSITION

        for (
                int i = 0;
                i < GAME_UNITS;
                i++
        ) {

            x[i] = 0;
            y[i] = 0;
        }

        // STOP OLD GAME TIMER

        if (timer != null) {

            timer.stop();
            timer = null;
        }

        // START NEW GAME

        startGame();

        requestFocusInWindow();

        repaint();
    }

    // =====================================================
    // MOVE
    // =====================================================

    public void move() {

        for (
                int i = bodyParts;
                i > 0;
                i--
        ) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':

                y[0] -= UNIT_SIZE;

                break;

            case 'D':

                y[0] += UNIT_SIZE;

                break;

            case 'L':

                x[0] -= UNIT_SIZE;

                break;

            case 'R':

                x[0] += UNIT_SIZE;

                break;
        }
    }

    // =====================================================
    // CHECK CODE BALLS
    // =====================================================

    public void checkApple() {

        // =================================================
        // FIRST BALL
        // =================================================

        if (
                x[0] == appleX1 &&
                y[0] == appleY1
        ) {

            if (appleCode1 == nextCode) {

                applesEaten++;
                nextCode++;

                if (
                        nextCode >=
                        codeBlocks.length
                ) {

                    showSuccessScreen();

                    return;
                }

                newApple();

            } else {

                wrongAnswer();

                if (running) {

                    newApple();
                }
            }
        }

        // =================================================
        // SECOND BALL
        // =================================================

        else if (
                x[0] == appleX2 &&
                y[0] == appleY2
        ) {

            if (appleCode2 == nextCode) {

                applesEaten++;
                nextCode++;

                if (
                        nextCode >=
                        codeBlocks.length
                ) {

                    showSuccessScreen();

                    return;
                }

                newApple();

            } else {

                wrongAnswer();

                if (running) {

                    newApple();
                }
            }
        }
    }

    // =====================================================
    // COLLISIONS
    // =====================================================

    public void checkCollisions() {

        // BODY COLLISION

        for (
                int i = 1;
                i < bodyParts;
                i++
        ) {

            if (
                    x[0] == x[i] &&
                    y[0] == y[i]
            ) {

                showGameError();

                return;
            }
        }

        // LEFT EDGE

        if (x[0] < 0) {

            showGameError();

            return;
        }

        // RIGHT EDGE

        if (x[0] >= SCREEN_WIDTH) {

            showGameError();

            return;
        }

        // TOP EDGE

        if (y[0] < 0) {

            showGameError();

            return;
        }

        // BOTTOM EDGE

        if (y[0] >= SCREEN_HEIGHT) {

            showGameError();

            return;
        }
    }

    // =====================================================
    // VICTORY SCREEN
    // =====================================================

    public void victory(Graphics g) {

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        50
                )
        );

        FontMetrics metrics =
                getFontMetrics(
                        g.getFont()
                );

        g.drawString(
                "CODE COMPLETE!",
                (
                        SCREEN_WIDTH -
                        metrics.stringWidth(
                                "CODE COMPLETE!"
                        )
                ) / 2,
                250
        );

        // SCORE

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        g.drawString(
                "Score: " + applesEaten,
                220,
                320
        );

        // MESSAGE

        g.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        20
                )
        );

        g.drawString(
                "You completed the code!",
                190,
                355
        );

        // RESTART

        g.drawString(
                "Press R to Play Again",
                195,
                395
        );
    }

    // =====================================================
    // NORMAL GAME OVER
    // =====================================================

    public void gameOver(Graphics g) {

        g.setColor(Color.WHITE);

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        60
                )
        );

        FontMetrics metrics =
                getFontMetrics(
                        g.getFont()
                );

        g.drawString(
                "ERROR!",
                (
                        SCREEN_WIDTH -
                        metrics.stringWidth(
                                "ERROR!"
                        )
                ) / 2,
                SCREEN_HEIGHT / 2
        );

        // SCORE

        g.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        g.drawString(
                "Score: " + applesEaten,
                240,
                350
        );

        // RESTART

        g.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        20
                )
        );

        g.drawString(
                "Press R to Restart",
                205,
                390
        );
    }

    // =====================================================
    // TIMER
    // =====================================================

    @Override
    public void actionPerformed(
            ActionEvent e
    ) {

        if (
                running &&
                !paused
        ) {

            move();

            checkApple();

            // Kapag success na,
            // huwag nang mag-check collision.

            if (running) {

                checkCollisions();
            }
        }

        repaint();
    }

    // =====================================================
    // KEY PRESSED
    // =====================================================

    @Override
    public void keyPressed(
            KeyEvent e
    ) {

        switch (e.getKeyCode()) {

            // W

            case KeyEvent.VK_W:

                if (direction != 'D') {

                    direction = 'U';
                }

                break;

            // S

            case KeyEvent.VK_S:

                if (direction != 'U') {

                    direction = 'D';
                }

                break;

            // A

            case KeyEvent.VK_A:

                if (direction != 'R') {

                    direction = 'L';
                }

                break;

            // D

            case KeyEvent.VK_D:

                if (direction != 'L') {

                    direction = 'R';
                }

                break;

            // LEFT

            case KeyEvent.VK_LEFT:

                if (direction != 'R') {

                    direction = 'L';
                }

                break;

            // RIGHT

            case KeyEvent.VK_RIGHT:

                if (direction != 'L') {

                    direction = 'R';
                }

                break;

            // UP

            case KeyEvent.VK_UP:

                if (direction != 'D') {

                    direction = 'U';
                }

                break;

            // DOWN

            case KeyEvent.VK_DOWN:

                if (direction != 'U') {

                    direction = 'D';
                }

                break;

            // PAUSE

            case KeyEvent.VK_P:

                if (
                        !showErrorScreen &&
                        !showSuccessScreen
                ) {

                    paused = !paused;

                    repaint();
                }

                break;

            // RESTART

            case KeyEvent.VK_R:

                if (
                        !running &&
                        !showErrorScreen &&
                        !showSuccessScreen
                ) {

                    restartGame();
                }

                break;
        }
    }

    // =====================================================
    // KEY RELEASED
    // =====================================================

    @Override
    public void keyReleased(
            KeyEvent e
    ) {
    }

    // =====================================================
    // KEY TYPED
    // =====================================================

    @Override
    public void keyTyped(
            KeyEvent e
    ) {
    }
}