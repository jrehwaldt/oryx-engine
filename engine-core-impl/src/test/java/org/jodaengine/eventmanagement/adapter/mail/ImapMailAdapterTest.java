package org.jodaengine.eventmanagement.adapter.mail;

import static org.testng.Assert.assertEquals;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jvnet.mock_javamail.Mailbox;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the {@link IncomingImapMailAdapter imap mail adapter}.
 * which provides methods for testing.
 */
public class ImapMailAdapterTest {

    private IncomingImapMailAdapter inboundImapAdapterSpy;
    private IncomingMailAdapterConfiguration config;
    private String address;

    /**
     * Setup test case.
     * 
     * @throws MessagingException
     *             thrown if the mock fails
     */
    @BeforeTest
    public void setUp()
    throws MessagingException {

        preparingInboundImapAdapter();

        sendingTestEmail();

        assertEquals(Mailbox.get(this.address).size(), 1);
    }

    /**
     * Tests an imap pull.
     * 
     * @throws Exception
     *             test fails if thrown
     */
    @Test
    public void testImapPull()
    throws Exception {

        inboundImapAdapterSpy.pull();

        
        ArgumentCaptor<MailAdapterEvent> event = ArgumentCaptor.forClass(MailAdapterEvent.class);
        Mockito.verify(inboundImapAdapterSpy).correlate(event.capture());
        Assert.assertNotNull(event.getValue(), "event should not be null");
        Assert.assertEquals(event.getValue().getAdapterType(), EventTypes.Mail);
    }

    /**
     * Sends a test email which is catched by the javaxMailMock-Lib.
     *
     * @throws MessagingException the messaging exception
     */
    private void sendingTestEmail()
    throws MessagingException {

        this.address = String.format("%s@%s", this.config.getUserName(), this.config.getDomainName());
        MimeMessage msg = new MimeMessage(Session.getInstance(this.config.toMailProperties()));
        msg.setRecipients(RecipientType.TO, this.address);
        msg.setFrom(new InternetAddress(this.address));
        msg.setText("Huhu");
        Transport.send(msg);
    }

    /**
     * Prepares the InboundImapAdapter.
     */
    private void preparingInboundImapAdapter() {

        this.config = IncomingMailAdapterConfiguration.jodaGoogleConfiguration();
        this.inboundImapAdapterSpy = Mockito.spy(new IncomingImapMailAdapter(this.config));
    }
}
