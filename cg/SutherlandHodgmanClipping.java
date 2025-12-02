import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SutherlandHodgmanClipping extends JFrame {

    private static final double X_MIN = 100;
    private static final double Y_MIN = 100;
    private static final double X_MAX = 500;
    private static final double Y_MAX = 400;

    public SutherlandHodgmanClipping() {
        setTitle("Sutherland-Hodgman Polygon Clipping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ClippingPanel panel = new ClippingPanel();
        add(panel);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private record Point(double x, double y) {
    }

    private class ClippingPanel extends JPanel {

        private final List<Point> initialPolygon;
        private List<Point> clippedPolygon;

        public ClippingPanel() {
            setBackground(Color.WHITE);
            initialPolygon = Arrays.asList(
                new Point(50, 200),
                new Point(300, 50),
                new Point(550, 200),
                new Point(450, 450),
                new Point(150, 450)
            );

            clippedPolygon = clip(initialPolygon);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect((int)X_MIN, (int)Y_MIN, (int)(X_MAX - X_MIN), (int)(Y_MAX - Y_MIN));
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Clipping Window", (int)X_MIN + 5, (int)Y_MIN - 10);
            
            g2d.setColor(new Color(150, 150, 150, 100));
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5, 5}, 0));
            drawPolygon(g2d, initialPolygon, false);

            if (clippedPolygon != null && clippedPolygon.size() >= 3) {
                g2d.setColor(new Color(30, 144, 255, 120));
                drawPolygon(g2d, clippedPolygon, true);

                g2d.setColor(new Color(0, 100, 0));
                g2d.setStroke(new BasicStroke(3));
                drawPolygon(g2d, clippedPolygon, false);
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
            g2d.draw(new java.awt.geom.Line2D.Double(550, 100, 570, 100));
            g2d.drawString("Original Polygon", 580, 105);

            g2d.setColor(new Color(0, 100, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(new java.awt.geom.Line2D.Double(550, 130, 570, 130));
            g2d.drawString("Clipped Polygon", 580, 135);
        }

        private void drawPolygon(Graphics2D g2d, List<Point> points, boolean fill) {
            if (points.size() < 2) return;
            
            Path2D path = new Path2D.Double();
            path.moveTo(points.get(0).x, points.get(0).y);
            for (int i = 1; i < points.size(); i++) {
                path.lineTo(points.get(i).x, points.get(i).y);
            }
            path.closePath();

            if (fill) {
                g2d.fill(path);
            } else {
                g2d.draw(path);
            }
        }

        private List<Point> clip(List<Point> inputPolygon) {
            List<Point> outputList = inputPolygon;

            outputList = clipEdge(outputList, X_MIN, Y_MIN, X_MIN, Y_MAX);
            outputList = clipEdge(outputList, X_MAX, Y_MAX, X_MAX, Y_MIN);
            outputList = clipEdge(outputList, X_MIN, Y_MAX, X_MAX, Y_MAX);
            outputList = clipEdge(outputList, X_MAX, Y_MIN, X_MIN, Y_MIN);
            
            return outputList;
        }

        private List<Point> clipEdge(List<Point> inputList, double x1, double y1, double x2, double y2) {
            List<Point> outputList = new ArrayList<>();
            if (inputList.isEmpty()) return outputList;

            double edgeDx = x2 - x1;
            double edgeDy = y2 - y1;

            Point s = inputList.get(inputList.size() - 1);

            for (Point e : inputList) {

                boolean s_inside = isInside(s, x1, y1, edgeDx, edgeDy);
                boolean e_inside = isInside(e, x1, y1, edgeDx, edgeDy);

                if (s_inside && e_inside) {
                    outputList.add(e);
                } 
                else if (!s_inside && e_inside) {
                    Point i = computeIntersection(s, e, x1, y1, edgeDx, edgeDy);
                    outputList.add(i);
                    outputList.add(e);
                } 
                else if (s_inside && !e_inside) {
                    Point i = computeIntersection(s, e, x1, y1, edgeDx, edgeDy);
                    outputList.add(i);
                }

                s = e;
            }

            return outputList;
        }

        private boolean isInside(Point p, double x1, double y1, double edgeDx, double edgeDy) {
            return (edgeDx * (p.y - y1) - edgeDy * (p.x - x1)) >= 0;
        }

        private Point computeIntersection(Point s, Point e, double x1, double y1, double edgeDx, double edgeDy) {
            double segDx = e.x - s.x;
            double segDy = e.y - s.y;

            double denominator = edgeDx * segDy - edgeDy * segDx;

            double t = ((s.x - x1) * edgeDy - (s.y - y1) * edgeDx) / denominator;

            t = Math.max(0.0, Math.min(1.0, t));

            double ix = s.x + t * segDx;
            double iy = s.y + t * segDy;

            return new Point(ix, iy);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SutherlandHodgmanClipping::new);
    }
}