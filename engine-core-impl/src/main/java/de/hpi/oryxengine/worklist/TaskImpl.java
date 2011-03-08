package de.hpi.oryxengine.worklist;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    private String subject;
    private String description;
    private AllocationStrategies allocationStrategies;

    /**
     * Default Constructor.
     * 
     * @param subject
     *            - the subject of the task
     * @param description
     *            - further information for the task
     * @param allocationStrategies
     *            - constains all {@link AllocationStrategies} responsible for the distribution of the task
     */
    public TaskImpl(String subject, String description, AllocationStrategies allocationStrategies) {

        this.subject = subject;
        this.description = description;
        this.allocationStrategies = allocationStrategies;
    }

    @Override
    public String getSubject() {

        return subject;
    }

    @Override
    public String getDescription() {

        return description;
    }

    @Override
    public Form getForm() {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

}
