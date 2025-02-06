import java.awt.Color;

/**
 * Trafficlights class used to show either red or green
 * to either allow cars to drive or not
 * 
 * @author Arvid Wilhelmsson
 * @version 2024-05-15
 */

// Enum for directions
enum Location {
    NORTH, EAST, SOUTH, WEST
}

// Enum for turns
enum Turn {
    LEFT, STRAIGHT, RIGHT
}
public class TrafficLights {
    private Location location; // Location of the traffic light (north, east, south, or west)
    private Turn direction; // Direction the traffic light controls (right, straight, left)
    private Color color; // Color of the traffic light (green or red)
    private Boolean isGreen;

    /**
     * Constructor
     * @param location is an enum for the location of the trafficlight
     * @param direction is the direction the trafficlight allows for cars to drive (unecessary, always STRAIGHT)
     */
    public TrafficLights(Location location, Turn direction) {
        this.location = location;
        this.direction = direction; //should be (left), forward or right
        this.color = Color.RED; 
        isGreen = false;
    }

    /**
     * Function used to switch color of trafficlight
     */
    public void switchColor() {
        if (color.equals(Color.RED)) {
            color = Color.GREEN;
            isGreen = true;
        } else {
            color = Color.RED;
            isGreen = false;
        }
    }

    // Getters and setters
    public Location getLocation() {
        return location;
    }
    // Getters and setters
    public void setLocation(Location location) {
        this.location = location;
    }
    // Getters and setters
    public Turn getDirection() {
        return direction;
    }
    // Getters and setters
    public void setDirection(Turn direction) {
        this.direction = direction;
    }
    // Getters and setters
    public Color getColor() {
        return color;
    }
    // Getters and setters
    public void setColor(Color color) {
        this.color = color;
        if(color == Color.GREEN){
            isGreen = true;
        }else{
            isGreen = false;
        }
    }
    // Getters and setters
    public boolean isGreen(){
        return isGreen;
    }
    /**
     * To string method to print class and its information
     */
    public String toString(){
        return "Trafikljus{" + location + ", " + direction + ", " + isGreen + ", " + color + "}";
    }
}
