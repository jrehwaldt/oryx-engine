package de.hpi.oryxengine.factory.worklist;

import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.resource.Resource;
import de.hpi.oryxengine.worklist.AllocationStrategies;
import de.hpi.oryxengine.worklist.AllocationStrategiesImpl;
import de.hpi.oryxengine.worklist.Pattern;
import de.hpi.oryxengine.worklist.Task;
import de.hpi.oryxengine.worklist.TaskImpl;
import de.hpi.oryxengine.worklist.pattern.SimplePullPattern;
import de.hpi.oryxengine.worklist.pattern.SimplePushPattern;

/**
 * Little factory for creating Resources. A short cut for in the implementation.
 */
public final class TaskFactory {
 
    public static final String SIMPLE_TASK_SUBJECT = "Get Gerardo a cup of coffee!";
    public static final String SIMPLE_TASK_DESCRIPTION = "You know what I mean.";

    /**
     * Private Constructor because the CheckStyle want me to do that.
     */
    private TaskFactory() { }
    
    
    public static Task createJannikServesGerardoTask() {
        
        Pattern pushPattern = new SimplePushPattern();
        Pattern pullPattern = new SimplePullPattern();
        
        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(pushPattern, pullPattern, null, null);
        
        Resource<?> resource = ParticipantFactory.createJannik();
        
        Task task = new TaskImpl(SIMPLE_TASK_SUBJECT,
                                 SIMPLE_TASK_DESCRIPTION,
                                 allocationStrategies,
                                 resource);
        
        return task;
    }
    
    public static Task createSimpleTask(Resource<?> resourceToAssign) { 
        
        Task task = new TaskImpl(SIMPLE_TASK_SUBJECT, SIMPLE_TASK_DESCRIPTION, null, resourceToAssign);
        
        return task;
    }
    
}
