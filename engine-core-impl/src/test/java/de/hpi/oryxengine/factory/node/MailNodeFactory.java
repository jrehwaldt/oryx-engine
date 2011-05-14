package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.node.activity.custom.MailingVariable;

/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public void setActivityBlueprint() {
       
       String variableToBeMailed = "result";
        activityBehavior = new MailingVariable(variableToBeMailed);
   }

}
