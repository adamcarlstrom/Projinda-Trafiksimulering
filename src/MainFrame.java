import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.TitledBorder;

/**
 * Class used to show the project on a seperate frame
 * 
 * @author Adam Carlström
 * @version 2024-05-15
 */
public class MainFrame extends JFrame{
    private Model model;
    private IntersectionController intersectionController;
    private IntersectionView intersectionView;
    private JPanel panelView,panelController;

    /**
     * Constructor for mainframe to create the frame of the project
     */
    public MainFrame(){
        model = new Model();//creates the model to be used for the project

        panelView = new JPanel();//creates a panel to show the trafficsimulation
        panelController = new JPanel();//creates a panel to show buttons and similar things the user can interact with

        panelView.setBorder(new TitledBorder(""));
        panelController.setBorder(new TitledBorder(""));

        intersectionController = new IntersectionController(model);
        intersectionView = new IntersectionView(model);

        panelView.setLayout(new GridLayout(1,1));

        panelView.add(intersectionView);
        panelController.add(intersectionController);

        setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
    
        //set the size of each panel
        panelView.setPreferredSize(new Dimension (978,990));
        panelController.setPreferredSize(new Dimension (400,990));

        //adds the panels to this frame, so they are shown
        add(panelView,BorderLayout.WEST);
        add(panelController,BorderLayout.EAST);
        
        //necessary code to show the frame and make the program stop running when closed
        pack();
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}