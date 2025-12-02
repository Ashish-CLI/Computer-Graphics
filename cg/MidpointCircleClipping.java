import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MidpointCircleClipping extends JPanel {
    private final int xMin, yMin, xMax, yMax;
    private final int centerX, centerY, radius;
    private final List<Point> circlePoints;
    private final List<Point> clippedPoints;

    public MidpointCircleClipping(int xMin, int yMin, int xMax, int yMax, int centerX, int centerY, int radius) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.circlePoints = new ArrayList<>();
        this.clippedPoints = new ArrayList<>();
        generateCirclePoints();
        clipCircle();
    }

    private void generateCirclePoints() {
        int x = 0;
        int y = radius;
        int p = 1 - radius;

        plotPoints(x, y);

        while (x < y) {
            x++;
            if (p < 0) {
                p += 2 * x + 1;
            } else {
                y--;
                p += 2 * (x - y) + 1;
            }
            plotPoints(x, y);
        }
    }

    private void plotPoints(int x, int y) {
        circlePoints.add(new Point(centerX + x, centerY + y));
        circlePoints.add(new Point(centerX - x, centerY + y));
        circlePoints.add(new Point(centerX + x, centerY - y));
        circlePoints.add(new Point(centerX - x, centerY - y));
        circlePoints.add(new Point(centerX + y, centerY + x));
        circlePoints.add(new Point(centerX - y, centerY + x));
        circlePoints.add(new Point(centerX + y, centerY - x));
        circlePoints.add(new Point(centerX - y, centerY - x));
    }

    private void clipCircle() {
        for (Point p : circlePoints) {
            if (p.x >= xMin && p.x <= xMax && p.y >= yMin && p.y <= yMax) {
                clippedPoints.add(p);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);

        // Draw clipping window
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);

        // Draw original circle points
        g2d.setColor(Color.LIGHT_GRAY);
        for (Point p : circlePoints) {
            g2d.drawLine(p.x, p.y, p.x, p.y);
        }

        // Draw clipped circle points
        g2d.setColor(Color.RED);
        for (Point p : clippedPoints) {
            g2d.drawLine(p.x, p.y, p.x, p.y);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter clipping window coordinates:");
            System.out.print("xMin: ");
            int xMin = scanner.nextInt();
            System.out.print("yMin: ");
            int yMin = scanner.nextInt();
            System.out.print("xMax: ");
            int xMax = scanner.nextInt();
            System.out.print("yMax: ");
            int yMax = scanner.nextInt();

            System.out.println("Enter circle parameters:");
            System.out.print("Center X: ");
            int centerX = scanner.nextInt();
            System.out.print("Center Y: ");
            int centerY = scanner.nextInt();
            System.out.print("Radius: ");
            int radius = scanner.nextInt();

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Midpoint Circle Clipping");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new MidpointCircleClipping(xMin, yMin, xMax, yMax, centerX, centerY, radius));
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter valid integers.");
        } finally {
            scanner.close();
        }
    }
}