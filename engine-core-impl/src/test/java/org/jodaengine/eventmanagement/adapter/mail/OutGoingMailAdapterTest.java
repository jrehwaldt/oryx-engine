/*
 * 
 */
package org.jodaengine.eventmanagement.adapter.mail;

import javax.mail.internet.AddressException;

import org.jvnet.mock_javamail.Mailbox;
import org.testng.Assert;
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
     * Test simply sending an email and afterwards retrievein it.
     *
     * @throws AddressException the address exception
     */
    @Test
    public void testSimpleSend() throws AddressException {
        outgoingMailAdapter.sendMessage(RECEIPENT, SUBJECT, MESSAGE);
        
        
        Assert.assertEquals(Mailbox.get(RECEIPENT).size(), 1);
    }
}
