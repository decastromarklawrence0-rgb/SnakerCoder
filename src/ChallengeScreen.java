import javax.swing.*;

public class ChallengeScreen extends JFrame {

    public ChallengeScreen() {

        setTitle("Coding Challenge");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        add(new ChallengePanel());

        setVisible(true);
    }
}