import java.util.ArrayList;
import java.util.Random;
import java.awt.Point;
import java.net.NoRouteToHostException;
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * IntersectionView class used to show the trafficsimulation
 * Uses java graphics to show everything
 * 
 * @author Adam Carlström
 * @authoer Arvid Wilhelmsson
 * @version 2024-05-15
 */

public class Model implements Runnable{
    private ArrayList<View> views = new ArrayList<>();
    public ArrayList<TrafficLights> trafficLights = new ArrayList<>();
    public ArrayList<Car> cars = new ArrayList<>();
    private int width, height,net, amountOfCars;
    private Thread t;
    private int currentTrafficLightIndex = 0, logic;
    private Timer trafficLightTimer;
    private HashMap<Direction, Integer> carCounts;
    private Boolean hasGotSize = false;
    private Queue<Car> carQueue;

    /**
     * Constructor for model
     */
    public Model() {
        views = new ArrayList<>();
        trafficLights = new ArrayList<>();
        cars = new ArrayList<>();
        carQueue = new LinkedList<> ();  
        carCounts = new HashMap<>();
        
        //create the trafficlights
        createTrafficLights(Location.NORTH, Turn.STRAIGHT);
        createTrafficLights(Location.EAST, Turn.STRAIGHT);
        createTrafficLights(Location.SOUTH, Turn.STRAIGHT);
        createTrafficLights(Location.WEST, Turn.STRAIGHT);
        
        for (Direction direction : Direction.values()) {
            carCounts.put(direction, 0);
        }
        
        createStartingCars();
        
        //create the thread to keep track and 
        //update positions of cars
        t = new Thread(this);
        t.start();

        setLogic1();

        //function to run another thread to keep track of the trafficlights
        startTrafficLightTimer();
    }

    /**
     * Function to register views to the model
     * @param v //the view to be registered
     */
    public void registerView(View v) {
        views.add(v);
        updateViews();
    }

    /**
     * Function to update the views
     */
    private void updateViews() {
        for(View view : views) {
            view.update();
        }
    }

    /**
     * Function to create the starting cars
     * One in each direction
     */
    private void createStartingCars(){
        amountOfCars = 4;
        cars.add(createCar(Direction.NORTH,Turn.STRAIGHT));
        cars.add(createCar(Direction.SOUTH,Turn.STRAIGHT));
        cars.add(createCar(Direction.EAST,Turn.STRAIGHT));
        cars.add(createCar(Direction.WEST,Turn.STRAIGHT));
    }

    /**
     * Helper function to create a number of random cars
     * Meaning they spawn and go in random directions
     * @param n amount of cars to be created
     */
    private void createRandomCar(int n){
        for (int index = 0; index < n; index++) {
            Direction direction;
            do{
                direction = Direction.values()[(int)(Math.random()*(Direction.values().length))];
            }while(getWeightOfDirection(direction) > 6);//so that there does not spawn too many cars in one direction
            carQueue.add(createCar(direction, Turn.values()[(int)(Math.random()*(Turn.values().length))]));
        }
    }

    /**
     * Helper function used to create the trafficlights and add them to the arraylist
     * @param location enum location of trafficlight
     * @param direction enum direction of trafficlight
     */
    public void createTrafficLights(Location location, Turn direction) {
        TrafficLights trafficLight = new TrafficLights(location, direction);
        trafficLights.add(trafficLight);
    }

    /**
     * Function used to create the cars and connect them to a trafficlight
     * @param directionComingFrom where the car will come from
     * @param desiredTurn how the car will turn
     * @return the newly created car
     */
    public Car createCar(Direction directionComingFrom, Turn desiredTurn) {
        TrafficLights associatedTrafficLight = findAssociatedTrafficLight(directionComingFrom);
        Car car = new Car(directionComingFrom, desiredTurn, this, associatedTrafficLight);
        return car;
    }

