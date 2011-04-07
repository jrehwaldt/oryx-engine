package de.hpi.oryxengine.worklist.gui.api;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
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

    /**
     * Default Constructor.
     */
    public NavigatorWebService() {

        nav = ServiceFactory.getNavigatorService();
    }

    /**
     * Gets the number of running process instances in the navigator.
     * 
     * @return the number of running process instances as string
     */
    @Path("/runninginstances")
    @GET
    @Produces("text/plain")
    public String runningInstances() {

        return String.valueOf(nav.getRunningInstances().size());
    }

    /**
     * Gets the number of finished instances in the navigator.
     * 
     * @return the number of finished instances as string
     */
    @Path("/endedinstances")
    @GET
    @Produces("text/plain")
    public String endedInstances() {

        return String.valueOf(nav.getEndedInstances().size());
    }

    /**
     * Starts a process instance according to the given process definition ID.
     * 
     * @param definitionID
     *            the id of the process definition to be instantiated and started
     */
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
