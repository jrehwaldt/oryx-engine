package de.hpi.oryxengine.factories.worklist;

import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.RolePushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * Little factory for creating Resources. A short cut for the implementation.
 */
public final class TaskFactory {

	public static final String SIMPLE_TASK_SUBJECT = "Get Gerardo a cup of coffee!";
	public static final String SIMPLE_TASK_DESCRIPTION = "You know what I mean.";

	/**
	 * Private Constructor because the CheckStyle want me to do that. Gerardo do
	 * what told. Gerardo intelligent. Gerardo checkstyle also want you to
	 * comment methods. Gerardo better do that.
	 */
	private TaskFactory() {
	}

	public static Task createTask(String subject, String description,
			AllocationStrategies allocationStrategies,
			AbstractResource<?> resource) {
	    
		Task task = new TaskImpl(subject, description, allocationStrategies,
				resource);
		return task;
	}

	public static Task createParticipantTask(AbstractResource<?> resource) {
		AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(
				new DirectPushPattern(), new SimplePullPattern(), null, null);

		return createTask(SIMPLE_TASK_SUBJECT, SIMPLE_TASK_SUBJECT,
				allocationStrategies, resource);
	}

	public static Task createRoleTask(String subject, String description, AbstractResource<?> resource) {

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(new RolePushPattern(),
            new SimplePullPattern(), null, null);
		
		return createTask(subject, description, allocationStrategies, resource);
	}
}
