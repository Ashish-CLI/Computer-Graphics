import javax.swing.*;
import java.awt.*;

public class MidpointCircleGUI extends JPanel {

    private int centerX;
    private int centerY;
    private int radius;

    public MidpointCircleGUI(int centerX, int centerY, int radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        setBackground(Color.BLACK);
    }

    private void plotOctants(Graphics g, int xc, int yc, int x, int y) {
        g.drawLine(xc + x, yc + y, xc + x, yc + y);
        g.drawLine(xc - x, yc + y, xc - x, yc + y);
        g.drawLine(xc + x, yc - y, xc + x, yc - y);
        g.drawLine(xc - x, yc - y, xc - x, yc - y);
        g.drawLine(xc + y, yc + x, xc + y, yc + x);
        g.drawLine(xc - y, yc + x, xc - y, yc + x);
        g.drawLine(xc + y, yc - x, xc + y, yc - x);
        g.drawLine(xc - y, yc - x, xc - y, yc - x);
    }

    public void drawCircle(Graphics g, int xc, int yc, int r) {
        if (r < 0) return;

        int x = 0;
        int y = r;
        int d = 1 - r;

        g.setColor(Color.WHITE);
        plotOctants(g, xc, yc, x, y);

        while (x < y) {
            x++;
            if (d < 0) {
                d += 2 * x + 1;
            } else {
                y--;
                d += 2 * (x - y) + 1;
            }
            plotOctants(g, xc, yc, x, y);
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCircle(g, this.centerX, this.centerY, this.radius);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Midpoint Circle Algorithm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        MidpointCircleGUI circlePanel = new MidpointCircleGUI(300, 300, 200);
        frame.add(circlePanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
