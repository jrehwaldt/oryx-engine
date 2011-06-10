package org.jodaengine.repository;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.structure.Node;


/**
 * It helps previously fill the RepositoryService.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    private static ProcessDefinitionID process1Plus1ProcessUUID;

    /**
     * Gets the process1 plus1 process uuid.
     *
     * @return the process1 plus1 process uuid
     */
    public static ProcessDefinitionID getProcess1Plus1ProcessID() {
    
        return process1Plus1ProcessUUID;
    }

    /**
     * Sets the process1 plus1 process uuid.
     *
     * @param process1Plus1ProcessUUID the new process1 plus1 process uuid
     */
    public static void setProcess1Plus1ProcessID(ProcessDefinitionID process1Plus1ProcessUUID) {
    
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

        ProcessDefinition definition = get1Plus1Process();
        deploymentBuilder.addProcessDefinition(definition);
        repo.deployInNewScope(deploymentBuilder.buildDeployment());
                
        process1Plus1ProcessUUID = definition.getID();
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

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();
        
        Node startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);
        
        int[] integers = {1, 1 };
        Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        
        BpmnNodeFactory.createControlFlowFromTo(builder, startNode, node1);
        BpmnNodeFactory.createControlFlowFromTo(builder, node1, node2);
        
        builder.setName(processName).setDescription(processDescription);
        
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        
        return builder.buildDefinition();
    }
}
