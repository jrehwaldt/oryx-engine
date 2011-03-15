package de.hpi.oryxengine.worklist;

/**
 * THe implementation of the Task Interface.
 */
public class TaskImpl implements Task {

    /** The subject. */
    private String subject;
    
    /** The description. */
    private String description;
    
    /** The allocation strategies. */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubject() {

        return subject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {

        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Form getForm() {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AllocationStrategies getAllocationStrategies() {

        return allocationStrategies;
    }

}
