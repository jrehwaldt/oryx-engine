package org.jodaengine.correlation.adapter.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.jodaengine.eventmanagement.CorrelationManager;
import org.jodaengine.eventmanagement.adapter.InboundPullAdapter;
import org.jodaengine.eventmanagement.adapter.mail.InboundImapMailAdapterImpl;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.adapter.mail.MailProtocol;

import org.jvnet.mock_javamail.Mailbox;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


/**
 * Tests the imap mail adapter.
 * 
 * @author Jan Rehwaldt
 */
public class ImapMailAdapterTest {
    
    private InboundPullAdapter adapter = null;
    private InboundMailAdapterConfiguration config = null;
    private String address = null;
    private CorrelationManager mock = null;
    
    /**
     * Setup test case.
     * 
     * @throws MessagingException thrown if the mock fails
     */
    @BeforeTest
    public void setUp() throws MessagingException {
        
        this.mock = mock(CorrelationManager.class);
        this.config = new InboundMailAdapterConfiguration(
            MailProtocol.IMAP,
// CHECKSTYLE:OFF
            "oryxengine",
            "dalmatina!",
// CHECKSTYLE:ON
            "imap.gmail.com",
            MailProtocol.IMAP.getPort(true),
            true
        );
        this.address = String.format("%s@%s", this.config.getUserName(), this.config.getAddress());
        this.adapter = new InboundImapMailAdapterImpl(this.mock, this.config);
        
        MimeMessage msg = new MimeMessage(Session.getInstance(this.config.toMailProperties()));
        msg.setRecipients(RecipientType.TO, this.address);
        msg.setFrom(new InternetAddress(this.address));
        msg.setText("Huhu");
        Transport.send(msg);
        
        assertEquals(Mailbox.get(this.address).size(), 1);
    }
    
    /**
     * Tests an imap pull.
     * 
     * @throws Exception test fails if thrown
     */
    @Test
    public void testImapPull() throws Exception {
        this.adapter.pull();
        ArgumentCaptor<MailAdapterEvent> event = ArgumentCaptor.forClass(MailAdapterEvent.class);
        verify(this.mock).correlate(event.capture());
        assertFalse(event.getValue() == null, "event should not be null");
    }
}
