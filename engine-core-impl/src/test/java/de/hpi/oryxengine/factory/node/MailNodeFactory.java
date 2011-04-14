package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;

/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public void setActivityBlueprint() {
       Class<?>[] constructorSig = {String.class};
       Object[] params = {"result"};
       blueprint = new ActivityBlueprintImpl(MailingVariable.class, constructorSig, params);
   }

}
