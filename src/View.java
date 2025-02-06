import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Class used to group together all views for the project
 * 
 * @author Adam Carlström
 * @version 2024-05-15
 */
public abstract class View extends JPanel{
    private static Model model;
    
    /**
     * Constructor for view 
     * @param m the model connected to the view
     */
    public View(Model m) {
        model = m;
        model.registerView(this);
    }
    
    /**
     * Function used when changes are made to the screen
     * Used to make sure all views register these changes
     */
    public void update() {
        repaint();
    }
    
    /***
     * Function used to paint graphics on a panel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    //necessary function for all views to draw graphics
    public abstract void draw(Graphics g);

    /**
     * Getter 
     * @return model
     */
   public Model getModel() {
       return model;
   }    

   /**
    * Function to print out the view
    */
   public String toString(){
        return "View{" + "}";
   }
}
