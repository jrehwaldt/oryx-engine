package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.activity.impl.MailingVariable;

/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public static void setActivity() {
       MailNodeFactory.activity = new MailingVariable("result");
   }

}
