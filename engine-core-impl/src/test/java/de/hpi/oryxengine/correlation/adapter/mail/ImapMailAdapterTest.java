package de.hpi.oryxengine.correlation.adapter.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.jvnet.mock_javamail.Mailbox;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;

/**
 * Tests the imap mail adapter.
 * 
 * @author Jan Rehwaldt
 */
public class ImapMailAdapterTest {
    
    private InboundPullAdapter adapter = null;
    private MailAdapterConfiguration config = null;
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
        this.config = new MailAdapterConfiguration(
            MailProtocol.IMAP,
            "oryxengine",
            "dalmatina!",
            "imap.gmail.com",
            MailProtocol.IMAP.getPort(true),
            true
        );
        this.address = String.format("%s@%s", this.config.getUserName(), this.config.getAddress());
        this.adapter = new InboundImapMailAdapterImpl(this.mock, this.config);
        
        MimeMessage msg = new MimeMessage(Session.getInstance(this.config.toMailProperties()));
        msg.setRecipients(RecipientType.TO, this.address);
        msg.setFrom();
        msg.setText("Huhu");
        Transport.send(msg);
        
        assertEquals(Mailbox.get(this.address).size(), 1);
    }
    
    /**
     * Tests a imap pull.
     * 
     * @throws Exception if thrown, test fails
     */
    @Test
    public void testImapPull() throws Exception {
        this.adapter.pull();
        ArgumentCaptor<MailAdapterEvent> event = ArgumentCaptor.forClass(MailAdapterEvent.class);
        verify(this.mock).correlate(event.capture());
        assertFalse(event.getValue() == null, "event should not be null");
    }
}
