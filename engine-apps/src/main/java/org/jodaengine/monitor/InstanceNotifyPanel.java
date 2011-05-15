package org.jodaengine.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jodaengine.navigator.schedule.SchedulerAction;
import org.jodaengine.navigator.schedule.SchedulerEvent;

/**
 * The Class InstanceNotifyPanel. You can use it in a Swing-GUI to present SchedulerEvents.
 */
public class InstanceNotifyPanel extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private LinkedList<JPanel> notifications;
    private static final int MAXIMUM_NOTIFICATIONS = 8;
    private static final Color GREEN = new Color(51, 255, 102);
    private static final Color RED = new Color(255, 102, 51);

    /**
     * Instantiates a new instance notify panel.
     */
    public InstanceNotifyPanel() {

        super();
        this.notifications = new LinkedList<JPanel>();
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Tell the Panel to add a notification for a given event.
     * 
     * @param event
     *            the event
     */
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

    /**
     * Creates a notification which is basically a JTextArea in a JPanel.
     * 
     * @param event
     *            the event
     * @return the text notification
     */
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

    /**
     * Text area for a new SchedulerEvent.
     * 
     * @param event
     *            the event that is to be displayed.
     * @return the j text area that does this.
     */
    private JTextArea textAreaFor(SchedulerEvent event) {

        JTextArea textArea = new JTextArea();
        if (event.getSchedulerAction() == SchedulerAction.SUBMIT) {
            textArea.setBackground(GREEN);
            textArea.setText("PI: " + event.getProcessToken().getID() + "\n" + "Added to Scheduler");
        } else if (event.getSchedulerAction() == SchedulerAction.RETRIEVE) {
            textArea.setBackground(RED);
            textArea.setText("PI: " + event.getProcessToken().getID() + "\n" + "Taken from Scheduler");
        }
        textArea.setOpaque(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);

        return textArea;
    }
}
