package org.jodaengine.factory.node;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.IdentityServiceImpl;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.pattern.creation.DirectDistributionPattern;


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
        List<AbstractResource<?>> resources = new ArrayList<AbstractResource<?>>();
        resources.add(participant);
        
        CreationPattern creationPattern = new DirectDistributionPattern(subject, description, null, resources);
                
        activityBehavior = new BpmnHumanTaskActivity(creationPattern);
    }

}
