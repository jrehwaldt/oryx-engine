package de.hpi.oryxengine.rest.api;

import java.util.HashMap;
import java.util.Map;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.condition.HashMapCondition;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.RolePushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * The Class ShortenedReferenceProcessDeployer. This is the implementation of the shortened version of the AOK reference
 * process.
 */
public class ShortenedReferenceProcessDeployer {

    // configuration constants
    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String GERARDO = "Gerardo";
    private static final String JAN = "Jan";
    private static final String OBJECTION_CLERK = "Objection Clerk";
    private static final String ALLOWANCE_CLERK = "Allowance Clerk";

    private static IdentityBuilder identityBuilder;

    private IdentityService identityService;

    // Nodes
    private static Node startNode;
    private static Node system1;
    private static Node system2;
    private static Node human1;
    private static Node human2;
    private static Node human3;
    private static Node human4;
    private static Node human5;
    private static Node xor1;
    private static Node xor2;
    private static Node xor3;
    private static Node xor4;
    private static Node xor5;
    private static Node endNode;
    
    private static boolean invoked = false;

    private static ProcessDefinitionBuilder builder;
    //
    // /**
    // * Gets the start node.
    // *
    // * @return the start node
    // */
    // public Node getStartNode() {
    //
    // return startNode;
    // }
    //
    // /**
    // * Gets the first system task.
    // *
    // * @return the system1
    // */
    // public Node getSystem1() {
    //
    // return system1;
    // }
    //
    // /**
    // * Gets the second system task.
    // *
    // * @return the system2
    // */
    // public Node getSystem2() {
    //
    // return system2;
    // }
    //
    // /**
    // * Gets the first human task.
    // *
    // * @return the human1
    // */
    // public Node getHuman1() {
    //
    // return human1;
    // }
    //
    // /**
    // * Gets the second human task.
    // *
    // * @return the human2
    // */
    // public Node getHuman2() {
    //
    // return human2;
    // }
    //
    // /**
    // * Gets the third human task.
    // *
    // * @return the human3
    // */
    // public Node getHuman3() {
    //
    // return human3;
    // }
    //
    // /**
    // * Gets the fourth human task.
    // *
    // * @return the human4
    // */
    // public Node getHuman4() {
    //
    // return human4;
    // }
    //
    // /**
    // * Gets the fifth human task.
    // *
    // * @return the human5
    // */
    // public Node getHuman5() {
    //
    // return human5;
    // }
    //
    // /**
    // * Gets first the xor.
    // *
    // * @return the xor1
    // */
    // public Node getXor1() {
    //
    // return xor1;
    // }
    //
    // /**
    // * Gets the second xor.
    // *
    // * @return the xor2
    // */
    // public Node getXor2() {
    //
    // return xor2;
    // }
    //
    // /**
    // * Gets the third xor.
    // *
    // * @return the xor3
    // */
    // public Node getXor3() {
    //
    // return xor3;
    // }
    //
    // /**
    // * Gets the fourth xor.
    // *
    // * @return the xor4
    // */
    // public Node getXor4() {
    //
    // return xor4;
    // }
    //
    // /**
    // * Gets the fifth xor.
    // *
    // * @return the xor5
    // */
    // public Node getXor5() {
    //
    // return xor5;
    // }
    //
    // /**
    // * Gets the end node.
    // *
    // * @return the end node
    // */
    // public Node getEndNode() {
    //
    // return endNode;
    // }

    // roles and participants
    private static Role objectionClerk;
    private static Role allowanceClerk;
    private static Participant jan;
    private static Participant gerardo;
    private static Participant tobi;
    private static Participant jannik;

    // /**
    // * Gets the objection clerk.
    // *
    // * @return the objection clerk
    // */
    // public Role getObjectionClerk() {
    //
    // return objectionClerk;
    // }
    //
    // /**
    // * Gets the participant "Jan".
    // *
    // * @return the participant "Jan"
    // */
    // public Participant getJan() {
    //
    // return jan;
    // }
    //
    // /**
    // * Gets the participant "Gerardo".
    // *
    // * @return the participant "Gerardo"
    // */
    // public Participant getGerardo() {
    //
    // return gerardo;
    // }
    //
    // /**
    // * Gets the participant "Tobi".
    // *
    // * @return the participant "Tobi"
    // */
    // public Participant getTobi() {
    //
    // return tobi;
    // }
    //
    // /**
    // * Gets the participant "Jannik".
    // *
    // * @return the participant "Jannik"
    // */
    // public Participant getJannik() {
    //
    // return jannik;
    // }

