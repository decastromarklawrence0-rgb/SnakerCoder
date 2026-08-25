import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image background = new ImageIcon(
            getClass().getResource("/images/gamePanelBackg.png")
    ).getImage();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
 }