    /**
     * Helper function used to connect cars from a certain direction to
     * a trafficlight of the same direction
     * @param direction the direction
     * @return the trafficlight
     */
    private TrafficLights findAssociatedTrafficLight(Direction direction) {
        // Iterate through the traffic lights to find the one associated with the given direction
        for (TrafficLights trafficLight : trafficLights) {
            if (isDirectionAssociatedWithTrafficLight(trafficLight, direction)) {
                return trafficLight;
            }
        }

        return null;
    }

    /**
     * Function to see if the trafficlight and the direction of the car is a match. 
     * (direction and location is the same)
     * @param trafficLight
     * @param direction
     * @return
     */
    private boolean isDirectionAssociatedWithTrafficLight(TrafficLights trafficLight, Direction direction) {
        // Get the location and direction controlled by the traffic light
        Location trafficLightLocation = trafficLight.getLocation();

        switch (trafficLightLocation) {
            case NORTH:
                return direction == Direction.NORTH;
            case EAST:
                return direction == Direction.EAST;
            case SOUTH:
                return direction == Direction.SOUTH;
            case WEST:
                return direction == Direction.WEST;
            default:
                return false; 
        }
    }

    /**
     * Function to control the trafficlights. 
     * Uses a timer in order to control the trafficlight timings.
     * Calls the method for the preferred logic based on the logic variable.
     */
    private void startTrafficLightTimer() {
        trafficLightTimer = new Timer();
        trafficLightTimer.scheduleAtFixedRate(new TimerTask() {
            int time = 0;
            @Override
            public void run() {
                time++;
                if(time == 5){
                    switch(logic){
                        case 1:
                            randomTrafficLightAlgorithm(false);
                            break;
                        case 2:
                            oppositeTrafficLightAlgorithm(false);
                            break;
                        case 3:
                            densityTrafficLightAlgorithm(false);
                            break;
                    }
                    
                }else if(time == 6){//wait one extra second to allow cars to pass through the crossing before turning green
                    switch(logic){
                        case 1:
                            randomTrafficLightAlgorithm(true);
                            break;
                        case 2:
                            oppositeTrafficLightAlgorithm(true);
                            break;
                        case 3:
                            densityTrafficLightAlgorithm(true);
                            break;
                    }
                    time = 0;
                    
                }

            }
        }, 0, 1000); 
    }

    /**
     * Function to make the trafficlights in opposite direction green at the same time. 
     * Makes use of the currentTrafficLightIndex variable in order to control which lights are 
     * supposed to be green and red when the function is called. 
     * @param change
     */
    public void oppositeTrafficLightAlgorithm(boolean change) {
        if(!change){
            for (TrafficLights trafficLight : trafficLights) {
                trafficLight.setColor(Color.RED);
            }

        }else{
            Location l = trafficLights.get(currentTrafficLightIndex).getLocation();
            switch(l){
                case NORTH:
                case SOUTH:
                    trafficUpdates(Direction.NORTH);
                    trafficUpdates(Direction.SOUTH);
                    break;
                case EAST:
                case WEST: 
                    trafficUpdates(Direction.EAST);
                    trafficUpdates(Direction.WEST);
                    break;
                default:
                    break;
            }
            currentTrafficLightIndex = (currentTrafficLightIndex + 1) % trafficLights.size();
        }
    }

    /**
     * A function that updates the "allowedDrive" status of all the cars in a direciton when it turns green.
     * @param d
     */
    private void allowDriveCar(Direction d) {
        for (Car car : cars) {
            if(car.getDirectionComingFrom().equals(d)){
                car.helpAllowedDrive();
            }
        }
    }

    /***
     * Helper method to convert a location to a direction
     * @param l the location to be converted
     * @return the corresponding direction
     */
    public Direction convertLocationToDirection(Location l){
        switch(l){
            case NORTH:
            return Direction.NORTH;
            case SOUTH:
            return Direction.SOUTH;
            case WEST:
            return Direction.WEST;
            case EAST:
            return Direction.EAST;
            default:
            return null;
        }
    }

