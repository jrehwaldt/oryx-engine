package de.hpi.oryxengine.factory;

/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public static void setActivity() {
       MailNodeFactory.activity = MailingVariableActivityFactory.create();
   }

}
