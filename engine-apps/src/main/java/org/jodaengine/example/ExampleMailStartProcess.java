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
import org.jodaengine.eventmanagement.processevent.incoming.start.ImapEmailProcessStartEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.logger.NavigatorListenerLogger;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.activation.ProcessDeActivationPattern;
import org.jodaengine.process.activation.pattern.RegisterAllStartEventPattern;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.definition.bpmn.BpmnProcessDefinitionBuilder;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.process.instantiation.pattern.EventBasedInstanceCreationPattern;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExampleMailStartProcess. This is an example process that is started
 * by an arriving email.
 */
public final class ExampleMailStartProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleMailStartProcess.class);

    /** Hidden constructor. */
    private ExampleMailStartProcess() {

    };

    /**
     * Creating and starting the example process. The example process is
     * triggered by an Email.
     * 
     * @param args
     *            the arguments
     * @throws IllegalStarteventException
     *             thrown if an illegal start event is given
     */
    public static void main(String... args)
    throws IllegalStarteventException {

        try {
            String exampleProcessName = "exampleMailProcess";

            // the main
            JodaEngineServices jodaEngineServices = JodaEngine.start();

            ((NavigatorImpl) jodaEngineServices.getNavigatorService()).registerListener(new NavigatorListenerLogger());

            // Building the ProcessDefintion
            DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
            BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

            Node startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

            // Building Node1
            int[] ints = {1, 1};
            Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

            // Building Node2
            Node node2 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(builder, "result");

            BpmnNodeFactory.createControlFlowFromTo(builder, startNode, node1);
            BpmnNodeFactory.createControlFlowFromTo(builder, node1, node2);

            builder.setDescription("description").setName(exampleProcessName);

            // Create a mail adapater here.
            // TODO @TobiP Could create a builder for this later.
            IncomingMailAdapterConfiguration config = IncomingMailAdapterConfiguration.jodaGoogleConfiguration();
            EventCondition subjectCondition = new MethodInvokingEventCondition(MailAdapterEvent.class,
                "getMessageSubject", "Hallo");
            List<EventCondition> conditions = new ArrayList<EventCondition>();
            conditions.add(subjectCondition);
           
            StartInstantiationPattern startInstantiationPattern = new EventBasedInstanceCreationPattern();
            builder.addStartInstantiationPattern(startInstantiationPattern);
            
            ProcessDeActivationPattern activationPattern = new RegisterAllStartEventPattern();
            builder.addActivationPattern(activationPattern);

            builder.createStartTrigger(new ImapEmailProcessStartEvent(subjectCondition, null), startNode);
            ProcessDefinition def = builder.buildDefinition();

            ProcessDefinitionID exampleProcessUUID = def.getID();
            deploymentBuilder.addProcessDefinition(def);

            jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());

            jodaEngineServices.getRepositoryService().activateProcessDefinition(exampleProcessUUID);

           // jodaEngineServices.getNavigatorService().startProcessInstance(exampleProcessUUID);

            // Thread.sleep(SLEEP_TIME);

        } catch (Exception exception) {

            String errorMessage = "An Exception occurred: " + exception.getMessage();
            LOGGER.error(errorMessage, exception);
        }
    }
}
