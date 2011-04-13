package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.activity.impl.HashComputationActivity;
import de.hpi.oryxengine.activity.impl.MailingVariable;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.structure.Node;

/**
 * A factory for creating MailNode objects.
 */
public class MailNodeFactory extends AbstractNodeFactory {
   /** The set activity. */
   public void setActivity() {
       activityClazz = MailingVariable.class;
   }

@Override
public void registerActivityParameters(ProcessInstance instance, Node node) {

    Class<?>[] constructorSig = {String.class};
    Object[] params = {"result"};

    instance.getContext().setActivityConstructorClasses(node.getID(), constructorSig);
    instance.getContext().setActivityParameters(node.getID(), params);
    
}

}
