package org.jodaengine.factory.resource;

import org.jodaengine.ServiceFactory;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.IdentityBuilder;


/**
 * Little factory for creating Resources. A short cut for in the implementation.
 */
public class ResourceFactory {
    
    /**
     * Hidden constructor.
     */
    protected ResourceFactory() {
        
    }
    
    /**
     * Creates a participant.
     * 
     * @param name participant name
     * @return the participant
     */
    public static AbstractParticipant createParticipant(String name) {
        
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant(name);
        
        return participant;
    }
}
