package de.hpi.oryxengine.rest;

import java.util.UUID;

import org.testng.Assert;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;

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

        Node startNode = BPMNActivityFactory.createBPMNNullStartNode(builder);
        
        int[] ints = {1, 1};
        Node node1 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", ints);
        Node node2 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", ints);

        Node endNode = BPMNActivityFactory.createBPMNEndEventNode(builder);
        
        BPMNActivityFactory.createTransitionFromTo(builder, startNode, node1);
        BPMNActivityFactory.createTransitionFromTo(builder, node1, node2);
        BPMNActivityFactory.createTransitionFromTo(builder, node2, endNode);

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
