package de.hpi.oryxengine.correlation.registration;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.RepositoryServiceImpl;
import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.adapter.EventType;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.adapter.mail.MailProtocol;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.RepositorySetup;
import de.hpi.oryxengine.util.testing.AbstractJodaEngineTest;

/**
 * The Class EventRegistrationAndEvaluationTest.
 */
public class EventRegistrationAndEvaluationTest extends AbstractJodaEngineTest {

    private StartEvent event, anotherEvent;
    private MailAdapterEvent incomingEvent, anotherIncomingEvent;
    public static final int MAIL_PORT = 25;

    /**
     * Tests that a process instance is started.
     * 
     * @throws DefinitionNotFoundException
     *             fails
     */
    @Test
    public void shouldAttemptToStartTheSimpleProcessInstance()
    throws DefinitionNotFoundException {

        Navigator navigator = mock(Navigator.class);
        CorrelationManagerImpl correlation = new CorrelationManagerImpl(navigator);
        correlation.registerStartEvent(event);

        correlation.correlate(incomingEvent);

        // we use eq(...) because if you use mockito matchers as the parameters, all parameters have to be matchers.
        verify(navigator).startProcessInstance(eq(RepositoryServiceImpl.SIMPLE_PROCESS_ID), any(StartEvent.class));
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

        Navigator navigator = mock(Navigator.class);
        CorrelationManagerImpl correlation = new CorrelationManagerImpl(navigator);
        correlation.registerStartEvent(event);

        correlation.correlate(anotherIncomingEvent);

        verify(navigator, never()).startProcessInstance(eq(RepositoryServiceImpl.SIMPLE_PROCESS_ID),
            any(StartEvent.class));
    }

    /**
     * Test that two similar start events with diferrent configurations work appropiately. That is the process is just
     * called one time.
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    @Test
    public void testTwoSimilarEventsWithDiferrentConfig()
    throws DefinitionNotFoundException {

        Navigator navigator = mock(Navigator.class);
        CorrelationManagerImpl correlation = new CorrelationManagerImpl(navigator);
        correlation.registerStartEvent(event);
        correlation.registerStartEvent(anotherEvent);

        correlation.correlate(incomingEvent);

        verify(navigator, times(1)).startProcessInstance(eq(RepositoryServiceImpl.SIMPLE_PROCESS_ID),
            any(StartEvent.class));
    }

    /**
     * Class initialization.
     * 
     * @throws NoSuchMethodException
     *             unlikely to be thrown...
     * @throws IllegalStarteventException
     *             test will fail
     */
    @BeforeClass
    public void beforeClass()
    throws NoSuchMethodException, IllegalStarteventException {

        RepositorySetup.fillRepository();
        // register some events
        UUID definitionID = RepositoryServiceImpl.SIMPLE_PROCESS_ID;
        EventType mailType = EventTypes.Mail;

        EventCondition subjectCondition = new EventConditionImpl(MailAdapterEvent.class.getMethod("getMessageTopic"),
            "Hallo");

        List<EventCondition> conditions1 = new ArrayList<EventCondition>();
        conditions1.add(subjectCondition);

        // Mockito isnt able to mock final classes so the next line doesnt work :(
        // MailAdapterConfiguration config = mock(MailAdapterConfiguration.class);
        MailAdapterConfiguration config = MailAdapterConfiguration.dalmatinaGoogleConfiguration();
        event = new StartEventImpl(mailType, config, conditions1, definitionID);

        MailAdapterConfiguration anotherConfig = new MailAdapterConfiguration(MailProtocol.IMAP, "horst", "kevin",
            "imap.horst.de", MAIL_PORT, false);
        anotherEvent = new StartEventImpl(mailType, anotherConfig, conditions1, definitionID);

        Method method = MailAdapterEvent.class.getMethod("getAdapterConfiguration");
        method.setAccessible(true);

        // create some incoming events, for example from a mailbox
        incomingEvent = mock(MailAdapterEvent.class);
        when(incomingEvent.getAdapterType()).thenReturn(mailType);
        when(incomingEvent.getMessageTopic()).thenReturn("Hallo");
        when(incomingEvent.getAdapterConfiguration()).thenReturn(config);

        anotherIncomingEvent = mock(MailAdapterEvent.class);
        when(anotherIncomingEvent.getAdapterType()).thenReturn(mailType);
        when(anotherIncomingEvent.getMessageTopic()).thenReturn("HalliHallo");
        when(anotherIncomingEvent.getAdapterConfiguration()).thenReturn(config);
    }

    /**
     * Tear down.
     * 
     * @throws SchedulerException
     *             tear down fails
     */
    @AfterMethod
    public void flushJobRepository()
    throws SchedulerException {

        new StdSchedulerFactory().getScheduler().shutdown();
    }

}
