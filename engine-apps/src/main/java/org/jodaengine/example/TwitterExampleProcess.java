package org.jodaengine.example;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.mail.IncomingMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.logger.NavigatorListenerLogger;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.activation.pattern.NullProcessDefinitionActivationPattern;
import org.jodaengine.process.activation.pattern.RegisterAllStartEventPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.instantiation.pattern.DefaultBpmnProcessInstanceCreationPattern;
import org.jodaengine.process.instantiation.pattern.EventBasedInstanceCreationPattern;
import org.jodaengine.process.structure.Node;

/**
 * An example process using the twitter shit and hopefulyl working.
 * 
 * TODO @Eventteam remove this vulgar langauge.
 */
public class TwitterExampleProcess {
    public static void main(String... args)
    throws IllegalStarteventException {

        try {
            String exampleProcessName = "exampleTweetProcess";

            // the main
            JodaEngineServices jodaEngineServices = JodaEngine.start();

            ((NavigatorImpl) jodaEngineServices.getNavigatorService()).registerListener(new NavigatorListenerLogger());

            // Building the ProcessDefintion
            DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
            BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

            Node startNode = BpmnNodeFactory.createBpmnStartEventNode(builder);


            Node node1 = BpmnCustomNodeFactory.createTwitterNode(builder);

            Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);
            

            BpmnNodeFactory.createControlFlowFromTo(builder, startNode, node1);
            BpmnNodeFactory.createControlFlowFromTo(builder, node1, endNode);

            builder.setDescription("description").setName(exampleProcessName);
       
            
            // TODO @EVENTTEAM: Ok I really dunno what the hell I am doing here,
            // the error messages told me so..
            StartInstantiationPattern startInstantiationPattern = new DefaultBpmnProcessInstanceCreationPattern();
            builder.addStartInstantiationPattern(startInstantiationPattern);
            
            ProcessDeActivationPattern activationPattern = new NullProcessDefinitionActivationPattern();
            builder.addActivationPattern(activationPattern);

            ProcessDefinition def = builder.buildDefinition();

            ProcessDefinitionID exampleProcessUUID = def.getID();
            deploymentBuilder.addProcessDefinition(def);

            jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());
            
            jodaEngineServices.getRepositoryService().activateProcessDefinition(exampleProcessUUID);

            jodaEngineServices.getNavigatorService().startProcessInstance(exampleProcessUUID);

            // Thread.sleep(SLEEP_TIME);

        } catch (Exception exception) {

            String errorMessage = "An Exception occurred: " + exception.getMessage();
            System.out.println(errorMessage);
        }
    }
}
