import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class LineBresem extends JPanel {
    private final BufferedImage canvas;

    public LineBresem(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = canvas.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();

        Bresenhem(100, 100, 300, 100, Color.RED);
    }

    private void putPixel(int x, int y, Color color) {
        if (x >= 0 && x < canvas.getWidth() && y >= 0 && y < canvas.getHeight()) {
            canvas.setRGB(x, y, color.getRGB());
        }
    }

    private void Bresenhem(int x0, int y0, int x1, int y1, Color color) {
        boolean inf = false;
        if (x1 < x0 && y1 < y0) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
        }
        if (x1 == x0) {
            int temp = x0;
            x0 = y0;
            y0 = temp;
            temp = y1;
            y1 = x1;
            x1 = temp;
            inf = true;
        }
        int x = x0, y = y0;
        System.out.println("Plotting...");
        System.out.println("(" + x + ", " + y + ")");
        putPixel(x, y, color);
        int dx = x1 - x0;
        int dy = y1 - y0;
        int pk = 2 * dy - dx;
        for (int i = 0; i < dx; i++) {
            if (pk < 0) {
                x = x + 1;
                pk = pk + 2 * dy;
            } else {
                x = x + 1;
                y = y + 1;
                pk = pk + 2 * dy - 2 * dx;
            }
            if (inf == true) {
                putPixel(y, x, color);
            } else {
                putPixel(x, y, color);
            }
            System.out.println("(" + x + ", " + y + ")");
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Nitin's Canvas");
        LineBresem panel = new LineBresem(800, 600);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}