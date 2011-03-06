package de.hpi.oryxengine.windows.loadGenerator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The Class LoadGeneratorWindow.
 */
public class LoadGeneratorWindow 
extends JPanel
implements ActionListener, PropertyChangeListener {
    
    private static final long serialVersionUID = 1L;

    // window components
    private JButton processModelButton;
    private JFileChooser fileChooser;
    private JTextField processModelText;
    private JFormattedTextField numberOfThreads;
    private JFormattedTextField numberOfInstances;
    
    // format values for formatted text fields
    private NumberFormat amountFormat;
    
    // default values to be shown in window
    private static final int DEFAULT_THREAD_NUMBER = 10;
    private static final int DEFAULT_INSTANCE_NUMBER = 10000;
    
    // variables to be passed on to Load Generator
    private int threads;
    private int instances;
    private String processModel;


    /**
     * Instantiates a new load generator window.
     */
    public LoadGeneratorWindow() {
       
        super(new BorderLayout());
        threads = DEFAULT_THREAD_NUMBER;
        instances = DEFAULT_INSTANCE_NUMBER;
        
        // -------------------- Area for creating small components like text fields or buttons --------------------
        // Create a field for to-be-loaded process model
        processModelText = new JTextField();
        processModelText.addActionListener(this);
        // Create file chooser to load a process model
        fileChooser = new JFileChooser();
        processModelButton = new JButton("Choose the process model");
        processModelButton.addActionListener(this);
        
        // Set up formats for formatted text fields
        amountFormat = NumberFormat.getNumberInstance();
        
        // Create field for number of navigator threads
        numberOfThreads = new JFormattedTextField(amountFormat);
        numberOfThreads.setValue(threads);
        numberOfThreads.addPropertyChangeListener("value", this);
        JLabel threadsLabel = new JLabel("number of navigator threads: ");
        threadsLabel.setLabelFor(numberOfThreads);
        
        // Create field for number of process instances
        numberOfInstances = new JFormattedTextField(amountFormat);
        numberOfInstances.setValue(instances);
        numberOfInstances.addPropertyChangeListener("vaule", this);
        JLabel instancesLabel = new JLabel("number of process instances: ");
        instancesLabel.setLabelFor(numberOfInstances);
        
        // Create a check box for showing monitor afterwards
        JCheckBox showMonitor = new JCheckBox("show monitor afterwards", false);
        
        // Create a start button for starting the load generator
        final JButton startButton = new JButton("Start");
        startButton.setActionCommand("Start");
        startButton.addActionListener(this);

        // -------------------- Area for creating panels like text field areas or button areas --------------------
        // Create a text field area
        JPanel textFieldArea = new JPanel(new GridLayout(0, 2));
        textFieldArea.setBorder(
            BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Navigator configuration"));
        
        // Create an area for the check box and a start button
        JPanel startingArea = new JPanel(new GridLayout(0, 1));
        startingArea.setBorder(BorderFactory.createEtchedBorder());     
        
        // -------------------- Area for putting the things together --------------------
        textFieldArea.add(processModelButton);
        textFieldArea.add(processModelText);
        textFieldArea.add(instancesLabel);
        textFieldArea.add(numberOfInstances);
        textFieldArea.add(threadsLabel);
        textFieldArea.add(numberOfThreads);
        startingArea.add(showMonitor);
        startingArea.add(startButton);
        this.add(textFieldArea, BorderLayout.NORTH);
        this.add(startingArea, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      //Handle process model button action.
        if (e.getSource() == processModelButton) {
            int returnValue = fileChooser.showOpenDialog(LoadGeneratorWindow.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                processModel = file.getName();
                processModelText.setText(processModel);
            } else {
                return;
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Object source = evt.getSource();
        if (source == numberOfInstances) {
            instances = (Integer) numberOfInstances.getValue();
        } else if (source == numberOfThreads) {
            threads = (Integer) numberOfThreads.getValue();
        }
    }

    /**
     * Create the GUI and show it.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("Load Generator GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // add the panel that is defined in the constructor
        frame.setContentPane(new LoadGeneratorWindow());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * The main method. Creates and starts the window.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
