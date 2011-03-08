package de.hpi.oryxengine.correlation.adapter.mail;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.EventType;
import de.hpi.oryxengine.correlation.adapter.AdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * This is the default imap mail client implementation
 * and acts as {@link CorrelationAdapter} for the {@link CorrelationManager}.
 */
public class InboundImapMailAdapterImpl
implements PullingInboundAdapter {
    
    private final AdapterConfiguration configuration;
    
    private final EventType type;
    
    /**
     * Default constructor.
     * 
     * @param configuration the adapter's configuration
     */
    public InboundImapMailAdapterImpl(@Nonnull AdapterConfiguration configuration) {
        this.configuration = configuration;
        this.type = new MailEvent();
    }
    
    @Override
    public EventType getEventType() {
        return this.type;
    }
    
    @Override
    public @Nonnull AdapterConfiguration getConfiguration() {
        return this.configuration;
    }
}
