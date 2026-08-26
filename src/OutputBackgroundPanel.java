import javax.swing.*;
import java.awt.*;

public class OutputBackgroundPanel extends JPanel {

    Image outputbackground;
    JLabel output;

    public OutputBackgroundPanel() {

        setLayout(null);

        // Background
        outputbackground = new ImageIcon(
            getClass().getResource("/images/outputBackground.png")
        ).getImage();

        // Output text
        output = new JLabel(
            "<html><center>OUTPUT:<br>Hello World</center></html>"
        );

        output.setFont(new Font("Consolas", Font.BOLD, 22));
        output.setForeground(Color.BLACK);

        // Center text horizontally and vertically
        output.setHorizontalAlignment(SwingConstants.CENTER);
        output.setVerticalAlignment(SwingConstants.CENTER);

        add(output);
    }

    // Automatically make the output text fill the panel
    @Override
    public void doLayout() {

        super.doLayout();

        output.setBounds(
            0,
            0,
            getWidth(),
            getHeight()
        );
    }

    // Background
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(
            outputbackground,
            0,
            0,
            getWidth(),
            getHeight(),
            this
        );
    }
}