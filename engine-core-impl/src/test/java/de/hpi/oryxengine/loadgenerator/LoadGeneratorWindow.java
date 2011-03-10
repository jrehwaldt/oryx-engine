package de.hpi.oryxengine.loadgenerator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sun.mail.mbox.NewlineOutputStream;

/**
 * The Class LoadGeneratorWindow.
 */
public class LoadGeneratorWindow extends JPanel implements ActionListener, PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    // window components
    private JComboBox processModelBox;
    private JFormattedTextField numberOfThreads;
    private JFormattedTextField numberOfInstances;
    private final JButton startButton;
    private JTextArea textArea;

    // format values for formatted text fields
    private NumberFormat amountFormat;

    // default values to be shown in window
    private static final int DEFAULT_THREAD_AMOUNT = 10;
    private static final int DEFAULT_INSTANCE_AMOUNT = 10000;
    private String[] processes;

    // variables to be passed on to Load Generator
    private int threads;
    private int instances;
    private String processModel;

    /**
     * Instantiates a new load generator window.
     */
    public LoadGeneratorWindow() {

        super(new BorderLayout());
        threads = DEFAULT_THREAD_AMOUNT;
        instances = DEFAULT_INSTANCE_AMOUNT;
        processes = findProcesses();
        // -------------------- Area for creating small components like text fields or buttons --------------------
        // Create combo box to choose a process model
        processModelBox = new JComboBox(processes);
        processModelBox.setSelectedIndex(-1);
        processModelBox.setActionCommand("processModel");
        processModelBox.addActionListener(this);
        JLabel processesLabel = new JLabel("choose your process: ");
        processesLabel.setLabelFor(processModelBox);
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
        numberOfInstances.addPropertyChangeListener("value", this);
        JLabel instancesLabel = new JLabel("number of process instances: ");
        instancesLabel.setLabelFor(numberOfInstances);

        // Create a check box for showing monitor afterwards
        JCheckBox showMonitor = new JCheckBox("show monitor afterwards", false);

        // Create a start button for starting the load generator
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        // Create a scrollable text area for the console output
        textArea = new JTextArea(9, 30);
        JScrollPane consoleText = new JScrollPane(textArea);

        // -------------------- Area for creating panels like text field areas or button areas --------------------
        // Create a text field area
        JPanel textFieldArea = new JPanel(new GridLayout(0, 2));
        textFieldArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            "Navigator configuration"));

        // Create an area for the check box and a start button
        JPanel startingArea = new JPanel(new GridLayout(0, 1));
        startingArea.setBorder(BorderFactory.createEtchedBorder());

        // Create an area for the console output to be displayed
        JPanel consoleArea = new JPanel(new GridLayout(0, 1));
        textFieldArea.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Console Output"));

        // -------------------- Area for putting the things together --------------------
        textFieldArea.add(processesLabel);
        textFieldArea.add(processModelBox);
        textFieldArea.add(instancesLabel);
        textFieldArea.add(numberOfInstances);
        textFieldArea.add(threadsLabel);
        textFieldArea.add(numberOfThreads);
        startingArea.add(showMonitor);
        startingArea.add(startButton);
        consoleArea.add(consoleText);
        this.add(textFieldArea, BorderLayout.NORTH);
        this.add(startingArea, BorderLayout.CENTER);
        this.add(consoleArea, BorderLayout.SOUTH);
    }

    /**
     * Find the example processes available for generating load.
     * 
     * @return the array of process names
     */
    private String[] findProcesses() {

        /*
         * File dir = new File(PATH_TO_PROCESSES); File[] fileList = dir.listFiles(); System.out.println(fileList);
         * for(File f : fileList) { System.out.println(f.getName()); }
         */
        String[] processModels = {"ExampleProcessTokenFactory", "HeavyComputationProcessTokenFactory"};
        return processModels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Handle start button action.
        if (e.getSource() == startButton) {
            if (processModel != null) {
                LoadGenerator gene = new LoadGenerator(processModel, instances, threads);
                gene.execute();
            } else {
                textArea.append("Please choose a process model." + System.getProperty("line.separator"));
            }
            // Handle the combo box
        } else if ("processModel".equals(e.getActionCommand())) {
            JComboBox box = (JComboBox) e.getSource();
            processModel = (String) box.getSelectedItem();
            System.out.println(processModel);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        Object source = evt.getSource();
        if (source == numberOfInstances) {
            instances = ((Long) numberOfInstances.getValue()).intValue();
        } else if (source == numberOfThreads) {
            threads = ((Long) numberOfThreads.getValue()).intValue();
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
     * @param args
     *            the arguments
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
