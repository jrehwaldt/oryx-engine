package org.jodaengine.rest.demo;

import java.io.File;
import java.util.UUID;

import org.jodaengine.IdentityService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.CreationPattern;
import org.jodaengine.allocation.Form;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.RawProcessDefintionImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.structure.Node;
import org.jodaengine.resource.IdentityBuilder;
import org.jodaengine.resource.Role;
import org.jodaengine.resource.allocation.CreationPatternBuilder;
import org.jodaengine.resource.allocation.CreationPatternBuilderImpl;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.resource.allocation.pattern.OfferMultiplePattern;

/**
 * This class deploys the benchmark process as specified in signavio.
 */
public final class BenchmarkDeployer {
    private static boolean invoked = false;
    private static final String PATH_TO_WEBFORMS = "src/main/resources/forms/benchmark";

    private static IdentityService identityService = ServiceFactory.getIdentityService();
    private static ProcessDefinitionBuilder processDefinitionBuilder = new ProcessDefinitionBuilderImpl();
    private static IdentityBuilder identityBuilder = identityService.getIdentityBuilder();

    private static Role roleA, roleB, roleC, roleD, roleE;

    /**
     * Hidden default constructor.
     */
    private BenchmarkDeployer() {

    }

    /**
     * Creates five roles for the benchmark process.
     */
    public static void createRoles() {

        roleA = (Role) identityBuilder.createRole("A");
        roleB = (Role) identityBuilder.createRole("B");
        roleC = (Role) identityBuilder.createRole("C");
        roleD = (Role) identityBuilder.createRole("D");
        roleE = (Role) identityBuilder.createRole("E");
    }

    /**
     * Creates the nodes as specified in the process model.
     * 
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     */
    public static void initializeNodes()
    throws DefinitionNotFoundException {

        Node startNode = BpmnNodeFactory.createBpmnStartEventNode(processDefinitionBuilder);

        Node andSplit1 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);
        Node andSplit2 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);
        Node andSplit3 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);
        Node andJoin1 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);
        Node andJoin2 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);
        Node andJoin3 = BpmnNodeFactory.createBpmnAndGatewayNode(processDefinitionBuilder);

        Form form = extractForm("dummyform", "dummy.html");
        CreationPatternBuilder builder = new CreationPatternBuilderImpl();
        builder.setItemSubject("Do stuff")
               .setItemDescription("Do it")
               .setItemForm(form)
               .addResourceAssignedToItem(roleA);
        CreationPattern patternA = builder.buildConcreteResourcePattern();

        builder.flushAssignedResources().addResourceAssignedToItem(roleB);
        CreationPattern patternB = builder.buildConcreteResourcePattern();

        builder.flushAssignedResources().addResourceAssignedToItem(roleC);
        CreationPattern patternC = builder.buildConcreteResourcePattern();

        builder.flushAssignedResources().addResourceAssignedToItem(roleD);
        CreationPattern patternD = builder.buildConcreteResourcePattern();

        builder.flushAssignedResources().addResourceAssignedToItem(roleE);
        CreationPattern patternE = builder.buildConcreteResourcePattern();
        // Task roleATask = createRoleTask("Do stuff", "Do it", form, roleA);
        // Task roleBTask = createRoleTask("Do stuff", "Do it", form, roleB);
        // Task roleCTask = createRoleTask("Do stuff", "Do it", form, roleC);
        // Task roleDTask = createRoleTask("Do stuff", "Do it", form, roleD);
        // Task roleETask = createRoleTask("Do stuff", "Do it", form, roleE);

        Node activityA1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternA,
            new OfferMultiplePattern());
        Node activityB1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternB,
            new OfferMultiplePattern());
        Node activityB2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternB,
            new OfferMultiplePattern());
        Node activityB3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternB,
            new OfferMultiplePattern());
        Node activityC1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternC,
            new OfferMultiplePattern());
        Node activityC2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternC,
            new OfferMultiplePattern());
        Node activityD1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternD,
            new OfferMultiplePattern());
        Node activityD2 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternD,
            new OfferMultiplePattern());
        Node activityD3 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternD,
            new OfferMultiplePattern());
        Node activityE1 = BpmnNodeFactory.createBpmnUserTaskNode(processDefinitionBuilder, patternE,
            new OfferMultiplePattern());

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(processDefinitionBuilder);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, startNode, andSplit1);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit1, activityB1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit1, activityC1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit1, activityD1);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityB1, andSplit3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit3, activityB2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit3, activityA1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityA1, andJoin3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityB2, andJoin3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andJoin3, activityB3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityB3, andJoin1);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityC1, activityC2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityC2, andJoin1);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityD1, andSplit2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit2, activityD2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andSplit2, activityE1);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityE1, andJoin2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityD2, andJoin2);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andJoin2, activityD3);
        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, activityD3, andJoin1);

        BpmnNodeFactory.createTransitionFromTo(processDefinitionBuilder, andJoin1, endNode);

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(processDefinitionBuilder);

        processDefinitionBuilder.setName("Benchmark Process").setDescription(
            "This process has 5 roles, some parallel gateways and user tasks only.");
    }

    /**
     * Creates roles, nodes and deploys the resulting process definition.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     */
    public static synchronized void generate()
    throws IllegalStarteventException, DefinitionNotFoundException, ResourceNotAvailableException {

        if (!invoked) {
            createRoles();
            initializeNodes();
            ProcessDefinition definition = processDefinitionBuilder.buildDefinition();
            DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
            invoked = true;
        }
    }

    /**
     * Extracts a form.
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

    // private static Task createRoleTask(String subject, String description, Form form, AbstractResource<?> resource) {
    //
    // AllocationStrategies allocationStrategies = new AllocationStrategiesImpl(new ConcreteResourcePattern(),
    // new SimplePullPattern(), null, null);
    //
    // return createTask(subject, description, form, allocationStrategies, resource);
    // }
    //
    // private static Task createTask(String subject,
    // String description,
    // Form form,
    // AllocationStrategies allocationStrategies,
    // AbstractResource<?> resource) {
    //
    // Task task = new TaskImpl(subject, description, form, allocationStrategies, resource);
    // return task;
    // }
}
