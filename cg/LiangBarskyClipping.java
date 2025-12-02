import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class LiangBarskyClipping extends JFrame {

    private static final int X_MIN = 100;
    private static final int Y_MIN = 100;
    private static final int X_MAX = 500;
    private static final int Y_MAX = 400;

    public LiangBarskyClipping() {
        setTitle("Liang-Barsky Line Clipping Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ClippingPanel panel = new ClippingPanel();
        add(panel);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private record Line(double x1, double y1, double x2, double y2) {
    }

    private class ClippingPanel extends JPanel {

        private final List<Line> testLines;

        public ClippingPanel() {
            setBackground(Color.WHITE);
            testLines = new ArrayList<>();
            testLines.add(new Line(150, 150, 450, 350));
            testLines.add(new Line(50, 50, 80, 80));
            testLines.add(new Line(50, 300, 550, 500));
            testLines.add(new Line(50, 150, 550, 250));
            testLines.add(new Line(450, 50, 600, 450));
            testLines.add(new Line(50, 50, 600, 50));
            testLines.add(new Line(200, 50, 200, 450));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(X_MIN, Y_MIN, X_MAX - X_MIN, Y_MAX - Y_MIN);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Clipping Window", X_MIN + 5, Y_MIN - 10);
            
            for (Line line : testLines) {
                g2d.setColor(new Color(150, 150, 150, 100));
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5, 5}, 0));
                g2d.draw(new Line2D.Double(line.x1, line.y1, line.x2, line.y2));

                Line clipped = liangBarskyClip(line);

                if (clipped != null) {
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.draw(new Line2D.Double(clipped.x1, clipped.y1, clipped.x2, clipped.y2));
                }
            }
            
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawString("LEGEND:", 550, 50);
            
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(550, 60, 20, 20);
            g2d.drawString("Clipping Window", 580, 75);
            
            g2d.setColor(new Color(150, 150, 150));
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5, 5}, 0));
            g2d.draw(new Line2D.Double(550, 100, 570, 100));
            g2d.drawString("Original Line (Unclipped)", 580, 105);

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(4));
            g2d.draw(new Line2D.Double(550, 130, 570, 130));
            g2d.drawString("Clipped Segment", 580, 135);
        }

        private Line liangBarskyClip(Line line) {
            double dx = line.x2 - line.x1;
            double dy = line.y2 - line.y1;

            double u1 = 0.0;
            double u2 = 1.0;

            double[] p = {-dx, dx, -dy, dy};
            double[] q = {line.x1 - X_MIN, X_MAX - line.x1, line.y1 - Y_MIN, Y_MAX - line.y1};

            for (int k = 0; k < 4; k++) {
                if (p[k] == 0) {
                    if (q[k] < 0) {
                        return null;
                    }
                } else {
                    double r = q[k] / p[k];
                    
                    if (p[k] < 0) {
                        u1 = Math.max(u1, r);
                    } else {
                        u2 = Math.min(u2, r);
                    }
                }
            }

            if (u1 > u2) {
                return null;
            }

            double x_clipped_start = line.x1 + u1 * dx;
            double y_clipped_start = line.y1 + u1 * dy;
            double x_clipped_end = line.x1 + u2 * dx;
            double y_clipped_end = line.y1 + u2 * dy;

            return new Line(x_clipped_start, y_clipped_start, x_clipped_end, y_clipped_end);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LiangBarskyClipping::new);
    }
}