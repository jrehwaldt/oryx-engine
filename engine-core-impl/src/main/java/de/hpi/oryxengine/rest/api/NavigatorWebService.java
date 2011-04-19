package de.hpi.oryxengine.rest.api;

import java.util.List;
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
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.token.Token;

/**
 * API servlet providing an interface for the navigator. It can be used to start/stop process instances.
 */
@Path("/navigator")
@Produces({ MediaType.APPLICATION_JSON })
public class NavigatorWebService implements Navigator {
    
    private static final String NOT_ACCESSIBLE_VIA_WEBSERVICE = "This method is not accessible via web service.";
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Navigator navigatorService;

    /**
     * Default Constructor.
     */
    public NavigatorWebService() {
        logger.info("NavigatorWebService is initializing");
        navigatorService = ServiceFactory.getNavigatorService();
    }
    
    @Path("/status/statistic")
    @GET
    @Override
    public NavigatorStatistic getStatistics() {
        return this.navigatorService.getStatistics();
    }

    /**
     * Starts a process instance according to the given process definition ID.
     * 
     * TODO version 2.0.1.GA of RESTeasy does not support UUID, version 2.2-beta-1 works, but has a problem with Jackson
     * Remove this method once RESTeasy 2.2 is stable or beta-2 fixed this problem.
     * 
     * @param definitionID
     *            the id of the process definition to be instantiated and started
     * @throws DefinitionNotFoundException
     *            thrown if the process definition is not found
     * @return returns the created instance
     */
    @Path("/process_alt/{definition-id}/start")
    @POST
    public ProcessInstance startProcessInstance(@PathParam("definition-id") String definitionID)
    throws DefinitionNotFoundException {
        
        return startProcessInstance(UUID.fromString(definitionID));
    }

//    @Path("/process/{definition-id}/start")
//    @GET
    @Override
    public ProcessInstance startProcessInstance(@PathParam("definition-id") UUID definitionID)
    throws DefinitionNotFoundException {
        
        return navigatorService.startProcessInstance(definitionID);
    }
    
    @Path("/status/is-idle")
    @GET
    @Override
    public boolean isIdle() {

        return this.navigatorService.isIdle();
    }


    @Override
    public List<ProcessInstance> getRunningInstances() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProcessInstance> getEndedInstances() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void start() {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void stop() {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void signalEndedProcessInstance(ProcessInstance instance) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public ProcessInstance startProcessInstance(UUID processID, StartEvent event)
    throws DefinitionNotFoundException {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void addThread() {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
    
    @Override
    public void addWorkToken(Token t) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void addSuspendToken(Token t) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void removeSuspendToken(Token t) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
}
