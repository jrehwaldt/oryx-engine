package de.hpi.oryxengine.correlation.registration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.EventType;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.adapter.mail.MailEvent;
import de.hpi.oryxengine.correlation.adapter.mail.MailType;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.repository.ProcessRepositoryImpl;
import de.hpi.oryxengine.repository.RepositorySetup;

/**
 * The Class EventRegistrationAndEvaluationTest.
 */
public class EventRegistrationAndEvaluationTest {

    private StartEvent event;
    private MailAdapterEvent incomingEvent, incomingEvent2;

    @Test
    public void shouldAttemptToStartTheSimpleProcessInstance()
    throws Exception {

        Navigator navigator = mock(Navigator.class);
        CorrelationManagerImpl correlation = new CorrelationManagerImpl(navigator);
        correlation.registerStartEvent(event);
        
        correlation.correlate(incomingEvent);
        
        verify(navigator).startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }
    
    @Test
    public void shouldNotAttemptToStartTheSimpleProcessInstance()
    throws Exception {

        Navigator navigator = mock(Navigator.class);
        CorrelationManagerImpl correlation = new CorrelationManagerImpl(navigator);
        correlation.registerStartEvent(event);
        
        correlation.correlate(incomingEvent2);
        
        verify(navigator, never()).startProcessInstance(ProcessRepositoryImpl.SIMPLE_PROCESS_ID);
    }

    @BeforeClass
    public void beforeClass()
    throws SecurityException, NoSuchMethodException {

        RepositorySetup.fillRepository();
        // register some events
        UUID defintionID = ProcessRepositoryImpl.SIMPLE_PROCESS_ID;
        EventType mailType = new MailEvent();

        EventCondition subjectCondition = new EventConditionImpl(MailAdapterEvent.class.getMethod("getMessag132eTopic"),
            "Hallo");

        List<EventCondition> conditions1 = new ArrayList<EventCondition>();
        conditions1.add(subjectCondition);

        // Mockito isnt able to mock final classes so the next line doesnt work :(
        // MailAdapterConfiguration config = mock(MailAdapterConfiguration.class);
        MailAdapterConfiguration config = new MailAdapterConfiguration(MailType.IMAP, "oryxengine", "dalmatina!",
            "imap.gmail.com", MailType.IMAP.getPort(true), true);
        event = new StartEventImpl(mailType, config, conditions1, defintionID);

        // create some incoming events, for example from a mailbox
        incomingEvent = mock(MailAdapterEvent.class);
        when(incomingEvent.getEventType()).thenReturn(mailType);
        when(incomingEvent.getMessageTopic()).thenReturn("Hallo");
        
        incomingEvent2 = mock(MailAdapterEvent.class);
        when(incomingEvent2.getEventType()).thenReturn(mailType);
        when(incomingEvent2.getMessageTopic()).thenReturn("HalliHallo");
    }

}
