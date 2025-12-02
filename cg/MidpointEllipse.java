import java.awt.*;
import javax.swing.*;

public class MidpointEllipse extends JPanel {
    private final int xc, yc;
    private final int rx, ry;

    public MidpointEllipse(int width, int height, int rx, int ry) { 
        this.xc = width / 2;
        this.yc = height / 2;
        this.rx = rx;
        this.ry = ry;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.RED);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        drawMidpointEllipse(g, xc, yc, rx, ry);
    }

    private void putPixel(Graphics g, int x, int y) {
        g.fillRect(x, y, 1, 1);
    }

    private void plotSymmetric(Graphics g, int xc, int yc, int x, int y) {
        putPixel(g, xc + x, yc + y);
        putPixel(g, xc - x, yc + y);
        putPixel(g, xc + x, yc - y);
        putPixel(g, xc - x, yc - y);
    }

    private void drawMidpointEllipse(Graphics g, int xc, int yc, int rx, int ry) {
        double rx2 = rx * (double) rx;
        double ry2 = ry * (double) ry;

        // Region 1
        int x = 0;
        int y = ry;
        double dx = 2 * ry2 * x;
        double dy = 2 * rx2 * y;

        double p1 = ry2 - (rx2 * ry) + (0.25 * rx2);

        while (dx < dy) {
            plotSymmetric(g, xc, yc, x, y);
            x++;
            dx = dx + 2 * ry2;
            if (p1 < 0) {
                p1 = p1 + dx + ry2;
            } else {
                y--;
                dy = dy - 2 * rx2;
                p1 = p1 + dx - dy + ry2;
            }
        }

        // Region 2
        double p2 = (ry2 * (x + 0.5) * (x + 0.5)) + (rx2 * (y - 1) * (y - 1)) - (rx2 * ry2);
        while (y >= 0) {
            plotSymmetric(g, xc, yc, x, y);
            y--;
            dy = dy - 2 * rx2;
            if (p2 > 0) {
                p2 = p2 + rx2 - dy;
            } else {
                x++;
                dx = dx + 2 * ry2;
                p2 = p2 + dx - dy + rx2;
            }
        }
    }

    public static void main(String[] args) {
        final int width = 800;
        final int height = 600;
        final int rx = 250;
        final int ry = 150;

        JFrame frame = new JFrame("MIDPOINT ELLIPSE ALGORITHM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new MidpointEllipse(width, height, rx, ry));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}