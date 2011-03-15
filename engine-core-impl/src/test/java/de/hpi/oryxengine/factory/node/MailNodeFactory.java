package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.impl.MailingVariable;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public void setActivity() {
       activity = new MailingVariable("result");
   }

}
