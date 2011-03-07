package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Iterator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import de.hpi.oryxengine.factory.MailNodeFactory;
import de.hpi.oryxengine.factory.SimpleProcessTokenFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class MailNodeActivitytest.
 * Which simply uses a fake mail server to send a mail and tests if the content is right
 */
public class MailNodeActivityTest {
  
  private Node mailernode;
  
  private Token p;
  
  private SimpleSmtpServer maily;
  
  private final static int SMTP_PORT = 2525;
  
  /**
   * Set up.
   * Creates a process token, a mailing node, sets the to-be-sent message
   * and starts the SMTP server on the given port. 
   */
  @BeforeTest
  public void setUp() {
      MailNodeFactory factory = new MailNodeFactory();
      mailernode = factory.create();
      SimpleProcessTokenFactory processfactory = new SimpleProcessTokenFactory(); 
      p = processfactory.create(mailernode);
      p.setVariable("result", "Roflcopter123!");
      maily = SimpleSmtpServer.start(SMTP_PORT);
  }
  
  /**
   * Test the sending of a mail.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testMailsend() {
      p.executeStep();
      maily.stop();
      assertEquals(maily.getReceivedEmailSize(), 1, "Upps we didn't receive an email.. too bad");
      Iterator<SmtpMessage> emailIter = maily.getReceivedEmail();
      SmtpMessage email = (SmtpMessage) emailIter.next();
      assertTrue(email.getBody().contains("Roflcopter123!"), "No Roflcopter on the fly...");
  }
}
