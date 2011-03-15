package de.hpi.oryxengine.activity;

import static org.testng.Assert.assertEquals;

import org.jvnet.mock_javamail.Mailbox;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.hpi.oryxengine.factory.node.MailNodeFactory;
import de.hpi.oryxengine.factory.token.SimpleProcessTokenFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

// TODO: Auto-generated Javadoc
/**
 * The Class MailNodeActivitytest.
 * Which simply uses a fake mail server to send a mail and tests if the content is right
 */
public class MailNodeActivityTest {
  
  /** The node. */
  private Node node = null;
  
  /** The p. */
  private Token p = null;
  
  /**
   * Set up.
   * Creates a process token, a mailing node, sets the to-be-sent message
   * and starts the SMTP server on the given port. 
   */
  @BeforeTest
  public void setUp() {
      MailNodeFactory factory = new MailNodeFactory();
      node = factory.create();
      SimpleProcessTokenFactory processfactory = new SimpleProcessTokenFactory(); 
      p = processfactory.create(node);
      p.getContext().setVariable("result", "Roflcopter123!");
  }
  
  /**
   * Test the sending of a mail.
   * @throws Exception thrown if a) the mail could not be send or b) the mock failed to work
   */
  @Test
  public void testMailsend() throws Exception {
      p.executeStep();
      assertEquals(Mailbox.get("gns@oryxengine.de").size(), 1, "Upps we didn't receive an email.. too bad");
  }
}
