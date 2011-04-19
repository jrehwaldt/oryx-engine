package de.hpi.oryxengine.rest;

import java.util.UUID;

import org.testng.Assert;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * Helper class for web server tests.
 * 
 * @author Jan Rehwaldt
 */
public final class TestUtils {
    
    /**
     * Hidden constructor.
     */
    private TestUtils() { }
    
    /**
     * Creates and deploys a simple test process.
     * 
     * @return the deployed process
     * @throws IllegalStarteventException thrown if the start event is missing
     */
    public static ProcessDefinition deploySimpleProcess()
    throws IllegalStarteventException {

        // create simple process
        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(
            new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
        Node startNode = builder.createStartNode(nodeParamBuilder.buildNodeParameter());
        
        nodeParamBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        int[] ints = {1, 1};
        nodeParamBuilder
            .setActivityBlueprintFor(AddNumbersAndStoreActivity.class)
            .addConstructorParameter(String.class, "result")
            .addConstructorParameter(int[].class, ints);
        Node node1 = builder.createNode(nodeParamBuilder.buildNodeParameter());
        Node node2 = builder.createNode(nodeParamBuilder.buildNodeParameter());
        builder.createTransition(startNode, node1).createTransition(node1, node2);
        
        nodeParamBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        nodeParamBuilder.setActivityBlueprintFor(EndActivity.class);
        Node endNode = builder.createNode(nodeParamBuilder.buildNodeParameter());
        builder.createTransition(node2, endNode);

        // deploy it
        ProcessDefinition definition = builder.buildDefinition();
        Assert.assertNotNull(definition);

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        UUID processId = deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));

        Assert.assertNotNull(processId);
        Assert.assertEquals(processId, definition.getID());
        
        return definition;
    }
}
