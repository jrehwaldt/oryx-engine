package org.jodaengine.rest;

import java.util.UUID;

import org.testng.Assert;

import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.RawProcessDefintionImporter;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.structure.Node;

/**
 * Helper class for web server tests.
 * 
 * @author Jan Rehwaldt
 */
public final class TestUtils {

    /**
     * Hidden constructor.
     */
    private TestUtils() {

    }

    /**
     * Creates and deploys a simple test process.
     * 
     * @return the deployed process
     * @throws IllegalStarteventException
     *             thrown if the start event is missing
     */
    public static ProcessDefinition deploySimpleProcess()
    throws IllegalStarteventException {

        // create simple process
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        int[] ints = {1, 1};
        Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);
        Node node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, endNode);

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);

        // deploy it
        ProcessDefinition definition = builder.buildDefinition();
        Assert.assertNotNull(definition);

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        Deployment deployment = deploymentBuilder.addProcessDefinition(definition).buildDeployment();
        ServiceFactory.getRepositoryService().deployInNewScope(deployment);
        ProcessDefinitionID processId = definition.getID();

        Assert.assertNotNull(processId);
        Assert.assertEquals(processId, definition.getID());

        return definition;
    }
}
