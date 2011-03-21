package de.hpi.oryxengine.factory.resource;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;

/**
 * Little factory for creating Resources. A short cut for in the implmentation.
 */
public class ResourceFactory {

    public static Participant createParticipant(String id, String name) {
        
        IdentityBuilder identityBuilder = ServiceFactory.getIdentityService().getIdentityBuilder();
        Participant participant = identityBuilder.createParticipant(id).setName(name);
        
        return participant;
    }
}
