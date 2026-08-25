import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu() {

        setTitle("Snaker Coder");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MenuPanel panel = new MenuPanel();
        add(panel);

        setVisible(true);
    }
}