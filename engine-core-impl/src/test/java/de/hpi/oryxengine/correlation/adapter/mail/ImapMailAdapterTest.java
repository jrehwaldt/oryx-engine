package de.hpi.oryxengine.correlation.adapter.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.jvnet.mock_javamail.Mailbox;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.CorrelationManager;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * Tests the imap mail adapter.
 * 
 * @author Jan Rehwaldt
 */
public class ImapMailAdapterTest {
    
    /** The imap. */
    private PullingInboundAdapter imap = null;
    
    /** The config. */
    private MailAdapterConfiguration config = null;
    
    /** The address. */
    private String address = null;
    
    /** The mock. */
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
            MailType.IMAP,
            "oryxengine",
            "dalmatina!",
            "imap.gmail.com",
            MailType.IMAP.getPort(true),
            true
        );
        this.address = String.format("%s@%s", this.config.getUserName(), this.config.getAddress());
        this.imap = new InboundImapMailAdapterImpl(this.mock, this.config);
        
        MimeMessage msg = new MimeMessage(Session.getInstance(this.config.toMailProperties()));
        msg.setRecipients(RecipientType.TO, this.address);
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
        this.imap.pull();
        verify(this.mock).correlate(null);
    }
}
