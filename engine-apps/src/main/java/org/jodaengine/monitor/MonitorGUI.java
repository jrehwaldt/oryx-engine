package org.jodaengine.monitor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.jodaengine.navigator.schedule.SchedulerAction;
import org.jodaengine.navigator.schedule.SchedulerEvent;
import org.jodaengine.process.token.BPMNToken;

/**
 * The Class MonitorGUI has the task to present input provided by e.g. the Monitor-Listener.
 */
public class MonitorGUI extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -7601506207557238298L;
    private static BarPaintSurface barSurface;
    private static InstanceNotifyPanel notifySurface;

    /**
     * Instantiates a new monitor gui.
     */
    public MonitorGUI() {

        super("Monitor");
    }

    /**
     * Start the monitor GUI. Renders a new JFrame.
     * 
     * @param maximumInstances
     *            the highest number of instances that can occur in the scheduler.
     * @return the monitor gui
     */
    public static MonitorGUI start(int maximumInstances) {

        MonitorGUI monitor = new MonitorGUI();
        monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        monitor.getContentPane().setLayout(new BorderLayout());

        barSurface = new BarPaintSurface(maximumInstances);
        barSurface.setPreferredSize(new Dimension(800, 400));
        monitor.getContentPane().add(barSurface, BorderLayout.PAGE_START);

        notifySurface = new InstanceNotifyPanel();
        notifySurface.setPreferredSize(new Dimension(800, 200));
        monitor.getContentPane().add(notifySurface, BorderLayout.PAGE_END);

        monitor.pack();
        monitor.setVisible(true);

        return monitor;
    }

    /**
     * Gets the paint surface.
     * 
     * @return the paint surface
     */
    public BarPaintSurface getPaintSurface() {

        return barSurface;
    }

    /**
     * Tell the PaintSurface that draws the Scheduler-Bars that the number of instances in the scheduler has changed.
     * 
     * @param numberOfInstances
     *            the number of instances
     */
    public void updateNumberOfInstances(int numberOfInstances) {

        barSurface.updateInstances(numberOfInstances);
    }

    /**
     * An instance has been retrieved from the scheduler. Show it in the GUI.
     * 
     * @param instance
     *            the instance
     */
    public void showInstanceRetrieved(BPMNToken instance) {

        SchedulerEvent event = new SchedulerEvent(SchedulerAction.RETRIEVE, instance, -1);
        notifySurface.addNotification(event);
    }

    /**
     * An instance has been submitted to the scheduler. Show it in the GUI.
     * 
     * @param instance
     *            the instance
     */
    public void showInstanceSubmitted(BPMNToken instance) {

        SchedulerEvent event = new SchedulerEvent(SchedulerAction.SUBMIT, instance, -1);
        notifySurface.addNotification(event);
    }
}
