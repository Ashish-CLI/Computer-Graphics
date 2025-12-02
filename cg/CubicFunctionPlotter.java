import java.awt.*;
import javax.swing.*;

public class CubicFunctionPlotter extends JPanel {

    // Define the window dimensions
    private final int width;
    private final int height;

    // Define the center of the panel (origin of the graph)
    private final int xc;
    private final int yc;

    // Scale factor to make the graph visible
    private final int SCALE = 20;

    public CubicFunctionPlotter(int width, int height) {
        this.width = width;
        this.height = height;
        this.xc = width / 2;
        this.yc = height / 2;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw axes first
        drawAxes(g);
        
        // Set color for the function plot and draw it
        g.setColor(Color.BLUE);
        drawFunction(g);
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.GRAY);
        // Draw X-axis
        g.drawLine(0, yc, width, yc);
        // Draw Y-axis
        g.drawLine(xc, 0, xc, height);

        g.setColor(Color.BLACK);
        for (int i = -10; i <= 10; i++) {
            if (i == 0) continue;
            int tickX = xc + i * SCALE;
            g.drawLine(tickX, yc - 5, tickX, yc + 5);
            g.drawString(String.valueOf(i), tickX - 5, yc + 20);
        }
    }

    /**
     * Plots a single pixel on the screen.
     */
    private void putPixel(Graphics g, int x, int y) {
        // Using fillRect to draw a 2x2 pixel for better visibility
        g.fillRect(x, y, 2, 2);
    }

    private void drawFunction(Graphics g) {
        for (double x_math = -10.0; x_math <= 10.0; x_math += 0.01) {
            
            double y_math = (Math.pow(x_math, 3)) / 12.0;

            int x_screen = xc + (int) (x_math * SCALE);
            int y_screen = yc - (int) (y_math * SCALE);

            // Plot the transformed pixel
            putPixel(g, x_screen, y_screen);
        }
    }

    public static void main(String[] args) {
        final int width = 800;
        final int height = 600;

        JFrame frame = new JFrame("Plot of y = x^3 / 12");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new CubicFunctionPlotter(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}