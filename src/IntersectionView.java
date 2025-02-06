import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 * IntersectionView class used to show the trafficsimulation
 * Uses java graphics to show everything
 * 
 * @author Adam Carlström
 * @version 2024-05-15
 */

public class IntersectionView extends View{//size width: 990 height: 978
    private Model model;
    private boolean sentSize = false;
    private int net;
    /**
     * Constructor
     * @param model the model connected to this view
     */
    public IntersectionView(Model model){
        super(model);
        this.model = model;
        net = 20;
    }

    /**
     * Function used to draw all graphics shown in the view
     */
    @Override
    public void draw(Graphics g) {
        if(!sentSize){//sends size of screen to model
            sentSize = true;
            model.setHeight(this.getHeight());
            model.setWidth(this.getWidth());
            model.setNet(net);
        }
        g.setColor(Color.BLACK);

        //create a background net
        for (int i = 0; i < net; i++) {
            for (int j = 0; j <net ; j++) {
                switch((i+j)%2){
                    case 0: {
                        //even
                        g.setColor(new Color(214,211,221));
                        break;
                    }
                    case 1: {
                        //odd
                        g.setColor(new Color(240,240,240));
                        break;
                    }
                    default: {
                        g.setColor(Color.BLACK);
                    }
                }
                g.fillRect(getWidth()/net*i,getHeight()/net*j,getWidth()/net,getHeight()/net);
            }
        }
        //fillRect(int x, int y, int width, int height)

        //create the roads
        g.setColor(Color.BLACK);
        g.fillRect(0,getHeight()/net*(net/2-1),getWidth(),getHeight()/net*2);//horisontell
        g.fillRect(getWidth()/net*(net/2-1),0,getWidth()/net*2,getHeight());//vertikal

        // //area where cars need to stop
        // g.setColor(Color.BLUE);
        // g.fillRect(getWidth()/net*(net/2-2),getHeight()/net*(net/2-2),getWidth()/net*4,getHeight()/net*4);

        // create white lines between roads
        g.setColor(Color.WHITE);
        for (int i = 0; i < net*2; i++) {
            g.fillRect(getWidth()/net*(net/2),getHeight()/net*i, getWidth()/net/10-getWidth()/net/10/2, getHeight()/net/2);//vertical
            g.fillRect(getWidth()/net*i,getHeight()/net*(net/2),getWidth()/net/2,getHeight()/net/10-getHeight()/net/10/2);//horizontal
        }
        // create stoplines
        g.fillRect(getWidth()/net*(net/2-1),getHeight()/net*(net/2-2),getWidth()/net,getHeight()/net/10);//NORTH
        g.fillRect(getWidth()/net*(net/2),getHeight()/net*(net/2+2)-getHeight()/net/10,getWidth()/net,getHeight()/net/10);//SOUTH
        g.fillRect(getWidth()/net*(net/2-2),getHeight()/net*(net/2),getWidth()/net/10,getHeight()/net);//WEST
        g.fillRect(getWidth()/net*(net/2+2)-getWidth()/net/10,getHeight()/net*(net/2-1),getWidth()/net/10,getHeight()/net);//EAST

        //create cars
        g.setColor(Color.YELLOW);
        for (Car car : model.cars) {
            if (!(car.getPosition().getX() == 0 && car.getPosition().getY() == 0)){
                g.fillOval((int)(car.getPosition().getX()), (int)(car.getPosition().getY()), car.getSize(), car.getSize());
            }
        }

        //create trafficlights
        for (TrafficLights light: model.trafficLights){
            Point p = new Point(0,0);
            switch(light.getLocation()){
                case NORTH:
                    p = new Point(getWidth()/net*(net/2-2),getHeight()/net*(net/2-4));
                    break;
                case SOUTH:
                    p = new Point(getWidth()/net*(net/2+1),getHeight()/net*(net/2+2));
                    break;
                case EAST:
                    p = new Point(getWidth()/net*(net/2+2),getHeight()/net*(net/2-3));
                    break;
                case WEST:
                    p = new Point(getWidth()/net*(net/2-3),getHeight()/net*(net/2+1));
                    break;
            }
            drawTrafficLight(g, p,light.isGreen());
        }
        
    }
    
    /**
     * Helper method used to draw the trafficlights
     * @param g graphics component used to draw
     * @param p is the position the trafficlight should be placed 
     * @param isGreen whether the green or red light should be on
     */
    private void drawTrafficLight(Graphics g, Point p,Boolean isGreen){
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)(p.getX()),(int)(p.getY()) , getWidth()/net, getHeight()/net*2);
        g.setColor(Color.BLACK);
        g.drawOval((int)(p.getX())+2, (int)(p.getY()) +1 + getHeight()/net*2/10, getWidth()/net-4, getHeight()/net/2-2);
        g.setColor(Color.BLACK);
        g.drawOval((int)(p.getX())+2, (int)(p.getY()) +1 + getHeight()/net*2/10 + getHeight()/net, getWidth()/net-4, getHeight()/net/2-2);
        
        if(isGreen){
            g.setColor(Color.GREEN);
            g.fillOval((int)(p.getX())+4, (int)(p.getY()) +2 + getHeight()/net*2/10 + getHeight()/net, getWidth()/net-8, getHeight()/net/2-4);
        }else{
            g.setColor(Color.RED);
            g.fillOval((int)(p.getX())+4, (int)(p.getY()) +2 + getHeight()/net*2/10, getWidth()/net-8, getHeight()/net/2-4);
        }
    }
     /**
      * Helper method to draw a square
      * @param g used to draw 
      * @param x1 starting x
      * @param x2 ending x
      * @param y1 starting y
      * @param y2 ending y
      */
    private void drawSquare(Graphics g, int x1, int x2, int y1,int y2){
        g.setColor(Color.GREEN);
        g.fillRect(x1,y1,x2-x1,y2-y1);
    }
}
