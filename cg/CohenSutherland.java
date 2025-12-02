import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class CohenSutherland extends JPanel {

    // Region codes
    static final int INSIDE = 0; // 0000
    static final int LEFT = 1;   // 0001
    static final int RIGHT = 2;  // 0010
    static final int BOTTOM = 4; // 0100
    static final int TOP = 8;    // 1000

    private final int xMin, yMin, xMax, yMax;
    private int x0, y0, x1, y1;
    private final int originalX0, originalY0, originalX1, originalY1;
    private boolean clipped;

    public CohenSutherland(int xMin, int yMin, int xMax, int yMax, int x0, int y0, int x1, int y1) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.originalX0 = this.x0 = x0;
        this.originalY0 = this.y0 = y0;
        this.originalX1 = this.x1 = x1;
        this.originalY1 = this.y1 = y1;
        this.clipped = false;
        cohenSutherlandClip();
    }

    private int computeCode(double x, double y) {
        int code = INSIDE;
        if (x < xMin)
            code |= LEFT;
        else if (x > xMax)
            code |= RIGHT;
        if (y < yMin)
            code |= BOTTOM;
        else if (y > yMax)
            code |= TOP;
        return code;
    }

    private void cohenSutherlandClip() {
        int code0 = computeCode(x0, y0);
        int code1 = computeCode(x1, y1);
        boolean accept = false;

        while (true) {
            if ((code0 == 0) && (code1 == 0)) {
                accept = true;
                break;
            } else if ((code0 & code1) != 0) {
                break;
            } else {
                int codeOut = (code0 != 0) ? code0 : code1;
                double x = 0, y = 0;

                if ((codeOut & TOP) != 0) {
                    x = x0 + (x1 - x0) * (yMax - y0) / (y1 - y0);
                    y = yMax;
                } else if ((codeOut & BOTTOM) != 0) {
                    x = x0 + (x1 - x0) * (yMin - y0) / (y1 - y0);
                    y = yMin;
                } else if ((codeOut & RIGHT) != 0) {
                    y = y0 + (y1 - y0) * (xMax - x0) / (x1 - x0);
                    x = xMax;
                } else if ((codeOut & LEFT) != 0) {
                    y = y0 + (y1 - y0) * (xMin - x0) / (x1 - x0);
                    x = xMin;
                }

                if (codeOut == code0) {
                    x0 = (int)x;
                    y0 = (int)y;
                    code0 = computeCode(x0, y0);
                } else {
                    x1 = (int)x;
                    y1 = (int)y;
                    code1 = computeCode(x1, y1);
                }
            }
        }
        clipped = accept;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, getHeight() / 2);

        // Draw clipping window
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xMin, yMin, xMax - xMin, yMax - yMin);

        // Draw original line
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(originalX0, originalY0, originalX1, originalY1);

        // Draw clipped line if it's visible
        if (clipped) {
            g2d.setColor(Color.RED);
            g2d.drawLine(x0, y0, x1, y1);
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

            System.out.println("Enter line endpoints:");
            System.out.print("x0: ");
            int x0 = scanner.nextInt();
            System.out.print("y0: ");
            int y0 = scanner.nextInt();
            System.out.print("x1: ");
            int x1 = scanner.nextInt();
            System.out.print("y1: ");
            int y1 = scanner.nextInt();

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Cohen-Sutherland Line Clipping");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new CohenSutherland(xMin, yMin, xMax, yMax, x0, y0, x1, y1));
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