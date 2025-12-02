import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class SmokeRing {
    private int x, y;
    private int radius;
    private float alpha; // Opacity

    public SmokeRing(int x, int y) {
        this.x = x;
        this.y = y;
        this.radius = 10;
        this.alpha = 1.0f;
    }

    public void update() {
        y -= 2; // Move upwards
        radius += 1;
        if (alpha > 0.05f) {
            alpha -= 0.02f;
        } else {
            alpha = 0;
        }
    }

    public boolean isVisible() {
        return alpha > 0;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(0.5f, 0.5f, 0.5f, alpha));
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}

class SmokePanel extends JPanel {
    private List<SmokeRing> smokeRings;
    private Random rand;

    public SmokePanel() {
        smokeRings = new ArrayList<>();
        rand = new Random();

        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new smoke ring periodically
                if (rand.nextInt(5) == 0) {
                    int x = getWidth() / 2 + rand.nextInt(40) - 20;
                    smokeRings.add(new SmokeRing(x, getHeight()));
                }

                // Update and remove invisible rings
                smokeRings.removeIf(ring -> {
                    ring.update();
                    return !ring.isVisible();
                });

                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (SmokeRing ring : smokeRings) {
            ring.draw(g2d);
        }
    }
}

public class SmokeAnimation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Smoke Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 700);
            frame.add(new SmokePanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}