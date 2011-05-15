package org.jodaengine.factory.node;

import org.jodaengine.IdentityServiceImpl;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;


/**
 * A factory for creating a HumanTaskNode objects.
 */
public class HumanTaskNodeFactory extends AbstractNodeFactory {
    /**
     * Sets the activity, overwriting the process in the AbstractNodeFactory.
     */
    @Override
    public void setActivityBlueprint() {
        
        IdentityBuilder identityBuilder = new IdentityServiceImpl().getIdentityBuilder();
        AbstractParticipant participant = identityBuilder.createParticipant("jannik");
        participant.setName("Jannik Streek");
        
        String subject = "Jannik, get me a cup of coffee!";
        String description = "You know what i mean.";
        AbstractResource<?>[] resources = {participant};
        
        CreationPattern creationPattern = new ConcreteResourcePattern(subject, description, null, resources);
                
        activityBehavior = new BpmnHumanTaskActivity(creationPattern, new AllocateSinglePattern());
    }

}
