package de.hpi.oryxengine.factories.process;

import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.loadgenerator.PseudoHumanJob;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilder;
import de.hpi.oryxengine.resource.allocation.CreationPatternBuilderImpl;
import de.hpi.oryxengine.resource.allocation.pattern.AllocateSinglePattern;
import de.hpi.oryxengine.resource.allocation.pattern.OfferMultiplePattern;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2 add Number activities.
 */
public class HumanTaskProcessDeployer extends AbstractProcessDeployer {

    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String LAZY = "lazy guy";
    private static final String ROLE = "DUMMIES";
    private static final String JOBGROUP = "dummy";

    public static final String PARTICIPANT_KEY = "Participant";

    /** an array with the waiting times of the different pseudo humans. */
    public static final int[] WAITING_TIME = {1000, 1000, 1000};

    private Scheduler scheduler;

    /** The node1. */
    private Node node1;

    /** The node2. */
    private Node node2;

    /** The node3. */
    private Node node3;

    /** The start node. */
    private Node startNode;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Role role;

    private static final String SIMPLE_TASK_SUBJECT = "Get Gerardo a cup of coffee!";
    private static final String SIMPLE_TASK_DESCRIPTION = "You know what I mean.";

    /**
     * Instantiates a new example process token factory.
     * 
     * @throws SchedulerException
     *             thrown if the scheduler can't work correctly
     */
    public HumanTaskProcessDeployer()
    throws SchedulerException {
    }

    @Override
    public void initializeNodes() {

        initializeNodesWithRoleTasks();
    }

    /**
     * Initializes the nodes.
     */
    public void initializeNodesWithDirectAlloc() {

        startNode = BpmnNodeFactory.createBpmnStartEventNode(processDefinitionBuilder);
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();

        Object[] participants = identityService.getParticipants().toArray();

        AbstractResource<?> resourceToAssign = (AbstractResource<?>) participants[0];
        builder.setItemDescription(SIMPLE_TASK_DESCRIPTION).setItemSubject(SIMPLE_TASK_SUBJECT).setItemForm(null)
        .addResourceAssignedToItem(resourceToAssign);

        // CreationPattern task = TaskFactory.createParticipantTask((AbstractResource<?>) participants[0]);)

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new AllocateSinglePattern());

        // Create the task
        resourceToAssign = (AbstractResource<?>) participants[1];
        builder.flushAssignedResources().addResourceAssignedToItem(resourceToAssign);

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new AllocateSinglePattern());

        // Create the task
        resourceToAssign = (AbstractResource<?>) participants[2];
        builder.flushAssignedResources().addResourceAssignedToItem(resourceToAssign);

        node3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder,
            builder.buildConcreteResourcePattern(), new AllocateSinglePattern());

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node2, node3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node3, endNode);
    }

    /**
     * Initialize the nodes with role tasks.
     */
    public void initializeNodesWithRoleTasks() {

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(processDefinitionBuilder);

        // Create the task
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();
        builder.setItemDescription("Do it cool").setItemSubject("Do stuff").setItemForm(null)
        .addResourceAssignedToItem(role);

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, builder.buildConcreteResourcePattern(),
            new OfferMultiplePattern());

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, builder.buildConcreteResourcePattern(),
            new OfferMultiplePattern());

        node3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, builder.buildConcreteResourcePattern(),
            new OfferMultiplePattern());

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node2, node3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, node3, endNode);
    }

    /**
     * Creates our dummy participants with a common role. Those are the ones that will claim and complete activity
     * within a time interval that is determined within the schedule dummy participants method.
     * 
     * @throws ResourceNotAvailableException
     */
    public void createAutomatedParticipants()
    throws ResourceNotAvailableException {

        Participant jannik = (Participant) identityBuilder.createParticipant(JANNIK);
        Participant tobi = (Participant) identityBuilder.createParticipant(TOBI);
        Participant lazy = (Participant) identityBuilder.createParticipant(LAZY);
        role = (Role) identityBuilder.createRole(ROLE);
        identityBuilder.participantBelongsToRole(jannik.getID(), role.getID())
        .participantBelongsToRole(tobi.getID(), role.getID()).participantBelongsToRole(lazy.getID(), role.getID());

    }

    /**
     * Schedule the dummy participants with a given time. A quartz scheduler is used to schedule them.
     * 
     * @throws SchedulerException
     *             the scheduler exception
     */
    public void scheduleDummyParticipants()
    throws SchedulerException {

        // Create the quartz scheduler
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.scheduler.start();

        // Schedule the jobs of our participants
        Set<AbstractParticipant> participants = identityService.getParticipants();
        int i = 0;
        for (AbstractParticipant participant : participants) {

            JobDetail jobDetail = new JobDetail(participant.getName(), JOBGROUP, PseudoHumanJob.class);
            JobDataMap data = jobDetail.getJobDataMap();
            data.put(PARTICIPANT_KEY, participant);

            Trigger trigger = new SimpleTrigger(participant.getID().toString(), -1, WAITING_TIME[i++]);

            try {
                this.scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException se) {
                logger.error("Failed scheduling of event manager job", se);
            }

        }

    }

    @Override
    public void stop() {

        try {
            this.scheduler.shutdown();
        } catch (SchedulerException e) {
            logger.error("Error when shutting down the scheduler of the Human process", e);
        }
    }

    /**
     * Really creates Pseudo Humans. @see scheduleDummyParticipants {@inheritDoc}
     * 
     * @throws ResourceNotAvailableException
     */
    @Override
    public void createPseudoHuman()
    throws ResourceNotAvailableException {

        createAutomatedParticipants();
        try {
            scheduleDummyParticipants();
        } catch (SchedulerException e) {
            logger.error("Scheduler Exception when trying to schedule dummy Participants", e);
        }
    }

}
