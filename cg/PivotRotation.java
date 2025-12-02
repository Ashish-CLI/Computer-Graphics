import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PivotRotation extends JPanel {
    private final List<Point> originalPoints;
    private final List<Point> rotatedPoints;
    private double angleDegrees;
    private final Point pivotPoint;

    public PivotRotation(List<Point> points, Point pivot, double angle) {
        this.originalPoints = points;
        this.pivotPoint = pivot;
        this.angleDegrees = angle;
        this.rotatedPoints = new ArrayList<>();
        performRotation();
    }

    private void performRotation() {
        rotatedPoints.clear();
        double angleRadians = Math.toRadians(angleDegrees);
        double cosAngle = Math.cos(angleRadians);
        double sinAngle = Math.sin(angleRadians);

        for (Point p : originalPoints) {
            double translatedX = p.x - pivotPoint.x;
            double translatedY = p.y - pivotPoint.y;

            double rotatedX = translatedX * cosAngle - translatedY * sinAngle;
            double rotatedY = translatedX * sinAngle + translatedY * cosAngle;

            double finalX = rotatedX + pivotPoint.x;
            double finalY = rotatedY + pivotPoint.y;

            rotatedPoints.add(new Point((int) Math.round(finalX), (int) Math.round(finalY)));
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
        drawShape(g2d, rotatedPoints);

        g2d.setColor(Color.GREEN);
        g2d.fillOval(pivotPoint.x - 5, pivotPoint.y - 5, 10, 10);
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
            System.out.print("Enter Pivot X coordinate: ");
            int pivotX = scanner.nextInt();
            System.out.print("Enter Pivot Y coordinate: ");
            int pivotY = scanner.nextInt();
            Point pivot = new Point(pivotX, pivotY);

            System.out.print("Enter rotation angle in degrees: ");
            double angle = scanner.nextDouble();

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
                JFrame frame = new JFrame("Rotation About a Pivot Point");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new PivotRotation(shapePoints, pivot, angle));
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