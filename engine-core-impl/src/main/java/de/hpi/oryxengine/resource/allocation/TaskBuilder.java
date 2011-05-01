package de.hpi.oryxengine.resource.allocation;

import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Pattern;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.resource.AbstractResource;

/**
 * This class helps to build {@link Task Tasks}.
 */
public interface TaskBuilder {

    /**
     * Sets the subject of the {@link Task} that should be built.
     * 
     * @param taskSubject
     *            - the subject of the Task
     * @return the current {@link TaskBuilder} in order continue configuring the Task
     */
    TaskBuilder setTaskSubject(String taskSubject);

    /**
     * Sets the description of the {@link Task} that should be built.
     * 
     * @param taskDescription
     *            - the description of the {@link Task}
     * @return the current {@link TaskBuilder} in order continue configuring the Task
     */
    TaskBuilder setTaskDescription(String taskDescription);

    /**
     * Sets the {@link Form} of the {@link Task} that should be built.
     * 
     * @param taskForm
     *            - the {@link Form} of the {@link Task}
     * @return the current {@link TaskBuilder} in order continue configuring the Task
     */
    TaskBuilder setTaskForm(Form taskForm);

    /**
     * Sets the {@link Pattern PushPattern} of the {@link Task} that should be built.
     * 
     * @param taskPushPattern
     *            - the {@link Pattern PushPattern} of the {@link Task}
     * @return the current {@link TaskBuilder} in order continue configuring the Task
     */
    TaskBuilder setTaskPushPattern(Pattern taskPushPattern);

    /**
     * Adds an {@link AbstractResource Resource} that should be assigned to the {@link Task}.
     * 
     * @param resourceAssignedToTask
     *            - the {@link AbstractResource Resource} that should be assigned to the {@link Task}
     * @return the current {@link TaskBuilder} in order continue configuring the Task
     */
    TaskBuilder addResourceAssignedToTask(AbstractResource<?> resourceAssignedToTask);

    /**
     * Builds the task.
     * 
     * @return the {@link Task} specified before by the {@link TaskBuilder}
     */
    Task buildTask();
}
