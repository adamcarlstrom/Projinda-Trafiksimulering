import javax.swing.JPanel;

/**
 * Class used to group together all controllers for the project
 * 
 * @author Adam Carlström
 * @version 2024-05-15
 */
class Controller extends JPanel{
    private Model model;
    
    /**
     * Constructor for controller
     * @param m the model used for this project
     */
    public Controller(Model m) {
        model = m;
    }
    
    /**
     * Getter
     * @return model
     */
    public Model getModel() {
        return model;
    }

}
