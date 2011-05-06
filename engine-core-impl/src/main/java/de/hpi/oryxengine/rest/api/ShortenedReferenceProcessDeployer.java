package de.hpi.oryxengine.rest.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.condition.HashMapCondition;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.FormImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.RolePushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * The Class ShortenedReferenceProcessDeployer. This is the implementation of the shortened version of the AOK reference
 * process.
 */
public final class ShortenedReferenceProcessDeployer {

    // TODO move somewhere else

    // configuration constants
    private static final String NAME_JANNIK = "Jannik";
    private static final String NAME_TOBI = "Tobi";
    private static final String NAME_GERARDO = "Gerardo";
    private static final String NAME_JAN = "Jan";
    private static final String NAME_OBJECTION_CLERK = "Objection Clerk";
    private static final String NAME_ALLOWANCE_CLERK = "Allowance Clerk";

    private static IdentityService identityService = ServiceFactory.getIdentityService();
    private static ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilderImpl();
    private static IdentityBuilder identityBuilder = identityService.getIdentityBuilder();

    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms";

    // Nodes
    private static Node startNode, system1Node, system2Node, human1Node, human2Node, human3Node, human4Node, human5Node, xor1Node, xor2Node, xor3Node, xor4Node,
    xor5Node, endNode;

    private static boolean invoked = false;

    /**
     * Hidden constructor, as this class only provides static methods.
     */
    private ShortenedReferenceProcessDeployer() {

        // do nothing
    }

    // roles and participants
    private static Role objectionClerk;
    private static Role allowanceClerk;
    private static Participant janParticipant;
    private static Participant gerardoParticipant;
    private static Participant tobiParticipant;
    private static Participant jannikParticipant;

    /**
     * Initialize nodes.
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    public static void initializeNodes()
    throws DefinitionNotFoundException {

        // start node, blank
        startNode = BpmnNodeFactory.createBpmnStartEventNode(processDefinitionBuilder);

        system1Node = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(processDefinitionBuilder,
            "Widerspruch wird vorbearbeitet");

        // human task for objection clerk, task is to check
        // positions of objection
        Form form = extractForm("form1", "claimPoints.html");
        Task task = createRoleTask("Positionen auf Anspruch pruefen", "Anspruchspositionen ueberpruefen", form,
            objectionClerk);
        human1Node = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, task);

        // XOR Split, condition is objection existence
        xor1Node = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("widerspruch", "stattgegeben");
        Condition condition1 = new HashMapCondition(map1, "==");
        Map<String, Object> map2 = new HashMap<String, Object>();
        // == abgelehnt or null
        map2.put("widerspruch", "stattgegeben");
        Condition condition2 = new HashMapCondition(map2, "!=", true);

        // human task for objection clerk, task is to check objection
        form = extractForm("form2", "checkForNewClaims.html");
        task = createRoleTask("Widerspruch pruefen", "Widerspruch erneut pruefen auf neue Ansprueche", form,
            objectionClerk);
        human2Node = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, task);

        // XOR Split, condition is new relevant aspects existence
        xor2Node = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        map1 = new HashMap<String, Object>();
        map1.put("neue Aspekte", "ja");
        Condition condition3 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("neue Aspekte", "ja");
        Condition condition4 = new HashMapCondition(map2, "!=", true);

        // human task for objection clerk, task is to create a new report
        form = extractForm("form3", "createReport.html");
        task = createRoleTask("neues Gutachten erstellen", "Anspruchspunkte in neues Gutachten uebertragen", form,
            objectionClerk);
        human3Node = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, task);

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3Node = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);
        map1 = new HashMap<String, Object>();
        map1.put("aufrecht", "ja");
        Condition condition5 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("aufrecht", "ja");
        Condition condition6 = new HashMapCondition(map2, "!=", true);

        // XOR Join
        xor4Node = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // human task for objection clerk, task is to do final work
        form = extractForm("form4", "postEditingClaim.html");
        task = createRoleTask("Nachbearbeitung", "Nachbearbeitung des Falls", form, objectionClerk);
        human4Node = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, task);

        // human task for allowance clerk, task is to enforce allowance
        form = extractForm("form5", "enforceAllowance.html");
        task = createRoleTask("Leistungsgewaehrung umsetzen", "Leistungsansprueche durchsetzen", form, allowanceClerk);
        human5Node = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, task);

        // final XOR Join
        xor5Node = BpmnNodeFactory.createBpmnXorGatewayNode(processDefinitionBuilder);

        // system task, close file
        system2Node = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(processDefinitionBuilder,
            "Akte wird geschlossen");

        // end node
        endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);

        // connect the nodes
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, system1Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, system1Node, human1Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human1Node, xor1Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor1Node, human2Node, condition2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor1Node, human5Node, condition1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human2Node, xor2Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor2Node, human3Node, condition3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor2Node, xor4Node, condition4);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human3Node, xor3Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor3Node, xor4Node, condition5);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor3Node, xor5Node, condition6);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor4Node, human4Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human4Node, human5Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, xor5Node, system2Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, human5Node, xor5Node);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, system2Node, endNode);

        processDefinitionBuilder.setName("Shortened Reference Process").setDescription("Shortened Reference Process");
    }

    /**
     * Creates the participants.
     * 
     * @throws ResourceNotAvailableException
     */
    public static void createParticipants()
    throws ResourceNotAvailableException {

        jannikParticipant = (Participant) identityBuilder.createParticipant(NAME_JANNIK);
        tobiParticipant = (Participant) identityBuilder.createParticipant(NAME_TOBI);
        gerardoParticipant = (Participant) identityBuilder.createParticipant(NAME_GERARDO);
        janParticipant = (Participant) identityBuilder.createParticipant(NAME_JAN);
        objectionClerk = (Role) identityBuilder.createRole(NAME_OBJECTION_CLERK);
        allowanceClerk = (Role) identityBuilder.createRole(NAME_ALLOWANCE_CLERK);
        identityBuilder.participantBelongsToRole(jannikParticipant.getID(), objectionClerk.getID())
        .participantBelongsToRole(tobiParticipant.getID(), objectionClerk.getID())
        .participantBelongsToRole(gerardoParticipant.getID(), objectionClerk.getID())
        .participantBelongsToRole(janParticipant.getID(), allowanceClerk.getID());
    }

