import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnimationPanel extends JPanel {
    private int sunRadius = 50;
    private int earthRadius = 20;
    private int orbitRadius = 200;
    private double angle = 0;

    public AnimationPanel() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                angle += 0.01;
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw Sun
        g.setColor(Color.YELLOW);
        g.fillOval(centerX - sunRadius, centerY - sunRadius, sunRadius * 2, sunRadius * 2);

        // Calculate Earth's position
        int earthX = centerX + (int) (orbitRadius * Math.cos(angle));
        int earthY = centerY + (int) (orbitRadius * Math.sin(angle));

        // Draw Earth
        g.setColor(Color.BLUE);
        g.fillOval(earthX - earthRadius, earthY - earthRadius, earthRadius * 2, earthRadius * 2);
    }
}

public class SunEarthAnimation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sun Earth Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.add(new AnimationPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}