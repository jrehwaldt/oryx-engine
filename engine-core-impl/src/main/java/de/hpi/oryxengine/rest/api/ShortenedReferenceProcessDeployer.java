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
import de.hpi.oryxengine.node.factory.bpmn.BpmnFunNodeFactory;
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

    // configuration constants
    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String GERARDO = "Gerardo";
    private static final String JAN = "Jan";
    private static final String OBJECTION_CLERK = "Objection Clerk";
    private static final String ALLOWANCE_CLERK = "Allowance Clerk";

    private static IdentityService identityService = ServiceFactory.getIdentityService();
    private static ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
    private static IdentityBuilder identityBuilder = identityService.getIdentityBuilder();

    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms";

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

    /**
     * Hidden constructor, as this class only provides static methods.
     */
    private ShortenedReferenceProcessDeployer() {

        // do nothing
    }

    // roles and participants
    private static Role objectionClerk;
    private static Role allowanceClerk;
    private static Participant jan;
    private static Participant gerardo;
    private static Participant tobi;
    private static Participant jannik;

    /**
     * Initialize nodes.
     *
     * @throws DefinitionNotFoundException the definition not found exception
     */
    public static void initializeNodes()
    throws DefinitionNotFoundException {

        // start node, blank
        startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);

        system1 = BpmnFunNodeFactory.createBpmnPrintingVariableNode(builder, "Widerspruch wird vorbearbeitet");

        // human task for objection clerk, task is to check
        // positions of objection
        Form form = extractForm("form1", "claimPoints.html");
        Task task = createRoleTask("Positionen auf Anspruch prüfen", "Anspruchspositionen überprüfen", form,
            objectionClerk);
        human1 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // XOR Split, condition is objection existence
        xor1 = BpmnNodeFactory.createBpmnXorGatewayNode(builder);
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("widerspruch", "stattgegeben");
        Condition condition1 = new HashMapCondition(map1, "==");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("widerspruch", "abgelehnt");
        Condition condition2 = new HashMapCondition(map2, "==");

        // human task for objection clerk, task is to check objection
        form = extractForm("form2", "checkForNewClaims.html");
        task = createRoleTask("Widerspruch prüfen", "Widerspruch erneut prüfen auf neue Ansprüche", form,
            objectionClerk);
        human2 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // XOR Split, condition is new relevant aspects existence
        xor2 = BpmnNodeFactory.createBpmnXorGatewayNode(builder);
        map1 = new HashMap<String, Object>();
        map1.put("neue Aspekte", "ja");
        Condition condition3 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("neue Aspekte", "nein");
        Condition condition4 = new HashMapCondition(map2, "==");

        // human task for objection clerk, task is to create a new report
        form = extractForm("form3", "createReport.html");
        task = createRoleTask("neues Gutachten erstellen", "Anspruchspunkte in neues Gutachten übertragen", form,
            objectionClerk);
        human3 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // intermediate mail event, customer answer
        // needs to be implemented and inserted here

        // XOR Split, condition is existence of objection in answer of customer
        xor3 = BpmnNodeFactory.createBpmnXorGatewayNode(builder);
        map1 = new HashMap<String, Object>();
        map1.put("aufrecht", "ja");
        Condition condition5 = new HashMapCondition(map1, "==");
        map2 = new HashMap<String, Object>();
        map2.put("aufrecht", "nein");
        Condition condition6 = new HashMapCondition(map2, "==");

        // XOR Join
        xor4 = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // human task for objection clerk, task is to do final work
        form = extractForm("form4", "postEditingClaim.html");
        task = createRoleTask("Nachbearbeitung", "abschließende Nachbearbeitung des Falls", form, objectionClerk);
        human4 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // human task for allowance clerk, task is to enforce allowance
        form = extractForm("form5", "enforceAllowance.html");
        task = createRoleTask("Leistungsgewährung umsetzen", "Leistungsansprüche durchsetzen", form, allowanceClerk);
        human5 = BpmnNodeFactory.createBpmnUserTaskNode(builder, task);

        // final XOR Join
        xor5 = BpmnNodeFactory.createBpmnXorGatewayNode(builder);

        // system task, close file
        system2 = BpmnFunNodeFactory.createBpmnPrintingVariableNode(builder, "Akte wird geschlossen");

        // end node
        endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        // connect the nodes
        BpmnNodeFactory.createTransitionFromTo(builder, startNode, system1);
        BpmnNodeFactory.createTransitionFromTo(builder, system1, human1);
        BpmnNodeFactory.createTransitionFromTo(builder, human1, xor1);
        BpmnNodeFactory.createTransitionFromTo(builder, xor1, human2, condition2);
        BpmnNodeFactory.createTransitionFromTo(builder, xor1, human5, condition1);
        BpmnNodeFactory.createTransitionFromTo(builder, human2, xor2);
        BpmnNodeFactory.createTransitionFromTo(builder, xor2, human3, condition3);
        BpmnNodeFactory.createTransitionFromTo(builder, xor2, xor4, condition4);
        BpmnNodeFactory.createTransitionFromTo(builder, human3, xor3);
        BpmnNodeFactory.createTransitionFromTo(builder, xor3, xor4, condition5);
        BpmnNodeFactory.createTransitionFromTo(builder, xor3, xor5, condition6);
        BpmnNodeFactory.createTransitionFromTo(builder, xor4, human4);
        BpmnNodeFactory.createTransitionFromTo(builder, human4, human5);
        BpmnNodeFactory.createTransitionFromTo(builder, xor5, system2);
        BpmnNodeFactory.createTransitionFromTo(builder, human5, xor5);
        BpmnNodeFactory.createTransitionFromTo(builder, system2, endNode);

        builder.setName("Shortened Reference Process").setDescription("Shortened Reference Process");
    }

    /**
     * Creates the participants.
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
     * @param form the form
     * @param resource the resource
     * @return the task
     */
    private static Task createRoleTask(String subject, String description, Form form, AbstractResource<?> resource) {

        AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(new RolePushPattern(),
            new SimplePullPattern(), null, null);

        return createTask(subject, description, form, allocationStrategies, resource);
    }

    /**
     * Creates the task - qucikn dirty helper.
     *
     * @param subject the subject
     * @param description the description
     * @param form the form
     * @param allocationStrategies the allocation strategies
     * @param resource the resource
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
     * @param formName the form name
     * @param formPath the form path
     * @return the form
     * @throws DefinitionNotFoundException the definition not found exception
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
     * @throws IllegalStarteventException the illegal startevent exception
     * @throws DefinitionNotFoundException the definition not found exception
     */
    public static synchronized void generate()
    throws IllegalStarteventException, DefinitionNotFoundException {

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
