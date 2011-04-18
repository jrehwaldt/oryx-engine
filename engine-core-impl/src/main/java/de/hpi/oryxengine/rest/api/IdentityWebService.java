package de.hpi.oryxengine.rest.api;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;



/**
 * The Class IdentityWebService.
 */
@Path("/identity")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public final class IdentityWebService {
    
    private final IdentityService identity;

    /**
     * Default constructor.
     */
    public IdentityWebService() {
        this.identity = ServiceFactory.getIdentityService();
        //DemoDataForWebservice.generate();
    }
    
    /**
     * Get all participants.
     * 
     * @return json
     */
    @Path("/participants")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AbstractParticipant> getParticipants() {
        
        Set<AbstractParticipant> participants = this.identity.getParticipants();
        return participants;
    
    }
    
    /**
     * Gets all roles.
     * 
     * @return json
     */
    @Path("/roles")
    @GET
    @Produces("text/plain")
    public Set<AbstractRole> getRoles() {
        
        Set<AbstractRole> roles = this.identity.getRoles();
        return roles;
    
    }

}
