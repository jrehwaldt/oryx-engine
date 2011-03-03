package de.hpi.oryxengine.monitor;

import java.util.LinkedHashSet;

import de.hpi.oryxengine.plugin.scheduler.AbstractSchedulerListener;
import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class Monitor is a Plugin that receives Scheduler Events and forwards it to the provided GUI.
 */
public class Monitor extends AbstractSchedulerListener {

    private MonitorGUI gui;
    private LinkedHashSet<ProcessInstance> instancesToTrack;

    public Monitor(MonitorGUI gui) {

        this.gui = gui;
        this.instancesToTrack = new LinkedHashSet<ProcessInstance>();
    }

    @Override
    public void processInstanceSubmitted(int numberOfInstances, ProcessInstance processInstance) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(processInstance)) {
            showInstanceSubmitted(processInstance);
        }
    }

    @Override
    public void processInstanceRetrieved(int numberOfInstances, ProcessInstance processInstance) {

        updateNumberOfInstances(numberOfInstances);
        if (instancesToTrack.contains(processInstance)) {
            showInstanceRetrieved(processInstance);
        }
    }

    private void showInstanceSubmitted(ProcessInstance instance) {

        gui.showInstanceSubmitted(instance);
    }

    private void showInstanceRetrieved(ProcessInstance instance) {

        gui.showInstanceRetrieved(instance);
    }

    private void updateNumberOfInstances(int instances) {

        gui.updateNumberOfInstances(instances);
    }

    public void markSingleInstance(ProcessInstance instance) {

        this.instancesToTrack.add(instance);
    }
}
