import java.awt.Point;
import java.awt.Color;

/**
 * Car class used to determine position of car
 * and if it needs to check if it is allowed to move
 * 
 * @author Adam Carlström
 * @author Arvid Wilhelmsson
 * @version 2024-05-15
 */

// Enum for directions
enum Direction {
    NORTH, SOUTH,EAST, WEST
}

// Enum for turns
enum Turn {
    STRAIGHT, RIGHT //,LEFT
}//left turn not allowed right now...


public class Car {
    private Direction directionComingFrom;
    private Turn desiredTurn;
    private Point position;
    private boolean allowedDrive, updatedWeight, hasTurned;
    private Model model;
    private TrafficLights trafficLight;
    private int x,y = 0;
    private int size;

    /***
     * Constructor of car
     * @param directionComingFrom is an enum for which direction the car should spawn in, determines how the car should move as well
     * @param desiredTurn is an enum for which direction the car should go in the intersection, whether to turn or go straight
     * @param model is the model connected to the car
     * @param trafficLight is the trafficlight connected to the car, from the direction it is coming from
     */
    public Car(Direction directionComingFrom, Turn desiredTurn, Model model, TrafficLights trafficLight) {
        this.directionComingFrom = directionComingFrom;
        this.desiredTurn = desiredTurn;
        this.allowedDrive = true;
        this.model = model;
        this.trafficLight = trafficLight;
        this.hasTurned = false;
        updatedWeight = false;
        size = 50/2;
        if(model.hasGotSize()){
            this.setOriginalPosition(model.getWidth(), model.getHeight(), model.getNet());
        }
        position = new Point(x,y);
    }

    /**
     * This function is used to determine whether the car should continue to 
     * drive, turn, or stop, depending on its positioning
     */
    public void updateAllowedDrive() {
        switch(directionComingFrom){
            //each direction has different positions to check
            case EAST:
                //This if statement checks where the car needs to stop if the trafficlight is red
                if(x <= model.getWidth()/model.getNet()*(model.getNet()/2-2) + model.getWidth()/model.getNet()*4+15  + 2*size*model.getWeightOfDirection(directionComingFrom)&& x >= model.getWidth()/model.getNet()*(model.getNet()/2-2) + model.getWidth()/model.getNet()*4 + 2*size*model.getWeightOfDirection(directionComingFrom)){
                    //if true, it needs to stop if the light is red
                    helpAllowedDrive();
                }
                //this statement checks where the car needs to turn in the intersection
                if(x >= model.getWidth()/model.getNet()*(model.getNet()/2) && x <= model.getWidth()/model.getNet()*(model.getNet()/2+1)-size-size/4){
                    this.turn();
                }
                break;
            case WEST:
                //This if statement checks where the car needs to stop if the trafficlight is red
                if(x <= model.getWidth()/model.getNet()*(model.getNet()/2-3)+model.getWidth()/model.getNet()/2 - 2*size*model.getWeightOfDirection(directionComingFrom) && x >= model.getWidth()/model.getNet()*(model.getNet()/2-3)+model.getWidth()/model.getNet()/2-15 - 2*size*model.getWeightOfDirection(directionComingFrom)){
                    //if true, it needs to stop if the light is red
                    helpAllowedDrive();
                }
                //this statement checks where the car needs to turn in the intersection
                if(x >= model.getWidth()/model.getNet()*(model.getNet()/2-1)+size/4 && x <= model.getWidth()/model.getNet()*(model.getNet()/2)){
                    //kolla ifall den vill svänga
                    this.turn();
                }
                break;
            case NORTH:
                //This if statement checks where the car needs to stop if the trafficlight is red
                if(y <= model.getHeight()/model.getNet()*(model.getNet()/2-3)+model.getWidth()/model.getNet()/2 - 2*size*model.getWeightOfDirection(directionComingFrom) && y >= model.getHeight()/model.getNet()*(model.getNet()/2-3)+model.getWidth()/model.getNet()/2-15 -2*size*model.getWeightOfDirection(directionComingFrom) ){
                    //if true, it needs to stop if the light is red
                    helpAllowedDrive();
                }
                //this statement checks where the car needs to turn in the intersection
                if(y >= model.getHeight()/model.getNet()*(model.getNet()/2-1)+size/4 && y<=model.getHeight()/model.getNet()*(model.getNet()/2)){
                    this.turn();
                }
                break;
            case SOUTH:
                //This if statement checks where the car needs to stop if the trafficlight is red
                if(y <= model.getHeight()/model.getNet()*(model.getNet()/2-2) + model.getHeight()/model.getNet()*4+15 + 2*size*model.getWeightOfDirection(directionComingFrom)&& y >= model.getHeight()/model.getNet()*(model.getNet()/2-2) + model.getHeight()/model.getNet()*4 + 2*size*model.getWeightOfDirection(directionComingFrom) ){
                    //if true, it needs to stop if the light is red
                    helpAllowedDrive();
                }
                //this statement checks where the car needs to turn in the intersection
                if(y >= model.getHeight()/model.getNet()*(model.getNet()/2) && y<=model.getHeight()/model.getNet()*(model.getNet()/2+1)-size-size/4){
                    this.turn();
                }
                break;
            default:
                throw new Error();
        }
    }

