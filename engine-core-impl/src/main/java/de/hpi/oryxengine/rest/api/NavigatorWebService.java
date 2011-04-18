package de.hpi.oryxengine.rest.api;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorStatistic;

/**
 * API servlet providing an interface for the navigator. It can be used to start/stop process instances.
 */
@Path("/navigator")
@Produces({ MediaType.APPLICATION_JSON })
public class NavigatorWebService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Navigator navigatorService;

    /**
     * Default Constructor.
     */
    public NavigatorWebService() {

        navigatorService = ServiceFactory.getNavigatorService();
    }

    /**
     * Starts a process instance according to the given process definition ID.
     * 
     * @param definitionID
     *            the id of the process definition to be instantiated and started
     */
    @Path("/instance/{definition-id}/start")
    @POST
    public void startInstance(@PathParam("definition-id") String definitionID) {

        UUID id = UUID.fromString(definitionID);
        try {
            navigatorService.startProcessInstance(id);
        } catch (DefinitionNotFoundException e) {
            logger.info("Definition with id {} not found", id.toString());
            e.printStackTrace();
        }
    }
    
    /**
     * Provides a statistic file with navigator information.
     * 
     * @return a statistic file
     */
    @Path("/statistic")
    @GET
    public NavigatorStatistic getStatistics() {
        return this.navigatorService.getStatistics();
    }
}
