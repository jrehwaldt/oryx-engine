package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.ProcessDefinitionImporter;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * It helps previously fill the RepositoryService.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    private static UUID process1Plus1ProcessUUID;

    /**
     * Gets the process1 plus1 process uuid.
     *
     * @return the process1 plus1 process uuid
     */
    public static UUID getProcess1Plus1ProcessUUID() {
    
        return process1Plus1ProcessUUID;
    }

    /**
     * Sets the process1 plus1 process uuid.
     *
     * @param process1Plus1ProcessUUID the new process1 plus1 process uuid
     */
    public static void setProcess1Plus1ProcessUUID(UUID process1Plus1ProcessUUID) {
    
        RepositorySetup.process1Plus1ProcessUUID = process1Plus1ProcessUUID;
    }

    /**
     * Fill repository with one process definition.
     * 
     * @throws IllegalStarteventException
     *             if there is no start event, the exception is thrown
     */
    public static void fillRepository()
    throws IllegalStarteventException {

        RepositoryService repo = ServiceFactory.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();

        // Deploying the process with a simple ProcessImporter
        ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(get1Plus1Process());
        process1Plus1ProcessUUID = deploymentBuilder.deployProcessDefinition(rawProDefImporter);
    }

    /**
     * Creates the process 'Plus1Process'.
     * 
     * @return the process definition
     * @throws IllegalStarteventException
     *             if there is no start event, the exception is thrown
     */
    private static ProcessDefinition get1Plus1Process()
    throws IllegalStarteventException {

        String processName = "1Plus1Process";
        String processDescription = "The process stores the result of the calculation '1 + 1' .";

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        
        Node startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);
        
        int[] integers = {1, 1 };
        Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        
        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        
        builder.setName(processName).setDescription(processDescription);
        BpmnProcessDefinitionModifier.decorateWithNormalBpmnProcessInstantiation(builder);
        
        return builder.buildDefinition();
    }
}
