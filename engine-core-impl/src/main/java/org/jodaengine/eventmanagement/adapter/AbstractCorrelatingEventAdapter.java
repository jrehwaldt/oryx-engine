package org.jodaengine.eventmanagement.adapter;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.EventRegistrar;
import org.jodaengine.eventmanagement.registration.ProcessEvent;
import org.jodaengine.eventmanagement.registration.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.registration.ProcessStartEvent;

public abstract class AbstractCorrelatingEventAdapter<Configuration extends AdapterConfiguration> extends AbstractEventAdapter<Configuration>
implements EventRegistrar, CorrelationManager {

    // Both lists are lazyInitialized
    private List<ProcessEvent> processEvents;
    private List<AdapterEvent> unCorrelatedAdapterEvents;

    public AbstractCorrelatingEventAdapter(CorrelationManager correlation, Configuration configuration) {

        super(correlation, configuration);
    }

    @Override
    public void registerStartEvent(ProcessStartEvent startEvent) {

        getProcessEvents().add(startEvent);
    }

    @Override
    public void registerIntermediateEvent(ProcessIntermediateEvent intermediateEvent) {

        getProcessEvents().add(intermediateEvent);
    }

    @Override
    public void correlate(AdapterEvent e) {

        // erst die Liste durchsuchen, wenn das nicht gefunden wurde dann schauen weglegen in die andere
        // Liste(ProcessEventsNotFound)
        for (ProcessEvent processEvent : getProcessEvents()) {
            
//        for (EventCondition condition : event.getConditions()) {
//            Method method = condition.getMethod();
//            Object returnValue = method.invoke(e);
//            if (!returnValue.equals(condition.getExpectedValue())) {
//                triggerEvent = false;
//                break;
//            }
//        }
//        if (triggerEvent) {
//            this.navigator.startProcessInstance(event.getDefinitionID(), event);
//            logger.info("Starting process {}", this.navigator);
//        }
        }

    }

    private List<ProcessEvent> getProcessEvents() {
    
        if (processEvents == null) {
            this.processEvents = new ArrayList<ProcessEvent>();
        }
        return processEvents;
    }

    private List<AdapterEvent> getUnCorrelatedAdapterEvents() {
    
        if (unCorrelatedAdapterEvents == null) {
            this.unCorrelatedAdapterEvents = new ArrayList<AdapterEvent>();
        }
        return unCorrelatedAdapterEvents;
    }
}
