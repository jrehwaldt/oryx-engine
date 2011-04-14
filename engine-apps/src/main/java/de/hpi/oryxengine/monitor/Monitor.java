package de.hpi.oryxengine.monitor;

import java.util.LinkedHashSet;

import de.hpi.oryxengine.plugin.scheduler.AbstractSchedulerListener;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class Monitor is a Plugin that receives Scheduler Events and forwards it to the provided GUI.
 */
public class Monitor extends AbstractSchedulerListener {

    private MonitorGUI gui;
    private LinkedHashSet<Token> instancesToTrack;

    /**
     * Instantiates a new monitor. This plugin listens for Scheduler events, such as a new instance that is scheduled,
     * etc.
     * 
     * @param gui
     *            the gui to inform about events.
     */
    public Monitor(MonitorGUI gui) {

        this.gui = gui;
        this.instancesToTrack = new LinkedHashSet<Token>();
    }

    @Override
    public void processInstanceSubmitted(int numberOfInstances, Token token) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(token)) {
            showInstanceSubmitted(token);
        }
    }

    @Override
    public void processInstanceRetrieved(int numberOfInstances, Token token) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(token)) {
            showInstanceRetrieved(token);
        }
    }

    /**
     * Show instance submitted on the GUI.
     *
     * @param instance the instance that has been submitted to the scheduler.
     */
    private void showInstanceSubmitted(Token instance) {

        gui.showInstanceSubmitted(instance);
    }

    /**
     * Show instance retrieved on the GUI.
     *
     * @param instance the instance that has been retrieved from the scheduler.
     */
    private void showInstanceRetrieved(Token instance) {

        gui.showInstanceRetrieved(instance);
    }

    /**
     * Tell the GUI that the number of instances that the scheduler currently manages has changed.
     *
     * @param instances the instances
     */
    private void updateNumberOfInstances(int instances) {

        gui.updateNumberOfInstances(instances);
    }

    /**
     * Mark single instance. State changes for this instance will be shown explicitly in the GUI.
     *
     * @param instance the instance
     */
    public void markSingleInstance(Token instance) {

        this.instancesToTrack.add(instance);
    }
}
