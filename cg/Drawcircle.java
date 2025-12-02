import javax.swing.*;
import java.awt.*;

public class Drawcircle extends JPanel {
    protected void  paintComponent(Graphics g) {
        super.paintComponent(g);
        // Set the color to blue
        g.setColor(Color.BLUE);
        
       int x =100, y = 100, radius = 50;
        g.drawOval(x-radius, y-radius, 2*radius, 2*radius);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Circle Example");
        Drawcircle circlePanel = new Drawcircle();
        frame.add(circlePanel);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}