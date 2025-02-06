import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class used to control the simulation
 * Contains buttons and variables the user can interact with
 * 
 * @author Adam Carlström
 * @version 2024-05-15
 */
public class IntersectionController extends Controller implements ActionListener, ChangeListener{
    private JPanel trafficlightPanel, carPanel, situationPanel;
    private JLabel lbltrafficlight,lblNrOfCars;
    private ButtonGroup btnGroup;
    private JRadioButton btnlogic1, btnlogic2, btnlogic3;
    private JButton situation1, situation2;
    private JSlider sldCar;
    private Model model;
    /**
     * Constructor, initializes everything that needs to be shown on this panel
     * @param m the model connected to this controller
     */
    public IntersectionController(Model m) {
        super(m);
        this.model = m;
        this.setLayout(new GridLayout(3,1));
        setBorder(new TitledBorder(""));
        trafficlightPanel = new JPanel();
        carPanel = new JPanel();
        lblNrOfCars = new JLabel("Amount of cars: ");
        lbltrafficlight = new JLabel("Logic for Trafficlight: ");

        btnlogic1 = new JRadioButton("Logic 1", true);
        btnlogic2 = new JRadioButton("Logic 2");
        btnlogic3 = new JRadioButton("Logic 3");
        btnGroup = new ButtonGroup();
        btnGroup.add(btnlogic1);
        btnGroup.add(btnlogic2);
        btnGroup.add(btnlogic3);


        sldCar = new JSlider(JSlider.HORIZONTAL, 0, 25, 4);
        sldCar.setMinorTickSpacing(1);  
        sldCar.setMajorTickSpacing(5);  
        sldCar.setPaintTicks(true);  
        sldCar.setPaintLabels(true);  

        btnlogic1.addActionListener(this);
        btnlogic2.addActionListener(this);
        btnlogic3.addActionListener(this);
        sldCar.addChangeListener(this);

        trafficlightPanel.setLayout(new GridLayout(2,2));
        trafficlightPanel.setBorder(new TitledBorder(""));
        trafficlightPanel.add(lbltrafficlight);
        trafficlightPanel.add(btnlogic1);
        trafficlightPanel.add(btnlogic2);
        trafficlightPanel.add(btnlogic3);


        carPanel.setLayout(new GridLayout(1,2));
        carPanel.setBorder(new TitledBorder(""));
        carPanel.add(lblNrOfCars);
        carPanel.add(sldCar);

        situationPanel = new JPanel();
        situationPanel.setBorder(new TitledBorder(""));
        situationPanel.setLayout(new BoxLayout(situationPanel, BoxLayout.Y_AXIS));
        situationPanel.add(new JLabel("Trafficsituations:"));
        situation1 = new JButton("Situation for logic2");
        situation2 = new JButton("Situation for logic3");
        situation1.addActionListener(this);
        situation2.addActionListener(this);
        situationPanel.add(situation1);
        situationPanel.add(situation2);


        this.add(trafficlightPanel);
        this.add(carPanel);
        this.add(situationPanel);

    }
    /***
     * Function that runs when a button is pressed
     * The function determines which button is pressed
     * and what to do after that
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if(o == btnlogic1){
            model.setLogic1();
        }else if(o == btnlogic2){
            model.setLogic2();
        }else if(o == btnlogic3){
            model.setLogic3();
        }else if(o == situation1){
            int n = 15;
            sldCar.setValue(n);
            model.situation1(n);
        }else if(o == situation2){
            int n = 19;
            sldCar.setValue(n);
            model.situation2(n);
        }

        revalidate();
    }

    /***
     * Function used to determine what happens when the 
     * state of the slider changes
     * Gets the value of the slider and sends this 
     * information to the model
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int value = (int)source.getValue();
            model.setAmountOfCars(value);
        }   
    }
    
}
