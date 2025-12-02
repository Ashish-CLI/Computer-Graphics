import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Circle {
    private int x, y, radius;
    private int dx, dy; // Direction and speed
    private Color color;

    public Circle(int x, int y, int radius, int dx, int dy, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = dx;
        this.dy = dy;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public Color getColor() { return color; }

    public void move() {
        x += dx;
        y += dy;
    }

    public void reverse() {
        dx = -dx;
        dy = -dy;
    }

    public void checkWallCollision(int panelWidth, int panelHeight) {
        if (x - radius < 0 || x + radius > panelWidth) {
            dx = -dx;
        }
        if (y - radius < 0 || y + radius > panelHeight) {
            dy = -dy;
        }
    }

    public boolean intersects(Circle other) {
        int dx_dist = this.x - other.x;
        int dy_dist = this.y - other.y;
        double distance = Math.sqrt(dx_dist * dx_dist + dy_dist * dy_dist);
        return distance < this.radius + other.radius;
    }
}

class CollisionPanel extends JPanel {
    private Circle c1;
    private Circle c2;
    private boolean circlesCollided = false;

    public CollisionPanel() {
        c1 = new Circle(100, 250, 50, 2, 0, Color.RED);
        c2 = new Circle(400, 250, 50, -2, 0, Color.BLUE);

        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                c1.move();
                c2.move();
                
                c1.checkWallCollision(getWidth(), getHeight());
                c2.checkWallCollision(getWidth(), getHeight());

                checkCircleCollision();
                repaint();
            }
        });
        timer.start();
    }

    private void checkCircleCollision() {
        if (c1.intersects(c2)) {
            if (!circlesCollided) {
                c1.reverse();
                c2.reverse();
                circlesCollided = true;
            }
        } else {
            circlesCollided = false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(c1.getColor());
        g.fillOval(c1.getX() - c1.getRadius(), c1.getY() - c1.getRadius(), c1.getRadius() * 2, c1.getRadius() * 2);

        g.setColor(c2.getColor());
        g.fillOval(c2.getX() - c2.getRadius(), c2.getY() - c2.getRadius(), c2.getRadius() * 2, c2.getRadius() * 2);
    }
}

public class CircleCollision {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Circle Collision");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.add(new CollisionPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}