    /**
     * Helper method to convert a direction to a location
     * @param d the direction to be converted
     * @return the corresponding location
     */
    public Location convertDirectionToLocation(Direction d){
        switch(d){
            case NORTH:
            return Location.NORTH;
            case SOUTH:
            return Location.SOUTH;
            case WEST:
            return Location.WEST;
            case EAST:
            return Location.EAST;
            default:
            return null;
        }
    }

    /**
     * A method to control the logic of the trafficlights. It makes use of the hashmap that keeps track
     * of the weight of cars in each direction and accordingly makes the trafficlight in the direction with the most cars
     * green.
     * @param change
     */
    public void densityTrafficLightAlgorithm(boolean change){
        if(!change){
            for (TrafficLights trafficLight : trafficLights) {
                trafficLight.setColor(Color.RED);
            }
            
        }else{
            int weight = 0;
            Direction dir = null;
            for (Direction direction : Direction.values()){
                if(getWeightOfDirection(direction) >= weight){
                    dir = direction;
                    weight = getWeightOfDirection(dir);
                    System.out.println(""+dir + ": " + getWeightOfDirection(direction));
                }
            }
            Location l = convertDirectionToLocation(dir);
            switch(l){
                case NORTH:
                case SOUTH:
                    trafficUpdates(Direction.NORTH);
                    trafficUpdates(Direction.SOUTH);
                    break;
                case EAST:
                case WEST:
                    trafficUpdates(Direction.EAST);
                    trafficUpdates(Direction.WEST);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Helper method to update the data of the status of the intersection. 
     * It changes the color of the trafficlight, clears the hashmaps count of cars in the given direction
     * and updated the allowedDrive variable for the cars.
     * @param d
     */
    public void trafficUpdates(Direction d){
        findAssociatedTrafficLight(d).switchColor();
        clearWeightOfDirection(d);
        allowDriveCar(d);
    }

    /**
     * Method for an algortihm to control the trafficlights which makes a random one green 
     * and the others stay red.
     * @param change
     */
    public void randomTrafficLightAlgorithm(boolean change){
        if(!change){
            for (TrafficLights trafficLight : trafficLights) {
                trafficLight.setColor(Color.RED);
            }
        }else{
            Direction direction = Direction.values()[(int)(Math.random()*(Direction.values().length))];
            trafficUpdates(direction);
        }
    }


    /**
     * The runnable method that continuosly runs with a delay of 100ms
     * Used to update positions of cars and determine if cars should be
     * added or removed or if they are allowed to continue to drive
     */
    @Override
    public void run() {
        int time = 0;
        try{//wait one second before the simulation starts
            Thread.sleep(1000);
            //System.out.println(this.getHeight() + " " + this.getWidth());
            //  Set correct positions for the cars
            for (Car car : cars) {
                car.setOriginalPosition(this.getWidth(),this.getHeight(), this.getNet());
            }
            hasGotSize = true;

        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
        
        while(!Thread.interrupted()){//this part runs continously
            try{
                time++;
                Thread.sleep(100);
                for (int i = 0; i < cars.size(); i++) {
                    cars.get(i).updateAllowedDrive();
                    if(cars.get(i).getAllowedDrive()){//if the car is allowed to drive, update its position
                        cars.get(i).setUpdatedWeight(false);
                        Point p;
                        switch(cars.get(i).getDirectionComingFrom()){//positions are updated differently depending on where they come from
                            case EAST:
                                p = new Point((int)(cars.get(i).getPosition().getX()-10),(int)(cars.get(i).getPosition().getY()));
                                break;
                            case WEST:
                                p = new Point((int)(cars.get(i).getPosition().getX()+10),(int)(cars.get(i).getPosition().getY()));
                                break;
                            case NORTH:
                                p = new Point((int)(cars.get(i).getPosition().getX()),(int)(cars.get(i).getPosition().getY()+10));
                                break;
                            case SOUTH:
                                p = new Point((int)(cars.get(i).getPosition().getX()),(int)(cars.get(i).getPosition().getY()-10));
                                break;
                            default:
                                throw new Error();
                        }
                        cars.get(i).setPosition(p);
                    }
                    if((cars.get(i).getPosition().getX() < 0 || cars.get(i).getPosition().getX() > this.getWidth()) || (cars.get(i).getPosition().getY() < 0 || cars.get(i).getPosition().getY() > this.getHeight()) ){
                        //car outofbounds, remove car
                        cars.remove(i);
                        if(cars.size() < amountOfCars){//determines wether cars should be added or not
                            this.createRandomCar(1);
                        }
                    }
                }

                //check if cars should be added, cars are not added directly so they don't fall on top of each other
                if(carQueue.size() > 0 && time >= 5){//every 500ms a car should be added if necessary
                    if(!(getWeightOfDirection(carQueue.peek().getDirectionComingFrom()) > 6)){//if the direction has a lot of traffic, it should not be added
                        cars.add(carQueue.remove());
                        time=0;
                    }
                }
                updateViews();
            }catch(InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
    //setter
    public void setWidth(int w) {
        width = w;
    }
    //setter
    public void setHeight(int h) {
        height = h;
    }
    //setter
    public void setNet(int n){
        this.net = n;
    }
    //getter
    public int getNet(){
        return net;
    }
    //getter
    public int getWidth(){
        return width;
    }
    //getter
    public int getHeight(){
        return height;
    }
    //getter
    public ArrayList<Car> getCars(){
        return cars;
    }
    //getter
    public ArrayList<TrafficLights> getTrafficLights(){
        return trafficLights;
    }
    /**
     * toString of class model
     */
    public String toString(){
        return "Model{" + cars + ", " + views + "}";
    }
    //getter
    public boolean hasGotSize(){
        return hasGotSize;
    }
    //setter
    public void setAmountOfCars(int n){
        int i = amountOfCars;
        this.amountOfCars = n;
        if(i < n){// if more cars are to be created
            createRandomCar(n-i);
        }
    }
    //getter
    public int getWeightOfDirection(Direction d){
        return carCounts.get(d);
    }
    //Setter
    public void addWeightOfDirection(Direction d){
        carCounts.put(d,carCounts.get(d)+1);
    }
    /**
     * Function to clear the weight of a direction
     * to show that there is no traffic here
     * @param d the direction
     */
    public void clearWeightOfDirection(Direction d){
        carCounts.put(d,0);
    }

    /**
     * Function to create the situation2
     * @param n the amount of cars to be added
     */
    public void situation2(int n) {//weight is important
        clearStreet();
        amountOfCars = n;
        cars.add(createCar(Direction.NORTH, Turn.STRAIGHT));
        cars.add(createCar(Direction.SOUTH, Turn.STRAIGHT));

        for (int i = 0; i < n-4; i++) {
            carQueue.add(createCar(Direction.EAST, Turn.STRAIGHT));
            carQueue.add(createCar(Direction.WEST, Turn.STRAIGHT));
        }


    }

    /**
     * Function to create the situation1
     * @param n the amount of cars to be added
     */
    public void situation1(int n) {
        clearStreet();
        amountOfCars = n;

        for (int i = 0; i < n; i++) {
            carQueue.add(createCar(Direction.EAST, Turn.STRAIGHT));
            carQueue.add(createCar(Direction.WEST, Turn.STRAIGHT));
            carQueue.add(createCar(Direction.NORTH, Turn.STRAIGHT));
            carQueue.add(createCar(Direction.SOUTH, Turn.STRAIGHT));
        }
    }

    //clears the street of all traffic so the simulation can be restarted
    public void clearStreet(){
        cars = new ArrayList<>();
        carQueue = new LinkedList<> ();  
        carCounts = new HashMap<>();
        for (Direction direction : Direction.values()) {
            carCounts.put(direction, 0);
        }
        amountOfCars = 0;
    }

    /*
     * Functions used to set the logic that the trafficlights should use
     */
    public void setLogic1(){//random
        logic = 1;
    }
    public void setLogic2(){//opposite
        logic = 2;
    }
    public void setLogic3(){//weight
        logic = 3;
    }
}
