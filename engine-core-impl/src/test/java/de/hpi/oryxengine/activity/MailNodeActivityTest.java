package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import de.hpi.oryxengine.factory.MailNodeFactory;
import de.hpi.oryxengine.factory.SimpleProcessInstanceFactory;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class MailNodeActivitytest.
 * Which simply uses a fake mail server to send a mail and tests if the content is right
 */
public class MailNodeActivityTest {
  
  /** The mailernode. */
  private Node mailernode;
  
  /** The p. */
  private ProcessInstance p;
  
  /** The maily. */
  private SimpleSmtpServer maily;
  
  /** The SMTP port. */
  private final static int SMTP_PORT = 2525;
  
  /**
   * Sets the up.
   */
  @BeforeTest
  public void setUp() {
      MailNodeFactory factory = new MailNodeFactory();
      mailernode = factory.create();
      SimpleProcessInstanceFactory processfactory = new SimpleProcessInstanceFactory(); 
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
      assertEquals(email.getBody().contains("Roflcopter123!"), true, "No Roflcopter on the fly...");
  }
}
