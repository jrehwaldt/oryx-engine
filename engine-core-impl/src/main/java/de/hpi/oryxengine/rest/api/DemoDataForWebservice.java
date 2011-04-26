package de.hpi.oryxengine.rest.api;

import java.io.File;
import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Form;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.resource.allocation.FormImpl;
import de.hpi.oryxengine.resource.allocation.TaskImpl;
import de.hpi.oryxengine.resource.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.resource.allocation.pattern.SimplePullPattern;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */

public final class DemoDataForWebservice {

    private static final String PATH_TO_WEBFORMS = "/Users/Gery/Entwicklung/BachelorprojektWorkspace/Oryx-Engine-Git/oryx_engine/engine-apps/src/main/java/de/hpi/oryxengine/webforms";
    private static IdentityBuilder builder;
    private static AbstractRole r;
    private final static int NUMBER_OF_PROCESSINSTANCES = 10;

    /**
     * Instantiates a new demo data for webservice.
     */
    private DemoDataForWebservice() {

    }

    /**
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        if (builder == null) {
            builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        }
        return builder;
    }

    /**
     * Generate example Participants.
     */
    public static void generate() {

        generateDemoParticipants();
        try {
            generateDemoWorklistItems();
        } catch (DefinitionNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate demo participants.
     */
    private static void generateDemoParticipants() {

        r = getBuilder().createRole("demo");
        AbstractParticipant p1 = getBuilder().createParticipant("Peter");
        AbstractParticipant p2 = getBuilder().createParticipant("Pfeiffer");
        AbstractParticipant p3 = getBuilder().createParticipant("Kumpel von Pfeiffer");
        AbstractParticipant p4 = getBuilder().createParticipant("Pfeiffers Hund");
        AbstractParticipant p5 = getBuilder().createParticipant("Pfeiffers Mutter");
        AbstractParticipant p6 = getBuilder().createParticipant("Pfeiffers Vaddi");
        getBuilder().participantBelongsToRole(p1.getID(), r.getID()).participantBelongsToRole(p2.getID(), r.getID())
        .participantBelongsToRole(p3.getID(), r.getID()).participantBelongsToRole(p4.getID(), r.getID())
        .participantBelongsToRole(p5.getID(), r.getID()).participantBelongsToRole(p6.getID(), r.getID());

    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws IllegalStarteventException
     * @throws DefinitionNotFoundException
     */
    private static void generateDemoWorklistItems()
    throws IllegalStarteventException, DefinitionNotFoundException {

        // TODO Use Example Process to create some tasks for the role demo

        ProcessBuilderImpl processBuilder = new ProcessBuilderImpl();
        Node startNode, node1, node2, node3, endNode;
        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
        startNode = processBuilder.createStartNode(nodeParamBuilder.buildNodeParameterAndClear());

        // Creating the Webform for the task
        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        UUID processArtifactID = deploymentBuilder.deployArtifactAsFile("form1", new File(PATH_TO_WEBFORMS + "/Form1.html"));
        Form form = new FormImpl(ServiceFactory.getRepositoryService().getProcessResource(processArtifactID));

        // Create the task
        AllocationStrategies strategies = new AllocationStrategiesImpl(new DirectPushPattern(),
            new SimplePullPattern(), null, null);

        Task task = new TaskImpl("do something", "Really do something we got a demo coming up guys!", form, strategies, r);

        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);
        node1 = processBuilder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);

        node2 = processBuilder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        // Create the task
        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);

        node3 = processBuilder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        processBuilder.createTransition(startNode, node1).createTransition(node1, node2).createTransition(node2, node3);

        nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setActivityBlueprintFor(EndActivity.class);
        endNode = processBuilder.createNode(nodeParamBuilder.buildNodeParameter());
        processBuilder.createTransition(node3, endNode).setName("Demoprocess")
        .setDescription("A wonderful demo process definition by Master Jannik himself");

        // Start Process
        ProcessDefinition processDefinition = processBuilder.buildDefinition();

        UUID processID = ServiceFactory.getRepositoryService().getDeploymentBuilder()
        .deployProcessDefinition(new RawProcessDefintionImporter(processDefinition));
        // Tobi wants more tasks
        for (int i = 0; i < NUMBER_OF_PROCESSINSTANCES; i++) {
            ServiceFactory.getNavigatorService().startProcessInstance(processID);
        }

    }
}
