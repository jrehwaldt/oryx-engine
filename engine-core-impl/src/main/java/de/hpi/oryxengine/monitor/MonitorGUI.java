package de.hpi.oryxengine.monitor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.navigator.schedule.SchedulerAction;
import de.hpi.oryxengine.navigator.schedule.SchedulerEvent;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class MonitorGUI has the task to present input provided by e.g. the Monitor-Plugin.
 */
public class MonitorGUI extends JFrame {

    private Monitor monitor;
    private static BarPaintSurface barSurface; 
    private static InstanceNotifyPanel notifySurface;
    
    public MonitorGUI() {
        super("Monitor");
    }
    
    public static MonitorGUI start(int maximumInstances) {
        MonitorGUI monitor = new MonitorGUI();
        monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        monitor.getContentPane().setLayout(new BorderLayout());
        
        barSurface = new BarPaintSurface(maximumInstances);
        barSurface.setPreferredSize(new Dimension(800,400));
        monitor.getContentPane().add(barSurface, BorderLayout.PAGE_START);
        
        notifySurface = new InstanceNotifyPanel();
        notifySurface.setPreferredSize(new Dimension(800,200));
        monitor.getContentPane().add(notifySurface, BorderLayout.PAGE_END);
        
        monitor.pack();
        monitor.setVisible(true);
        
        return monitor;
    }
    
    public BarPaintSurface getPaintSurface() {
        return barSurface;
    }
    
    public void updateNumberOfInstances(int numberOfInstances) {
        barSurface.updateInstances(numberOfInstances);
    }
    
    public void showInstanceRetrieved(ProcessInstance instance) {
        SchedulerEvent event = new SchedulerEvent(SchedulerAction.RETRIEVE, instance, -1);
        notifySurface.addNotification(event);
    }
    
    public void showInstanceSubmitted(ProcessInstance instance) {
        SchedulerEvent event = new SchedulerEvent(SchedulerAction.SUBMIT, instance, -1);
        notifySurface.addNotification(event);
    }
}
