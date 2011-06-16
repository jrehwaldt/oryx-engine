package org.jodaengine.eventmanagement.processevent.incoming.start;

import java.util.UUID;

import javax.mail.MessagingException;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.eventmanagement.EventManager;
import org.jodaengine.eventmanagement.adapter.AbstractCorrelatingEventAdapter;
import org.jodaengine.eventmanagement.adapter.EventType;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.configuration.AdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.adapter.mail.MailProtocol;
import org.jodaengine.eventmanagement.processevent.incoming.ProcessStartEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.start.DefaultProcessStartEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.factory.definition.SimpleProcessDefinitionFactory;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class EventRegistrationAndEvaluationTest.
 */
public class MailStartEventSubscriptionAndCorrelationTest extends AbstractJodaEngineTest {

    private ProcessStartEvent event, anotherEvent;
    private MailAdapterEvent incomingAdapterEvent, anotherIncomingAdapterEvent;
    private static final int MAIL_PORT = 25;
    private IncomingMailAdapterConfiguration config;
    private IncomingMailAdapterConfiguration anotherConfig;
    private EventManager eventManager;
    private Navigator navigatorServiceSpy;
    private ProcessDefinitionID definitionID;

    /**
     * Class initialization.
     *
     * @throws NoSuchMethodException unlikely to be thrown...
     * @throws IllegalStarteventException test will fail
     * @throws MessagingException the messaging exception
     */
    @BeforeMethod
    public void beforeMethod()
    throws NoSuchMethodException, IllegalStarteventException, MessagingException {
    
        eventManager = (EventManager) ServiceFactory.getEventManagerService();
    
        // Deploy the process
        RepositoryService repo = ServiceFactory.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();
        definitionID = new ProcessDefinitionID(UUID.randomUUID().toString(), 0);
        ProcessDefinition processDefinition = new SimpleProcessDefinitionFactory().create(definitionID);
        deploymentBuilder.addProcessDefinition(processDefinition);
        repo.deployInNewScope(deploymentBuilder.buildDeployment());
    
        // Spying on the navigatorService
        navigatorServiceSpy = Mockito.spy(jodaEngineServices.getNavigatorService());
    
        // register some processStartEvents
        // register first processStartEvent
        EventType mailType = EventTypes.Mail;
        EventCondition subjectCondition = new MethodInvokingEventCondition(MailAdapterEvent.class, "getMessageSubject",
            "Hallo");
        config = IncomingMailAdapterConfiguration.jodaGoogleConfiguration();
        event = new DefaultProcessStartEvent(config, subjectCondition, definitionID);
    
        // register another processStartEvent
        anotherConfig = new IncomingMailAdapterConfiguration(MailProtocol.IMAP, "horst", "kevin", "imap.horst.de",
            MAIL_PORT, false);
        anotherEvent = new DefaultProcessStartEvent(anotherConfig, subjectCondition, definitionID);
        anotherEvent.injectNavigatorService(navigatorServiceSpy);
    
        // create some incoming events, for example from a mailbox
        incomingAdapterEvent = Mockito.mock(MailAdapterEvent.class);
        Mockito.when(incomingAdapterEvent.getAdapterType()).thenReturn(mailType);
        Mockito.when(incomingAdapterEvent.getMessageSubject()).thenReturn("Hallo");
        Mockito.when(incomingAdapterEvent.getAdapterConfiguration()).thenReturn(config);
    
        anotherIncomingAdapterEvent = Mockito.mock(MailAdapterEvent.class);
        Mockito.when(anotherIncomingAdapterEvent.getAdapterType()).thenReturn(mailType);
        Mockito.when(anotherIncomingAdapterEvent.getMessageSubject()).thenReturn("HalliHallo");
        Mockito.when(anotherIncomingAdapterEvent.getAdapterConfiguration()).thenReturn(config);
    
    }

    /**
     * Tests that a process instance is started.
     * 
     * @throws DefinitionNotFoundException
     *             fails
     */
    @Test
    public void shouldAttemptToStartTheSimpleProcessInstance()
    throws DefinitionNotFoundException {
    
        eventManager.registerStartEvent(event);
        event.injectNavigatorService(navigatorServiceSpy);
    
        getCorrelatingAdapter(config).correlate(incomingAdapterEvent);
    
        // we use eq(...) because if you use mockito matchers as the parameters, all parameters have to be matchers.
        Mockito.verify(navigatorServiceSpy).startProcessInstance(Mockito.eq(definitionID), Mockito.eq(event));
    }

    /**
     * Tests that no process is invoked on wrong event.
     * 
     * @throws DefinitionNotFoundException
     *             fails
     */
    @Test
    public void shouldNotAttemptToStartTheSimpleProcessInstance()
    throws DefinitionNotFoundException {

        eventManager.registerStartEvent(event);
        event.injectNavigatorService(navigatorServiceSpy);

        getCorrelatingAdapter(config).correlate(anotherIncomingAdapterEvent);

        Mockito.verify(navigatorServiceSpy, Mockito.never()).startProcessInstance(
            Mockito.any(ProcessDefinitionID.class), Mockito.any(ProcessStartEvent.class));
    }

    /**
     * Test that two similar start events with different configurations work appropriately. That is the process is just
     * called one time.
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    @Test
    public void testTwoSimilarEventsWithDiferrentConfig()
    throws DefinitionNotFoundException {

        eventManager.registerStartEvent(event);
        event.injectNavigatorService(navigatorServiceSpy);
        eventManager.registerStartEvent(anotherEvent);
        anotherEvent.injectNavigatorService(navigatorServiceSpy);

        getCorrelatingAdapter(config).correlate(incomingAdapterEvent);

        Mockito.verify(navigatorServiceSpy).startProcessInstance(Mockito.eq(definitionID), Mockito.eq(event));
    }

    /**
     * Returns the {@link AbstractCorrelatingEventAdapter} that belongs to that {@link AdapterConfiguration}.
     * 
     * @param config
     *            - the {@link AdapterConfiguration}
     * @return the {@link AbstractCorrelatingEventAdapter} that belongs to that {@link AdapterConfiguration}
     */
    private AbstractCorrelatingEventAdapter<? extends AdapterConfiguration> 
        getCorrelatingAdapter(AdapterConfiguration config) {

        return (AbstractCorrelatingEventAdapter<?>) eventManager.getEventAdapters().get(config);
    }
}
