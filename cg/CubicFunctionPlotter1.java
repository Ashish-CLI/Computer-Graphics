import javax.swing.*;
import java.awt.*;
//y = x^3/12 -10<=x<=10


public class CubicFunctionPlotter1 extends JPanel{
    private final int width;
    private final int height;
    private final int scale;

    public CubicFunctionPlotter1 (int width, int height, int scale) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        drawAxes(g);
        g.setColor(Color.RED);
        plotCubicFunction(g);
    }

    private void drawAxes(Graphics g) {
        // Draw X and Y axes
        g.drawLine(0, height / 2, width, height / 2); // X-axis
        g.drawLine(width / 2, 0, width / 2, height); // Y-axis
    }

    private void plotCubicFunction(Graphics g) {
        for (int x = -10; x <= 10; x++) {
            int y = (int) ((Math.pow(x, 3) / 12));
            int screenX = width / 2 + x * scale;
            int screenY = height / 2 - y * scale; // Invert y for screen coordinates
            g.fillRect(screenX, screenY, 2, 2); // Plot point
        }
    }

    public static void main(String[] args) {
        final int width = 800;
        final int height = 600;
        final int scale = 20; // Scale factor for better visibility

        JFrame frame = new JFrame("Cubic Function Plotter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new CubicFunctionPlotter1(width, height, scale));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
