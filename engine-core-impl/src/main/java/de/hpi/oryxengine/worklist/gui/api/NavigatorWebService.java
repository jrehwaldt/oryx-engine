package de.hpi.oryxengine.worklist.gui.api;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.OryxEngineAppContext;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.navigator.Navigator;

/**
 * API servlet providing an interface for the navigator. It can be used to start/stop process instances.
 */
@Path("/navigator")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class NavigatorWebService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Navigator nav;
    
    
    public NavigatorWebService() {
        nav = (Navigator) OryxEngineAppContext.getBean("navigatorService");
    }
    @Path("/runninginstances")
    @GET
    @Produces("text/plain")
    public String runningInstances() {

        return String.valueOf(nav.getRunningInstances().size());
    }

    @Path("/endedinstances")
    @GET
    @Produces("text/plain")
    public String endedInstances() {

        return String.valueOf(nav.getEndedInstances().size());
    }

    @Path("/start/{definition-id}")
    @GET
    public void startInstance(@PathParam("definition-id") String definitionID) {

        UUID id = UUID.fromString(definitionID);
        try {
            nav.startProcessInstance(id);
        } catch (DefinitionNotFoundException e) {
            logger.info("Definition with id {} not found", id.toString());
            e.printStackTrace();
        }
    }

}