    /**
     * This function is called when the car is in a
     * position to stop if the light is red
     */
    public void helpAllowedDrive(){
        if(!hasTurned){//does not need to stop if the car has turned
            if (trafficLight.getColor() == Color.GREEN) {
                allowedDrive = true;
            } else {
                allowedDrive = false;
                if(!updatedWeight){
                    model.addWeightOfDirection(directionComingFrom);//update the traffic from this direction
                    updatedWeight=true;
                }
            }
        }
    }

    /**
     * This function is used to determine how the car should turn
     */
    private void turn(){
        switch (desiredTurn) {
            // case LEFT:
            //     switch (directionComingFrom) {
            //         case NORTH:
            //             directionComingFrom = Direction.WEST;
            //             break;
            //         case SOUTH:
            //             directionComingFrom = Direction.EAST;
            //             break;
            //         case EAST:
            //             directionComingFrom = Direction.NORTH;
            //             break;
            //         case WEST:
            //             directionComingFrom = Direction.SOUTH;
            //             break;
            //         default:
            //             break;
            //     }
            //     break;
            case RIGHT:  
                switch (directionComingFrom) {
                    case NORTH:
                        directionComingFrom = Direction.EAST;
                        break;
                    case SOUTH:
                        directionComingFrom = Direction.WEST;
                        break;
                    case EAST:
                        directionComingFrom = Direction.SOUTH;
                        break;
                    case WEST:
                        directionComingFrom = Direction.NORTH;
                        break;
                    default:
                        break;
                }
                break;
            case STRAIGHT://does not need to change anything if it is going straight 
            break;
            default:
            break;
        }
        desiredTurn = Turn.STRAIGHT;
        hasTurned = true;
    }

    /**
     * This function is used to determine the starting positions of cars when they spawn in
     * @param width is the width of the intersectionview panel
     * @param height is the height of the intersectionview panel
     * @param net is the net of the intersectionview panel
     */
    public void setOriginalPosition(int width, int height, int net) {
        switch(directionComingFrom){
            case EAST:
                x = width-50/2;
                y = width/net*(net/2-1) + height/net/4;
                break;
            case WEST:
                x = 0+10;
                y = width/net*(net/2) + height/net/4;
                break;
            case NORTH:
                x = width/net * 9 + width/net/4;
                y = 0;
                break;
            case SOUTH:
                x = width/net * 10 + width/net/4;
                y = height - 50/2;
                break;
            default:
                throw new Error();
        }
        position = new Point(x,y);    }
    

    // Getters
    public Direction getDirectionComingFrom() {
        return directionComingFrom;
    }
    // Getters
    public Turn getDesiredTurn() {
        return desiredTurn;     
    }
    // Getters
    public Point getPosition() {
        return position;
    }
    // Getters
    public boolean getAllowedDrive() {
        return allowedDrive;
    }
    // Getters
    public int getSize(){
        return size;
    }
    // Getters
    public boolean hasUpdatedWeight(){
        return updatedWeight;
    }
    // Setter
    public void setUpdatedWeight(boolean b){
        updatedWeight = b;
    }
    // Setter
    public void setPosition(Point pos){
        this.position = pos;
        x =(int)(pos.getX());
        y =(int)(pos.getY());

    }
    // Setter
    public void setAllowedDrive(boolean allowed){
        this.allowedDrive = allowed;
    }
    /**
     * To string to print out the car and its information
     */
    public String toString(){
        return "Car{" + position + ", driving: " + allowedDrive + ", from: " + directionComingFrom + ", going: "+ desiredTurn + ", updatedWeight: " + updatedWeight + ", hasTurned: " + hasTurned +  "}";
    }

}