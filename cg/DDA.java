public class DDA {

    public void drawLine (int x1,int y1,int x2,int y2){
        int dx = x2 -x1 ;
        int dy = y2 -y1;
    
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xincr = (float) dx/steps;
        float yincr = (float) dy/steps;

        float x = x1;
        float y = y1;

        System.out.println("line from ( " + x1 +"," + y1 + ") to (" + x2 + "," + y2 + ")" );
        System.out.println("------------------------------------");
        for (int i = 0; i <= steps; i++) {
            int plotX = Math.round(x);
            int plotY = Math.round(y);
            System.out.println("(" + plotX + ", " + plotY + ")");

            x += xincr;
            y += yincr;
        }
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) {
        DDA d = new DDA();
        d.drawLine(100, 100, 300,200);
        d.drawLine(300, 200, 100, 100);
        d.drawLine(100, 100, 300,100);
        d.drawLine(100, 100, 100, 300); 
    }
    }


