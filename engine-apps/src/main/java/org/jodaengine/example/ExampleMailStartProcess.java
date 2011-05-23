package org.jodaengine.example;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.ext.logger.NavigatorListenerLogger;
import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
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

            ((NavigatorImpl) jodaEngineServices.getNavigatorService()).registerListener(NavigatorListenerLogger
            .getInstance());

            // Building the ProcessDefintion
            DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
            ProcessDefinitionBuilder builder = deploymentBuilder.getProcessDefinitionBuilder();

            Node startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

            // Building Node1
            int[] ints = { 1, 1 };
            Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

            // Building Node2
            Node node2 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(builder, "result");

            BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
            BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);

            builder.setDescription("description").setName(exampleProcessName);

            // Create a mail adapater event here.
            // TODO @TobiP Could create a builder for this later.
            InboundMailAdapterConfiguration config = InboundMailAdapterConfiguration.jodaGoogleConfiguration();
            EventCondition subjectCondition = new MethodInvokingEventCondition(MailAdapterEvent.class, "getMessageTopic", "Hallo");
            List<EventCondition> conditions = new ArrayList<EventCondition>();
            conditions.add(subjectCondition);

            // StartEvent event = new StartEventImpl( EventTypes.Mail,
            // config,
            // conditions,
            // exampleProcessUUID);

            builder.createStartTrigger(EventTypes.Mail, config, conditions, startNode);
            ProcessDefinition def = builder.buildDefinition();

            ProcessDefinitionID exampleProcessUUID = def.getID();
            deploymentBuilder.addProcessDefinition(def);
            
            jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());

            jodaEngineServices.getRepositoryService().activateProcessDefinition(exampleProcessUUID);

            jodaEngineServices.getNavigatorService().startProcessInstance(exampleProcessUUID);

            // Thread.sleep(SLEEP_TIME);

        } catch (Exception exception) {

            String errorMessage = "An Exception occurred: " + exception.getMessage();
            LOGGER.error(errorMessage, exception);
        }
    }
}
