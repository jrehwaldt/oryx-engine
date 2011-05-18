package org.jodaengine.eventmanagement.adapter;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.EventRegistrar;
import org.jodaengine.eventmanagement.registration.ProcessIntermediateEvent;
import org.jodaengine.eventmanagement.registration.ProcessStartEvent;

public abstract class AbstractCorrelatingEventAdapter<Configuration extends AdapterConfiguration> extends AbstractEventAdapter<Configuration>
implements EventRegistrar, CorrelationManager {

    // Hier kommen die ganzen Listen hin

    public AbstractCorrelatingEventAdapter(CorrelationManager correlation, Configuration configuration) {

        super(correlation, configuration);
    }

    @Override
    public void registerStartEvent(ProcessStartEvent event) {

        // TODO Auto-generated method stub
        
    }

    @Override
    public String registerIntermediateEvent(ProcessIntermediateEvent event) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void correlate(AdapterEvent e) {

        // TODO Auto-generated method stub
        // erst die Liste durchsuchen, wenn das nicht gefunden wurde dann schauen weglegen in die andere Liste(ProcessEventsNotFound)
        
    }
}
