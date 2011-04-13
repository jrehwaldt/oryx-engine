package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.activity.impl.HashComputationActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.allocation.pattern.SimplePullPattern;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * A factory for creating a HumanTaskNode objects.
 */
public class HumanTaskNodeFactory extends AbstractNodeFactory {
    /**
     * Sets the activity, overwriting the process in the AbstractNodeFactory.
     */
    @Override
    public void setActivity() {
        
        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");
        
        String subject = "Jannik, get me a cup of coffee!";
        String description = "You know what i mean.";
        
        Pattern pushPattern = new DirectPushPattern();
        Pattern pullPattern = new SimplePullPattern();
        
        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        
        TaskImpl task = new TaskImpl(subject, description, allocationStrategies, participant);
        
//        activity = new HumanTaskActivity(task);
        // TODO parameters
        activityClazz = HumanTaskActivity.class;
    }

}
