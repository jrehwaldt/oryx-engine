package de.hpi.oryxengine.rest.api;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.IdentityBuilderImpl;



/**
 * The Class IdentityWebService.
 */
@Path("/identity")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public final class IdentityWebService {
    
    private final IdentityService identity;
    private final Logger logger = LoggerFactory.getLogger(getClass());

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
     * Creates a participant with a given name.
     */
    @Path("/participants")
    @POST
    @Consumes("text/plain")
    public void createParticipant(String participantName) {
        // TODO ask Gerardo, why we need the Impl here/why the Impl has methods that are not specified in the interface.
        logger.debug("Creating participant called {}", participantName);
        IdentityServiceImpl identityServiceImpl = (IdentityServiceImpl) identity;
        
        IdentityBuilder builder = new IdentityBuilderImpl(identityServiceImpl);
        builder.createParticipant(participantName);
    
        
    }
    
    /**
     * Gets all roles.
     * 
     * @return json
     */
    @Path("/roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<AbstractRole> getRoles() {
        
        Set<AbstractRole> roles = this.identity.getRoles();
        return roles;
    
    }

}
