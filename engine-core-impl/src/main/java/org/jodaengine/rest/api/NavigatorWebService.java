package org.jodaengine.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.eventmanagement.processevent.incoming.intermediate.ProcessStartEvent;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorStatistic;
import org.jodaengine.navigator.schedule.Scheduler;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * API providing an interface for the {@link Navigator}. It can be used to start/stop process instances.
 */
@Path("/navigator")
@Produces({ MediaType.APPLICATION_JSON })
public class NavigatorWebService implements Navigator {

    private static final String NOT_ACCESSIBLE_VIA_WEBSERVICE = "This method is not accessible via web service.";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Navigator navigatorService;

    /**
     * Default Constructor.
     *
     * @param engineServices the engine services
     */
    public NavigatorWebService(JodaEngineServices engineServices) {

        logger.info("NavigatorWebService is initializing");
        navigatorService = engineServices.getNavigatorService();
    }

    @Path("/status/statistic")
    @GET
    @Override
    public NavigatorStatistic getStatistics() {

        return this.navigatorService.getStatistics();
    }

    @Override
    public AbstractProcessInstance startProcessInstance(ProcessDefinitionID definitionId)
    throws DefinitionNotFoundException {

        return navigatorService.startProcessInstance(definitionId);
    }

    /**
     * Starts a process instance according to the given process definition ID.
     * 
     * TODO version 2.0.1.GA of RESTeasy does not support UUID, version 2.2-beta-1 works, but has a problem with Jackson
     *      Remove this method once RESTeasy 2.2 is stable or beta-2? fixed this problem.
     * 
     * @param definitionID
     *            the id of the process definition to be instantiated and started
     * @throws DefinitionNotFoundException
     *             thrown if the process definition is not found
     * @return returns the created instance
     */
    @Path("/process-definitions/{definitionId}/start")
    @POST
    public AbstractProcessInstance startProcessInstance(@PathParam("definitionId") String definitionID)
    throws DefinitionNotFoundException {

        return startProcessInstance(ProcessDefinitionID.fromString(definitionID));
    }

    @Path("/status/is-idle")
    @GET
    @Override
    public boolean isIdle() {

        return this.navigatorService.isIdle();
    }

    @Path("/status/running-instances")
    @GET
    @Override
    public List<AbstractProcessInstance> getRunningInstances() {

        return this.navigatorService.getRunningInstances();
    }

    @Path("/status/finished-instances")
    @GET
    @Override
    public List<AbstractProcessInstance> getEndedInstances() {

        return this.navigatorService.getEndedInstances();
    }
    
    @Override
    public boolean isRunning() {
        return this.navigatorService.isRunning();
    }

    @Override
    public void start(JodaEngineServices services) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void stop() {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public void cancelProcessInstance(AbstractProcessInstance instance) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
        
    }

    @Override
    public void signalEndedProcessInstance(AbstractProcessInstance instance) {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }

    @Override
    public AbstractProcessInstance startProcessInstance(ProcessDefinitionID processID, ProcessStartEvent event)
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

    @Override
    public void removeTokenFromScheduler(Token t) {

        this.navigatorService.removeTokenFromScheduler(t);
        
    }

    @Override
    public Scheduler getScheduler() {

        throw new UnsupportedOperationException(NOT_ACCESSIBLE_VIA_WEBSERVICE);
    }
}
