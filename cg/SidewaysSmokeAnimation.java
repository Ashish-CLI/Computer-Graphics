import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class SmokeRing {
    private int x, y;
    private int width, height;
    private float alpha;

    public SmokeRing(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 20;
        this.alpha = 1.0f;
    }

    public void update() {
        y -= 3; // Move upwards
        width += 2;
        if (alpha > 0.05f) {
            alpha -= 0.015f;
        } else {
            alpha = 0;
        }
    }

    public boolean isVisible() {
        return alpha > 0;
    }

    public void draw(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawOval(x - width / 2, y - height / 2, width, height);
    }
}

class SmokePanel extends JPanel {
    private List<SmokeRing> smokeRings;
    private Random rand;

    public SmokePanel() {
        smokeRings = new ArrayList<>();
        rand = new Random();
        setBackground(Color.DARK_GRAY);

        Timer timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rand.nextInt(10) == 0) {
                    int x = getWidth() / 2 + rand.nextInt(60) - 30;
                    smokeRings.add(new SmokeRing(x, getHeight()));
                }

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
        Graphics2D g2d = (Graphics2D) g.create();

        for (SmokeRing ring : smokeRings) {
            ring.draw(g2d);
        }
        
        g2d.dispose();
    }
}

public class SidewaysSmokeAnimation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sideways Smoke Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 700);
            frame.add(new SmokePanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}