    /*
     * Hackety hack anti movement methods!!!
     */

    /**
     * Creates the role task.
     * 
     * @param subject
     *            the subject
     * @param description
     *            the description
     * @param form
     *            the form
     * @param resource
     *            the resource
     * @return the task
     */
    private static Task createRoleTask(String subject, String description, Form form, AbstractResource<?> resource) {

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(new RolePushPattern(),
            new SimplePullPattern(), null, null);

        return createTask(subject, description, form, allocationStrategies, resource);
    }

    /**
     * Creates the task - quick'n'dirty helper.
     * 
     * @param subject
     *            the subject
     * @param description
     *            the description
     * @param form
     *            the form
     * @param allocationStrategies
     *            the allocation strategies
     * @param resource
     *            the resource
     * @return the task
     */
    private static Task createTask(String subject,
                                   String description,
                                   Form form,
                                   AllocationStrategies allocationStrategies,
                                   AbstractResource<?> resource) {

        Task task = new TaskImpl(subject, description, form, allocationStrategies, resource);
        return task;
    }

    /**
     * Extract form.
     * 
     * @param formName
     *            the form name
     * @param formPath
     *            the form path
     * @return the form
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    private static Form extractForm(String formName, String formPath)
    throws DefinitionNotFoundException {

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        UUID processArtifactID = deploymentBuilder.deployArtifactAsFile(formName, new File(PATH_TO_WEBFORMS + "/"
            + formPath));
        Form form;
        form = new FormImpl(ServiceFactory.getRepositoryService().getProcessResource(processArtifactID));
        return form;
    }

    /**
     * Generates/deploys the shortened reference process.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     * @throws ResourceNotAvailableException
     */
    public static synchronized void generate()
    throws IllegalStarteventException, DefinitionNotFoundException, ResourceNotAvailableException {

        if (!invoked) {
            createParticipants();
            initializeNodes();
            ProcessDefinition definition = processDefinitionBuilder.buildDefinition();
            DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
            invoked = true;
        }
    }
}
