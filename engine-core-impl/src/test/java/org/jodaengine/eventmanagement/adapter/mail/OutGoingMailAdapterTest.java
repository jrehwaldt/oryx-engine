/*
 * 
 */
package org.jodaengine.eventmanagement.adapter.mail;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.jodaengine.eventmanagement.adapter.AddressableMessage;
import org.jodaengine.eventmanagement.adapter.AddressableMessageImpl;
import org.jodaengine.eventmanagement.adapter.AddressableMessageWithSubject;
import org.jodaengine.eventmanagement.adapter.AddressableMessageWithSubjectImpl;
import org.jvnet.mock_javamail.Mailbox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Testing the class Outgoing Mail Adapter Test.. i.e. whether it sends emails as we excpect it too.
 * 
 * ATTENTION
 * 
 * Be aware that the Mailbox used here is pure magic, as soon as it is in the classpath it 
 * takes care of all the emailstuff for testing purposes.
 */
public class OutGoingMailAdapterTest {

    private OutgoingMailAdapter outgoingMailAdapter;
    private final static MailProtocol PROTOCOL = MailProtocol.SMTP;
    private final static String USER_NAME = "test";
    private final static String ADDRESS = "example.com";
    private final static String SMTP_SERVER = "localhost";
    private final static String RECIPIENT = "testie@test.de";
    private final static String OTHER_RECIPIENT = "other@recipient.org";
    private final static String SUBJECT = "Test me!";
    private final static String MESSAGE = "A wonderful test this is.";
    private final static int SMTP_PORT = 25;
    
    
    /**
     * Create our adapter which is going to be tested.
     */
    @BeforeClass
    public void setUp() {
        // no password needed as we don't test authentication here
        OutgoingMailAdapterConfiguration outgoingMailAdapterConfiguration = 
            new OutgoingMailAdapterConfiguration(PROTOCOL, USER_NAME, "", ADDRESS, SMTP_SERVER, SMTP_PORT);
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
        outgoingMailAdapter.sendMessage(createAddressableMessage(RECIPIENT, MESSAGE));
        
        Mailbox mailbox = Mailbox.get(RECIPIENT);
        Assert.assertEquals(mailbox.size(), 1);
        Message message = mailbox.get(0);
        Assert.assertEquals(message.getSubject(), OutgoingMailAdapter.DEFAULT_SUBJECT);
        Assert.assertEquals(message.getContent().toString(), MESSAGE);
    }
    
    /**
     * Test simply sending an email and afterwards retrieveing it.
     * @throws MessagingException 
     * @throws IOException 
     */
    @Test
    public void testSimpleSend() throws MessagingException, IOException {
        outgoingMailAdapter.sendMessage(createAddressableMessageWithSubject(RECIPIENT, SUBJECT, MESSAGE));
        
        Mailbox mailbox = Mailbox.get(RECIPIENT);
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
        outgoingMailAdapter.sendMessage(createAddressableMessageWithSubject(RECIPIENT, SUBJECT, MESSAGE));
        outgoingMailAdapter.sendMessage(createAddressableMessageWithSubject(RECIPIENT, 
                                                                            "another subject",
                                                                            "Another message"));
        
        Assert.assertEquals(Mailbox.get(RECIPIENT).size(), 2);
    }
    
    /**
     * Test send two emails to different recipients (1mail/recipient).
     *
     * @throws AddressException the address exception
     */
    @Test
    public void testSendTwoEmailsToDifferentReceipents() throws AddressException {
        outgoingMailAdapter.sendMessage(createAddressableMessage(RECIPIENT, MESSAGE));
        outgoingMailAdapter.sendMessage(createAddressableMessage(OTHER_RECIPIENT, "Hello other guy!"));
        
        Assert.assertEquals(Mailbox.get(RECIPIENT).size(), 1);
        Assert.assertEquals(Mailbox.get(OTHER_RECIPIENT).size(), 1);
    }
    
    /**
     * Creates the addressable message.
     *
     * @param recipient the recipient
     * @param content the content
     * @return the addressable message
     */
    private AddressableMessage createAddressableMessage(String recipient, String content) {
        return new AddressableMessageImpl(recipient, content);
    }
    
    /**
     * Creates the addressable message with subject.
     *
     * @param recipient the recipient
     * @param subject the subject
     * @param content the content
     * @return the addressable message with subject
     */
    private AddressableMessageWithSubject createAddressableMessageWithSubject(String recipient, 
                                                                              String subject, 
                                                                              String content) {
        return new AddressableMessageWithSubjectImpl(recipient, subject, content);
    }
    
    /**
     * Clean up the mailbox after every test so we are good to go from the start.
     */
    @AfterMethod
    public void cleanUp() {
        Mailbox.clearAll();
    }
}
