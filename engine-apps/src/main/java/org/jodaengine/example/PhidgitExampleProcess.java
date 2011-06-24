package org.jodaengine.example;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.activation.pattern.NullProcessDefinitionActivationPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.jodaengine.process.instantiation.pattern.DefaultBpmnProcessInstanceCreationPattern;
import org.jodaengine.process.structure.Node;

/**
 * The Class PhidgitExampleProcess.
 */
public class PhidgitExampleProcess {
    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IllegalStarteventException the illegal startevent exception
     */
    public static void main(String... args)
    throws IllegalStarteventException {

        try {
            String exampleProcessName = "exampleTweetProcess";

            // Start all our brilliant services.
            JodaEngineServices jodaEngineServices = JodaEngine.start();

            // Building the ProcessDefintion
            DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
            BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

            Node startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);
            Node node1 = BpmnCustomNodeFactory.createPhidgetNode(builder, 2);
            Node endNode = BpmnCustomNodeFactory.createTwitterEndEventNode(builder);

            BpmnNodeFactory.createControlFlowFromTo(builder, startNode, node1);
            BpmnNodeFactory.createControlFlowFromTo(builder, node1, endNode);

            builder.setDescription("A beautiful process which is so kind to tell the whole world when it's finished.")
                .setName(exampleProcessName);

            // some patterns that have to be set, those are kind of default values.
            StartProcessInstantiationPattern startInstantiationPattern = new DefaultBpmnProcessInstanceCreationPattern();
            builder.addStartInstantiationPattern(startInstantiationPattern);
            ProcessDeActivationPattern activationPattern = new NullProcessDefinitionActivationPattern();
            builder.addDeActivationPattern(activationPattern);

            // BUILD!
            ProcessDefinition def = builder.buildDefinition();

            ProcessDefinitionID exampleProcessUUID = def.getID();
            deploymentBuilder.addProcessDefinition(def);
            jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());
            jodaEngineServices.getRepositoryService().activateProcessDefinition(exampleProcessUUID);

            // START!
            jodaEngineServices.getNavigatorService().startProcessInstance(exampleProcessUUID);

        } catch (Exception exception) {

            String errorMessage = "An Exception occurred: " + exception.getMessage();
            System.out.println(errorMessage);
        }
    }

}
