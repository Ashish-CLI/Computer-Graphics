import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RotationAlgorithm extends JPanel {
    private List<Point> originalPoints;
    private List<Point> rotatedPoints;
    private double angle;

    public RotationAlgorithm() {
        originalPoints = new ArrayList<>();
        rotatedPoints = new ArrayList<>();
        
        getUserInput();
        
        performRotation();
    }

    private void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter rotation angle in degrees: ");
        angle = Math.toRadians(scanner.nextDouble());
        
        System.out.print("Enter the number of points: ");
        int n = scanner.nextInt();
        
        System.out.println("Enter the coordinates of points:");
        for (int i = 0; i < n; i++) {
            System.out.print("Point " + (i + 1) + " x-coordinate: ");
            int x = scanner.nextInt();
            System.out.print("Point " + (i + 1) + " y-coordinate: ");
            int y = scanner.nextInt();
            originalPoints.add(new Point(x, y));
        }
        
        scanner.close();
    }

    private void performRotation() {
        rotatedPoints.clear();
        for (Point p : originalPoints) {
            double x = p.x * Math.cos(angle) - p.y * Math.sin(angle);
            double y = p.x * Math.sin(angle) + p.y * Math.cos(angle);
            rotatedPoints.add(new Point((int) Math.round(x), (int) Math.round(y)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLUE);
        drawShape(g2d, originalPoints);

        g2d.setColor(Color.RED);
        drawShape(g2d, rotatedPoints);
    }

    private void drawShape(Graphics2D g2d, List<Point> points) {
        if (points.size() < 2) return;
        
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
        RotationAlgorithm algorithm = new RotationAlgorithm();
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rotation Algorithm");
            frame.add(algorithm);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}