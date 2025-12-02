import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CirclePanel extends JPanel {
    private int radius = 50;
    private int minRadius = 20;
    private int maxRadius = 150;
    private boolean growing = true; 

    public CirclePanel() {
        Timer timer = new Timer(1, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateCircleSize();
                repaint();
            }
        });
        timer.start();
    }

    private void updateCircleSize() {
        if (growing) {
            radius++;
            if (radius >= maxRadius) {
                growing = false;
            }
        } else {
            radius--;
            if (radius <= minRadius) {
                growing = true;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int x = centerX - radius;
        int y = centerY - radius;

        g.setColor(Color.RED);

        g.fillOval(x, y, radius * 2, radius * 2);
    }
}

public class BlinkingCircle {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blinking Circle Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            frame.add(new CirclePanel());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}