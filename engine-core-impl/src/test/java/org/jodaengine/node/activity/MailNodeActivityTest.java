package org.jodaengine.node.activity;

import static org.testng.Assert.assertEquals;

import org.jvnet.mock_javamail.Mailbox;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.jodaengine.factory.node.MailNodeFactory;
import org.jodaengine.factory.token.SimpleProcessTokenFactory;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;


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
  @BeforeClass
  public void setUp() {
      MailNodeFactory factory = new MailNodeFactory();
      node = factory.create();
      SimpleProcessTokenFactory processfactory = new SimpleProcessTokenFactory(); 
      p = processfactory.create(node);
      ProcessInstanceContext context = p.getInstance().getContext();
      context.setVariable("result", "Roflcopter123!");
  }
  
  /**
   * Test the sending of a mail.
   * @throws Exception thrown if a) the mail could not be send or b) the mock failed to work
   */
  @Test
  public void testMailsend() throws Exception {
      p.executeStep();
// CHECKSTYLE:OFF
      assertEquals(Mailbox.get("gns@oryxengine.de").size(), 1, "Upps we didn't receive an email.. too bad");
// CHECKSTYLE:ON
  }
}
