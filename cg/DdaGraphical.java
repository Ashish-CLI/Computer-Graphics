import javax.swing.*;
import java.awt.*;


class LinePanel extends JPanel {
    private final int x1, y1, x2, y2;

    public LinePanel(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        drawDdaLine(g);
    }
    
    private void drawDdaLine(Graphics g) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            int plotX = Math.round(x);
            int plotY = Math.round(y);

            g.drawLine(plotX, plotY, plotX, plotY);

            x += xIncrement;
            y += yIncrement;
        }
    }
}

public class DdaGraphical {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DDA Line Drawing Algorithm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            frame.add(new LinePanel(100, 100, 100,300));
            // frame.add(new LinePanel(300, 200, 100, 100));
            
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}