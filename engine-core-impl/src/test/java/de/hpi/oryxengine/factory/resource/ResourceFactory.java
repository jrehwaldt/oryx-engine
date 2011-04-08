package de.hpi.oryxengine.factory.resource;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.ParticipantImpl;

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
    public static ParticipantImpl createParticipant(String id, String name) {
        
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        ParticipantImpl participant = identityBuilder.createParticipant(id).setName(name);
        
        return participant;
    }
}
