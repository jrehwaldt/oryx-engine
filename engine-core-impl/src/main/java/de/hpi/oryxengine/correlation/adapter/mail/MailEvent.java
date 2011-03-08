package de.hpi.oryxengine.correlation.adapter.mail;

import java.util.UUID;

import de.hpi.oryxengine.correlation.EventType;

/**
 * Mail event type.
 */
public final class MailEvent implements EventType {
    
    /**
     * Hidden constructor.
     */
    MailEvent() {
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getID() {
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "mail";
    }

}
