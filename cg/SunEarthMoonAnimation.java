import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AnimationPanel extends JPanel {
    private int sunRadius = 50;
    private int earthRadius = 20;
    private int moonRadius = 8;
    private int earthOrbitRadius = 200;
    private int moonOrbitRadius = 40;
    private double earthAngle = 0;
    private double moonAngle = 0;

    public AnimationPanel() {
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                earthAngle += 0.01;
                moonAngle += 0.05;
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

        // Draw Earth's Orbit
        g.setColor(Color.GRAY);
        g.drawOval(centerX - earthOrbitRadius, centerY - earthOrbitRadius, earthOrbitRadius * 2, earthOrbitRadius * 2);

        // Calculate Earth's position
        int earthX = centerX + (int) (earthOrbitRadius * Math.cos(earthAngle));
        int earthY = centerY + (int) (earthOrbitRadius * Math.sin(earthAngle));

        // Draw Earth
        g.setColor(Color.BLUE);
        g.fillOval(earthX - earthRadius, earthY - earthRadius, earthRadius * 2, earthRadius * 2);
        
        // Draw Moon's Orbit
        g.setColor(Color.LIGHT_GRAY);
        g.drawOval(earthX - moonOrbitRadius, earthY - moonOrbitRadius, moonOrbitRadius * 2, moonOrbitRadius * 2);

        // Calculate Moon's position
        int moonX = earthX + (int) (moonOrbitRadius * Math.cos(moonAngle));
        int moonY = earthY + (int) (moonOrbitRadius * Math.sin(moonAngle));
        
        // Draw Moon
        g.setColor(Color.WHITE);
        g.fillOval(moonX - moonRadius, moonY - moonRadius, moonRadius * 2, moonRadius * 2);
    }
}

public class SunEarthMoonAnimation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sun, Earth, and Moon Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.add(new AnimationPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}