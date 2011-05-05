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

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.factories.worklist.TaskFactory;
import de.hpi.oryxengine.loadgenerator.PseudoHumanJob;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;

/**
 * A factory for creating ExampleProcessToken objects. These objects just have 2 add Number activities.
 */
public class HumanTaskProcessDeployer extends AbstractProcessDeployer {

    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String LAZY = "lazy guy";
    private static final String ROLE = "DUMMIES";
    private static final String JOBGROUP = "dummy";
    private IdentityBuilder identityBuilder;

    private IdentityService identityService;

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

    /**
     * Instantiates a new example process token factory.
     * 
     * @throws SchedulerException
     *             thrown if the scheduler can't work correctly
     */
    public HumanTaskProcessDeployer()
    throws SchedulerException {

        identityService = ServiceFactory.getIdentityService();
        builder = new ProcessDefinitionBuilderImpl();
        identityBuilder = identityService.getIdentityBuilder();

    }
    
    public void initializeNodes() {
        initializeNodesWithRoleTasks();
    }

    /**
     * Initializes the nodes.
     */
    public void initializeNodesWithDirectAlloc() {

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        
        // Create the task
        Object[] participants = identityService.getParticipants().toArray();
        Task task = TaskFactory.createParticipantTask((AbstractResource<?>) participants[0]);

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // Create the task
        task = TaskFactory.createParticipantTask((AbstractResource<?>) identityService.getParticipants().toArray()[1]);

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // Create the task
        task = TaskFactory.createParticipantTask((AbstractResource<?>) identityService.getParticipants().toArray()[2]);
        
        node3 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, node3);
        BpmnNodeFactory.createTransitionFromTo(builder, node3, endNode);
    }
    
    public void initializeNodesWithRoleTasks() {
        
        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        // Create the task
        Task roleTask = TaskFactory.createRoleTask("Do stuff", "Do it cool", role);

        node1 = BpmnNodeFactory.createBpmnUserTaskNode(builder, roleTask);

        node2 = BpmnNodeFactory.createBpmnUserTaskNode(builder, roleTask);

        node3 = BpmnNodeFactory.createBpmnUserTaskNode(builder, roleTask);

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, node3);
        BpmnNodeFactory.createTransitionFromTo(builder, node3, endNode);
    }

    /**
     * Creates our dummy participants with a common role. Those are the ones that will claim and complete activity
     * within a time interval that is determined within the schedule dummy participants method.
     * @throws ResourceNotAvailableException 
     */
    public void createAutomatedParticipants() throws ResourceNotAvailableException {

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
        Set<AbstractParticipant> participants = ServiceFactory.getIdentityService().getParticipants();
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
     * @throws ResourceNotAvailableException 
     */
    @Override
    public void createPseudoHuman() throws ResourceNotAvailableException {

        createAutomatedParticipants();
        try {
            scheduleDummyParticipants();
        } catch (SchedulerException e) {
            logger.error("Scheduler Exception when trying to schedule dummy Participants", e);
        }
    }

}
