package de.hpi.oryxengine.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import de.hpi.oryxengine.navigator.schedule.SchedulerAction;
import de.hpi.oryxengine.navigator.schedule.SchedulerEvent;

/**
 * The Class InstanceNotifyPanel.
 */
public class InstanceNotifyPanel extends JPanel {
    private LinkedList<JPanel> notifications;
    private int MAXIMUM_NOTIFICATIONS = 8;

    public InstanceNotifyPanel() {

        super();
        this.notifications = new LinkedList<JPanel>();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public void addNotification(SchedulerEvent event) {

        if (notifications.size() >= MAXIMUM_NOTIFICATIONS) {
            JPanel removedNotification = notifications.removeFirst();
            this.remove(removedNotification);
            this.validate();
        }
        JPanel newNotification = getTextNotification(event);
        notifications.add(newNotification);
        this.add(newNotification);
        this.repaint();
    }

    public JPanel getTextNotification(SchedulerEvent event) {

        int height = getHeight();
        int width = getWidth();
        int notificationHeight = (int) (((double) height) * 0.9);
        double notificationWidthAsDouble = ((double) width * 0.9) / MAXIMUM_NOTIFICATIONS;
        int notificationWidth = (int) notificationWidthAsDouble;

        JTextArea textArea = textAreaFor(event);

        textArea.setBounds(0, 0, notificationWidth, notificationHeight);

        JPanel content = new JPanel();
        content.setSize(notificationWidth, notificationHeight);
        content.setPreferredSize(new Dimension(notificationWidth, notificationHeight));
        content.add(textArea, BorderLayout.CENTER);
        content.setVisible(true);

        return content;
    }

    private JTextArea textAreaFor(SchedulerEvent event) {
                
        JTextArea textArea = new JTextArea();
        if (event.getSchedulerAction() == SchedulerAction.SUBMIT) {
            textArea.setBackground(new Color(51,255,102));
            textArea.setText("PI: " + event.getProcessInstance().getID() + "\n" +
                "Added to Scheduler");
        }
        else if (event.getSchedulerAction() == SchedulerAction.RETRIEVE) {
            textArea.setBackground(new Color(255,51,102));
            textArea.setText("PI: " + event.getProcessInstance().getID() + "\n" +
            "Taken from Scheduler");
        }
        textArea.setOpaque(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        
        return textArea;
    }
}
