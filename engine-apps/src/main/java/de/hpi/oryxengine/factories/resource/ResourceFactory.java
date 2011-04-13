package de.hpi.oryxengine.factories.resource;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.IdentityBuilder;

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
     * @param id participant id
     * @param name participant name
     * @return the participant
     */
    public static AbstractParticipant createParticipant(String id, String name) {
        
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant(id).setName(name);
        
        return participant;
    }
}
