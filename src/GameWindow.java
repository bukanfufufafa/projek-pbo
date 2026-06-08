import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class GameWindow {
    public static final int WIDTH = 1100;
    public static final int HEIGHT = 720;
    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

    private GameWindow() {
    }

    public static void apply(JFrame frame, String title) {
        frame.setTitle(title);

        URL iconUrl = frame.getClass().getResource("/gameburger/image/burger icon 2.png");
        if (iconUrl != null) {
            frame.setIconImage(new ImageIcon(iconUrl).getImage());
        }

        frame.setSize(SIZE);
        frame.setMinimumSize(SIZE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public static void centerContent(JFrame frame, JPanel content, String title) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(content.getBackground());
        wrapper.setPreferredSize(SIZE);

        frame.getContentPane().remove(content);
        wrapper.add(content);
        frame.setContentPane(wrapper);
        frame.pack();

        apply(frame, title);
    }
}
