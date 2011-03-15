package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.worklist.AllocationStrategies;
import de.hpi.oryxengine.worklist.AllocationStrategiesImpl;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.TaskImpl;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePushPattern;

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
        Participant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");
        
        String subject = "Jannik, get me a cup of coffee!";
        String description = "You know what i mean.";
        
        Pattern pushPattern = new SimplePushPattern();
        Pattern pullPattern = new SimplePullPattern();
        
        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        
        TaskImpl task = new TaskImpl(subject, description, allocationStrategies, participant);
        
        activity = new HumanTaskActivity(task);
    }

}
