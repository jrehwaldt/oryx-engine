package org.jodaengine.eventmanagement.subscription;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.jodaengine.process.token.BPMNToken;



/**
 * The Class TimerEventImpl.
 */
public class TimerEventImpl extends AbstractProcessEvent implements ProcessIntermediateEvent {

    private BPMNToken bPMNToken;

    /**
     * Instantiates a new timer event impl.
     *
     * @param config the config for the event, consists out of options for scheduling and for the adaptor.
     * @param bPMNToken the process token
     */
    public TimerEventImpl(AdapterConfiguration config,
                          BPMNToken bPMNToken) {

        super(EventTypes.Timer, config, new TrueEventCondition());
        this.bPMNToken = bPMNToken;
    }
    
    @Override
    public BPMNToken getToken() {

        return bPMNToken;
    }

    @Override
    public void trigger() {

        bPMNToken.resume();
    }
}
