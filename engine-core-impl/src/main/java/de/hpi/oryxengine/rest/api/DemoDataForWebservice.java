package de.hpi.oryxengine.rest.api;

import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistManager;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.AllocationStrategiesImpl;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.allocation.pattern.DirectPushPattern;
import de.hpi.oryxengine.allocation.pattern.SimplePullPattern;
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
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */

public final class DemoDataForWebservice {

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
     * @throws IllegalStarteventException 
     * @throws DefinitionNotFoundException 
     */
    private static void generateDemoWorklistItems() throws IllegalStarteventException, DefinitionNotFoundException {

        // TODO Use Example Process to create some tasks for the role demo

        ProcessBuilderImpl builder = new ProcessBuilderImpl();
        Node startNode, node1, node2, node3, endNode;
        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
        startNode = builder.createStartNode(nodeParamBuilder.buildNodeParameterAndClear());

        // Create the task
        AllocationStrategies strategies = new AllocationStrategiesImpl(new DirectPushPattern(),
            new SimplePullPattern(), null, null);
        Task task = new TaskImpl("do something", "Really do something we got a demo coming up guys!", strategies, r);

        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);
        node1 = builder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);

        node2 = builder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        // Create the task
        nodeParamBuilder.setActivityBlueprintFor(HumanTaskActivity.class).addConstructorParameter(Task.class, task);

        node3 = builder.createNode(nodeParamBuilder.buildNodeParameterAndClear());

        builder.createTransition(startNode, node1).createTransition(node1, node2).createTransition(node2, node3);

        nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setActivityBlueprintFor(EndActivity.class);
        endNode = builder.createNode(nodeParamBuilder.buildNodeParameter());
        builder.createTransition(node3, endNode);
        
        //Start Process
        ProcessDefinition processDefinition = builder.buildDefinition();
        
        UUID processID = ServiceFactory
                            .getRepositoryService()
                            .getDeploymentBuilder()
                            .deployProcessDefinition(new RawProcessDefintionImporter(processDefinition));
        // Tobi wants more tasks
        for (int i = 0; i < NUMBER_OF_PROCESSINSTANCES; i++) {
        ServiceFactory.getNavigatorService().startProcessInstance(processID);
        }

    }

}
