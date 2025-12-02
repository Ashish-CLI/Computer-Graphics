import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReflectionY extends JPanel {
    private final List<Point> originalPoints;
    private final List<Point> reflectedPoints;

    public ReflectionY(List<Point> points) {
        this.originalPoints = points;
        this.reflectedPoints = new ArrayList<>();
        performReflection();
    }

    private void performReflection() {
        reflectedPoints.clear();
        for (Point p : originalPoints) {
            reflectedPoints.add(new Point(-p.x, p.y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.translate(getWidth() / 2, getHeight() / 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0);
        g2d.drawLine(0, -getHeight() / 2, 0, getHeight() / 2);

        g2d.setColor(Color.BLUE);
        drawShape(g2d, originalPoints);

        g2d.setColor(Color.RED);
        drawShape(g2d, reflectedPoints);
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
                JFrame frame = new JFrame("Reflection About Y-Axis");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ReflectionY(shapePoints));
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