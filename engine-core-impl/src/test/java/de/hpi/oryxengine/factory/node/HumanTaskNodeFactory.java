package de.hpi.oryxengine.factory.node;

import de.hpi.oryxengine.IdentityServiceImpl;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.node.activity.bpmn.BpmnHumanTaskActivity;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectDistributionPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

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
        
        CreationPattern creationPattern = new DirectDistributionPattern(subject, description, null, resources);
                
        Class<?>[] constructorSig = {CreationPattern.class};
        Object[] params = {creationPattern};
        blueprint = new ActivityBlueprintImpl(BpmnHumanTaskActivity.class, constructorSig, params);
    }

}
