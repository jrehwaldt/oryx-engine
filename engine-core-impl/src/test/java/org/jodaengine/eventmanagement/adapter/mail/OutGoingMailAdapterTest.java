/*
 * 
 */
package org.jodaengine.eventmanagement.adapter.mail;

import java.io.IOException;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.jvnet.mock_javamail.Mailbox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Testing the class Outgoing Mail Adapter Test.. i.e. whether it sends emails as we excpect it too.
 */
public class OutGoingMailAdapterTest {

    private OutgoingMailAdapter outgoingMailAdapter;
    private final static MailProtocol PROTOCOL = MailProtocol.SMTP;
    private final static String USER_NAME = "test";
    private final static String PASSWORD = "toor";
    private final static String ADDRESS = "example.com";
    private final static String SMTP_SERVER = "localhost";
    private final static String RECEIPENT = "testie@test.de";
    private final static String OTHER_RECEIPENT = "other@receipent.org";
    private final static String SUBJECT = "Test me!";
    private final static String MESSAGE = "A wonderful test this is.";
    private final static int SMTP_PORT = 25;
    
    
    /**
     * Create our adapter which is going to be tested.
     */
    @BeforeClass
    public void setUp() {
        OutgoingMailAdapterConfiguration outgoingMailAdapterConfiguration = 
            new OutgoingMailAdapterConfiguration(PROTOCOL, USER_NAME, PASSWORD, ADDRESS, SMTP_SERVER, SMTP_PORT);
        outgoingMailAdapter = new OutgoingMailAdapter(outgoingMailAdapterConfiguration);
    }
    
    /**
     * Test sending email with default subject (the simpler interface).
     *
     * @throws MessagingException the messaging exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testSendingEmailWithDefaultSubject() throws MessagingException, IOException {
        outgoingMailAdapter.sendMessage(RECEIPENT, MESSAGE);
        
        Mailbox mailbox = Mailbox.get(RECEIPENT);
        Assert.assertEquals(mailbox.size(), 1);
        Message message = mailbox.get(0);
        Assert.assertEquals(message.getSubject(), OutgoingMailAdapter.DEFAULT_SUBJECT);
        Assert.assertEquals(message.getContent().toString(), MESSAGE);
    }
    
    /**
     * Test simply sending an email and afterwards retrievein it.
     * @throws MessagingException 
     * @throws IOException 
     */
    @Test
    public void testSimpleSend() throws MessagingException, IOException {
        outgoingMailAdapter.sendMessage(RECEIPENT, SUBJECT, MESSAGE);
        
        Mailbox mailbox = Mailbox.get(RECEIPENT);
        Assert.assertEquals(mailbox.size(), 1);
        Message message = mailbox.get(0);
        Assert.assertEquals(message.getSubject(), SUBJECT);
        Assert.assertEquals(message.getContent().toString(), MESSAGE);
    }
    
    /**
     * Test sending two emails.
     *
     * @throws AddressException the address exception
     */
    @Test
    public void testSendTwoEmails() throws AddressException {
        outgoingMailAdapter.sendMessage(RECEIPENT, SUBJECT, MESSAGE);
        outgoingMailAdapter.sendMessage(RECEIPENT, "another subject", "Another message");
        
        Assert.assertEquals(Mailbox.get(RECEIPENT).size(), 2);
    }
    
    /**
     * Test send two emails to different receipents (1mail/receiepent).
     *
     * @throws AddressException the address exception
     */
    @Test
    public void testSendTwoEmailsToDifferentReceipents() throws AddressException {
        outgoingMailAdapter.sendMessage(RECEIPENT, MESSAGE);
        outgoingMailAdapter.sendMessage(OTHER_RECEIPENT, "Hello other guy!");
        
        Assert.assertEquals(Mailbox.get(RECEIPENT).size(), 1);
        Assert.assertEquals(Mailbox.get(OTHER_RECEIPENT).size(), 1);
    }
    
    /**
     * Clean up the mailbox after every test so we are good to go from the start.
     */
    @AfterMethod
    public void cleanUp() {
        Mailbox.clearAll();
    }
}
