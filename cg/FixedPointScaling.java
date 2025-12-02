import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FixedPointScaling extends JPanel {
    private final List<Point> originalPoints;
    private final List<Point> scaledPoints;
    private final double sx;
    private final double sy;
    private final Point fixedPoint;

    public FixedPointScaling(List<Point> points, Point fixedPt, double sx, double sy) {
        this.originalPoints = points;
        this.fixedPoint = fixedPt;
        this.sx = sx;
        this.sy = sy;
        this.scaledPoints = new ArrayList<>();
        performScaling();
    }

    private void performScaling() {
        scaledPoints.clear();
        for (Point p : originalPoints) {
            double translatedX = p.x - fixedPoint.x;
            double translatedY = p.y - fixedPoint.y;

            double scaledX = translatedX * sx;
            double scaledY = translatedY * sy;

            double finalX = scaledX + fixedPoint.x;
            double finalY = scaledY + fixedPoint.y;

            scaledPoints.add(new Point((int) Math.round(finalX), (int) Math.round(finalY)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(getWidth() / 2, getHeight() / 2);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLUE);
        drawShape(g2d, originalPoints);

        g2d.setColor(Color.RED);
        drawShape(g2d, scaledPoints);

        g2d.setColor(Color.GREEN);
        g2d.fillOval(fixedPoint.x - 5, fixedPoint.y - 5, 10, 10);
    }

    private void drawShape(Graphics2D g2d, List<Point> points) {
        if (points == null || points.size() < 2) {
            return;
        }
        Path2D path = new Path2D.Double();
        Point firstPoint = points.get(0);
        path.moveTo(firstPoint.x, firstPoint.y);
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            path.lineTo(p.x, p.y);
        }
        path.closePath();
        g2d.draw(path);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter Fixed Point X coordinate: ");
            int fixedX = scanner.nextInt();
            System.out.print("Enter Fixed Point Y coordinate: ");
            int fixedY = scanner.nextInt();
            Point fixedPoint = new Point(fixedX, fixedY);

            System.out.print("Enter scaling factor in x-direction (sx): ");
            double sx = scanner.nextDouble();
            System.out.print("Enter scaling factor in y-direction (sy): ");
            double sy = scanner.nextDouble();

            System.out.print("Enter the number of points for the shape: ");
            int numPoints = scanner.nextInt();

            List<Point> shapePoints = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                System.out.print("Enter x-coordinate for point " + (i + 1) + ": ");
                int x = scanner.nextInt();
                System.out.print("Enter y-coordinate for point " + (i + 1) + ": ");
                int y = scanner.nextInt();
                shapePoints.add(new Point(x, y));
            }

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Scaling About a Fixed Point");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new FixedPointScaling(shapePoints, fixedPoint, sx, sy));
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter valid numbers.");
        } finally {
            scanner.close();
        }
    }
}