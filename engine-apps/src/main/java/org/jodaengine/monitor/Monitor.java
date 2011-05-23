package org.jodaengine.monitor;

import java.util.HashSet;
import java.util.Set;

import org.jodaengine.ext.listener.AbstractSchedulerListener;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.BPMNToken;

/**
 * The Class Monitor is a Listener that receives Scheduler Events and forwards it to the provided GUI.
 */
public class Monitor extends AbstractSchedulerListener {

    private MonitorGUI gui;
    private Set<AbstractProcessInstance> instancesToTrack;

    /**
     * Instantiates a new monitor. This plugin listens for Scheduler events, such as a new instance that is scheduled,
     * etc.
     * 
     * @param gui
     *            the gui to inform about events.
     */
    public Monitor(MonitorGUI gui) {

        this.gui = gui;
        this.instancesToTrack = new HashSet<AbstractProcessInstance>();
    }

    @Override
    public void processInstanceSubmitted(int numberOfInstances, BPMNToken bPMNToken) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(bPMNToken.getInstance())) {
            showInstanceSubmitted(bPMNToken);
        }
    }

    @Override
    public void processInstanceRetrieved(int numberOfInstances, BPMNToken bPMNToken) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(bPMNToken.getInstance())) {
            showInstanceRetrieved(bPMNToken);
        }
    }

    /**
     * Show instance submitted on the GUI.
     * 
     * @param instance
     *            the instance that has been submitted to the scheduler.
     */
    private void showInstanceSubmitted(BPMNToken instance) {

        gui.showInstanceSubmitted(instance);
    }

    /**
     * Show instance retrieved on the GUI.
     * 
     * @param instance
     *            the instance that has been retrieved from the scheduler.
     */
    private void showInstanceRetrieved(BPMNToken instance) {

        gui.showInstanceRetrieved(instance);
    }

    /**
     * Tell the GUI that the number of instances that the scheduler currently manages has changed.
     * 
     * @param instances
     *            the instances
     */
    private void updateNumberOfInstances(int instances) {

        gui.updateNumberOfInstances(instances);
    }

    /**
     * Mark single instance. State changes for this instance will be shown explicitly in the GUI.
     * 
     * @param processInstance
     *            the instance
     */
    public void markSingleInstance(AbstractProcessInstance processInstance) {

        this.instancesToTrack.add(processInstance);
    }
}