    /**
     * Default constructor.
     */
    public ShortenedReferenceProcessDeployer() {

        identityService = ServiceFactory.getIdentityService();
        builder = new ProcessBuilderImpl();
        identityBuilder = identityService.getIdentityBuilder();
    }

    /**
     * Initialize nodes.
     */
    public static void initializeNodes() {

        // start node, blank
        startNode = BPMNActivityFactory.createBPMNStartEventNode(builder);

        system1 = BPMNActivityFactory.createBPMNPrintingVariableNode(builder, "Widerspruch wird vorbearbeitet");

        // human task for objection clerk, task is to check
        // positions of objection
        Task task = createRoleTask("Positionen auf Anspruch prüfen", "Anspruchspositionen überprüfen", objectionClerk);
        human1 = BPMNActivityFactory.createBPMNUserTaskNode(builder, task);

        // XOR Split, condition is objection existence
        xor1 = BPMNActivityFactory.createBPMNXorGatewayNode(builder);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("widerspruch", "stattgegeben");
        Condition condition1 = new HashMapCondition(map1, "==");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("widerspruch", "abgelehnt");
        Condition condition2 = new HashMapCondition(map2, "==");

        // human task for objection clerk, task is to check objection
        task = createRoleTask("Widerspruch prüfen", "Widerspruch erneut prüfen auf neue Ansprüche", objectionClerk);
        human2 = BPMNActivityFactory.createBPMNUserTaskNode(builder, task);

        // XOR Split, condition is new relevant aspects existence
        xor2 = BPMNActivityFactory.createBPMNXorGatewayNode(builder);
        map1 = new HashMap<String, Object>();
        map1.put("neue Aspekte", "ja");
        Condition condition3 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("neue Aspekte", "nein");
        Condition condition4 = new HashMapCondition(map1, "==");

        // human task for objection clerk, task is to create a new report
        task = createRoleTask("neues Gutachten erstellen", "Anspruchspunkte in neues Gutachten übertragen",
            objectionClerk);
        human3 = BPMNActivityFactory.createBPMNUserTaskNode(builder, task);

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3 = BPMNActivityFactory.createBPMNXorGatewayNode(builder);
        map1 = new HashMap<String, Object>();
        map1.put("aufrecht", "ja");
        Condition condition5 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("aufrecht", "nein");
//        Condition condition6 = new HashMapCondition(map1, "==");

        // XOR Join
        xor4 = BPMNActivityFactory.createBPMNXorGatewayNode(builder);

        // human task for objection clerk, task is to do final work
        task = createRoleTask("Nachbearbeitung", "abschließende Nachbearbeitung des Falls", objectionClerk);
        human4 = BPMNActivityFactory.createBPMNUserTaskNode(builder, task);

        // human task for allowance clerk, task is to enforce allowance
        task = createRoleTask("Leistungsgewährung umsetzen", "Leistungsansprüche durchsetzen", allowanceClerk);
        human5 = BPMNActivityFactory.createBPMNUserTaskNode(builder, task);

        // final XOR Join
        xor5 = BPMNActivityFactory.createBPMNXorGatewayNode(builder);

        // system task, close file
        system2 = BPMNActivityFactory.createBPMNPrintingVariableNode(builder, "Akte wird geschlossen");

        // end node
        endNode = BPMNActivityFactory.createBPMNEndEventNode(builder);

        // connect the nodes
        BPMNActivityFactory.createTransitionFromTo(builder, startNode, system1);
        BPMNActivityFactory.createTransitionFromTo(builder, system1, human1);
        BPMNActivityFactory.createTransitionFromTo(builder, human1, xor1);
        BPMNActivityFactory.createTransitionFor(builder, xor1, human2, condition2);
        BPMNActivityFactory.createTransitionFor(builder, xor1, human5, condition1);
        BPMNActivityFactory.createTransitionFromTo(builder, human2, xor2);
        BPMNActivityFactory.createTransitionFor(builder, xor2, human3, condition3);
        BPMNActivityFactory.createTransitionFor(builder, xor2, xor4, condition4);
        BPMNActivityFactory.createTransitionFromTo(builder, human3, xor3);
        BPMNActivityFactory.createTransitionFor(builder, xor3, xor4, condition5);
        BPMNActivityFactory.createTransitionFromTo(builder, xor4, human4);
        BPMNActivityFactory.createTransitionFromTo(builder, human4, human5);
        BPMNActivityFactory.createTransitionFromTo(builder, xor5, system2);
        BPMNActivityFactory.createTransitionFromTo(builder, human5, xor5);
        BPMNActivityFactory.createTransitionFromTo(builder, system2, endNode);
    }

    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. See also
    // * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
    // *
    // * @param activityClass
    // * the activity class which the node shall be connected to
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass) {
    //
    // return createParamBuilderFor(activityClass, null, null, new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
    // }
    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. See also
    // *
    // * @param activityClass
    // * the activity class which the node shall be connected to
    // * @param paramClass
    // * the class of the parameter
    // * @param constructorParam
    // * the parameters that should be passed on to the constructor of the activity
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // * {@link #createParamBuilderFor(Class, Object, IncomingBehaviour, OutgoingBehaviour)}
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass,
    // Class<? extends Object> paramClass,
    // Object constructorParam) {
    //
    // return createParamBuilderFor(activityClass, paramClass, constructorParam, new SimpleJoinBehaviour(),
    // new TakeAllSplitBehaviour());
    // }
    //
    // /**
    // * Helper method for creating a {@link NodeParameterBuilder}. There are also smaller convenient helper methods for
    // * special cases.
    // *
    // * @param activityClass
    // * the {@link Activity} class which the node shall be connected to
    // * @param paramClass
    // * class of the parameter
    // * @param constructorParam
    // * the parameters that should be passed on to the constructor of the {@link Activity}
    // * @param in
    // * the {@link IncomingBehaviour} of the node
    // * @param out
    // * the {@link OutgoingBehaviour} of the node
    // * @return the {@link NodeParameterBuilder} for the given parameters
    // */
    // private NodeParameterBuilder createParamBuilderFor(Class<? extends AbstractActivity> activityClass,
    // Class<? extends Object> paramClass,
    // Object constructorParam,
    // IncomingBehaviour in,
    // OutgoingBehaviour out) {
    //
    // NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(in, out);
    // nodeParamBuilder.setActivityBlueprintFor(activityClass);
    // if (constructorParam != null) {
    // nodeParamBuilder.addConstructorParameter(paramClass, constructorParam);
    // }
    // return nodeParamBuilder;
    // }

    /**
     * Creates the participants,
     */
    public static void createParticipants() {

        jannik = (Participant) identityBuilder.createParticipant(JANNIK);
        tobi = (Participant) identityBuilder.createParticipant(TOBI);
        gerardo = (Participant) identityBuilder.createParticipant(GERARDO);
        jan = (Participant) identityBuilder.createParticipant(JAN);
        objectionClerk = (Role) identityBuilder.createRole(OBJECTION_CLERK);
        allowanceClerk = (Role) identityBuilder.createRole(ALLOWANCE_CLERK);
        identityBuilder.participantBelongsToRole(jannik.getID(), objectionClerk.getID())
        .participantBelongsToRole(tobi.getID(), objectionClerk.getID())
        .participantBelongsToRole(gerardo.getID(), objectionClerk.getID())
        .participantBelongsToRole(jan.getID(), allowanceClerk.getID());
    }

    /*
     * Hackety hack anti movement methods!!!
     */

    /**
     * Creates the role task.
     *
     * @param subject the subject
     * @param description the description
     * @param resource the resource
     * @return the task
     */
    private static Task createRoleTask(String subject, String description, AbstractResource<?> resource) {

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(new RolePushPattern(),
            new SimplePullPattern(), null, null);

        return createTask(subject, description, allocationStrategies, resource);
    }

    /**
     * Creates the task - qucikn dirty helper.
     *
     * @param subject the subject
     * @param description the description
     * @param allocationStrategies the allocation strategies
     * @param resource the resource
     * @return the task
     */
    private static Task createTask(String subject,
                           String description,
                           AllocationStrategies allocationStrategies,
                           AbstractResource<?> resource) {

        Task task = new TaskImpl(subject, description, allocationStrategies, resource);
        return task;
    }
    
    /**
     * Generates/deploys the shortened reference process.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    public static synchronized void generate() throws IllegalStarteventException {
        if (!invoked) {
            createParticipants();
            initializeNodes();
            ProcessDefinition definition = builder.buildDefinition();
            DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
            invoked = true;
        }
    }

}
