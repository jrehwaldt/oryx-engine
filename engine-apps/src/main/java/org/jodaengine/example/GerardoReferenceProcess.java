package org.jodaengine.example;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.start.ImapEmailStartProcessEvent;
import org.jodaengine.eventmanagement.processevent.incoming.start.TimerStartProcessEvent;
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
import org.jodaengine.process.instantiation.StartProcessInstantiationPattern;
import org.jodaengine.process.instantiation.pattern.EventBasedInstanceCreationPattern;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the implementation of the reference process of Gerardo Navarro Suarez for his bachelor thesis.
 * 
 * It represents the process that can be found under
 * http://academic.signavio.com/p/model/2b366d2e566844d8865fd15eaf1ec4f6
 * /png?inline&authkey=ed9ab238cc1f3cec80e170e618329157139d7c1573c769c49418aeba5c6571a
 * 
 */
public final class GerardoReferenceProcess {

    private static final int START_INTERVAL = 5000;

    private static final int TIME_INTERMEDIATE_EVENT_TIME_1 = 5000;

    private static final int INCOMING_MESSAGE_TIME = 3000;
    private static final int TIME_INTERMEDIATE_EVENT_TIME_2 = 15000;


    private static final Logger LOGGER = LoggerFactory.getLogger(GerardoReferenceProcess.class);

    private static final String GERARDO_REFERENCE_PROCESS_NAME = "GerardoReferenceProcess";
    private static final String GERARDO_REFERENCE_PROCESS_DESCRIPTION =
        "This is the implementation of the reference process of Gerardo Navarro Suarez for his bachelor thesis.";

    /** Hidden constructor. */
    private GerardoReferenceProcess() {

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

            // the main
            JodaEngineServices jodaEngineServices = JodaEngine.start();

            ((NavigatorImpl) jodaEngineServices.getNavigatorService()).registerListener(new NavigatorListenerLogger());

            ProcessDefinition def = buildProcessDefinition();

            // Building the ProcessDefintion
            DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();

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

    private static ProcessDefinition buildProcessDefinition() throws IllegalStarteventException {

        Node startIncomingMessageEventNode, startTimerEventNode, xorJoin, dummyNode1, intermdiateTimerEvent1, dummyNode2, eventBasedGateway, intermdiateIncomingMessageEvent, intermdiateTimerEvent2, dummyNode3, endNode;

        BpmnProcessDefinitionBuilder builder = BpmnProcessDefinitionBuilder.newBuilder();

        builder.setDescription(GERARDO_REFERENCE_PROCESS_DESCRIPTION).setName(GERARDO_REFERENCE_PROCESS_NAME);

        startIncomingMessageEventNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        startTimerEventNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);
        xorJoin = BpmnNodeFactory.createBpmnXorGatewayNode(builder);
        dummyNode1 = BpmnCustomNodeFactory.createBpmnPrintingNode(builder, "Antrag bearbeiten");
        intermdiateTimerEvent1 = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, TIME_INTERMEDIATE_EVENT_TIME_1);
        dummyNode2 = BpmnCustomNodeFactory.createBpmnPrintingNode(builder, "Versicherungsbeitrag berechnen");
        eventBasedGateway = BpmnNodeFactory.createBpmnEventBasedXorGatewayNode(builder);
        intermdiateIncomingMessageEvent = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, INCOMING_MESSAGE_TIME);
        intermdiateTimerEvent2 = BpmnNodeFactory.createBpmnIntermediateTimerEventNode(builder, TIME_INTERMEDIATE_EVENT_TIME_2);
        dummyNode3 = BpmnCustomNodeFactory.createBpmnPrintingNode(builder, "Antwort formulieren");
        endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createControlFlowFromTo(builder, startIncomingMessageEventNode, xorJoin);
        BpmnNodeFactory.createControlFlowFromTo(builder, startTimerEventNode, xorJoin);
        BpmnNodeFactory.createControlFlowFromTo(builder, xorJoin, dummyNode1);
        BpmnNodeFactory.createControlFlowFromTo(builder, dummyNode1, intermdiateTimerEvent1);
        BpmnNodeFactory.createControlFlowFromTo(builder, intermdiateTimerEvent1, dummyNode2);
        BpmnNodeFactory.createControlFlowFromTo(builder, dummyNode2, eventBasedGateway);
        BpmnNodeFactory.createControlFlowFromTo(builder, eventBasedGateway, intermdiateIncomingMessageEvent);
        BpmnNodeFactory.createControlFlowFromTo(builder, eventBasedGateway, intermdiateTimerEvent2);
        BpmnNodeFactory.createControlFlowFromTo(builder, intermdiateIncomingMessageEvent, dummyNode3);
        BpmnNodeFactory.createControlFlowFromTo(builder, intermdiateTimerEvent2, dummyNode2);
        BpmnNodeFactory.createControlFlowFromTo(builder, dummyNode3, endNode);

        // Setting the star events
        EventCondition subjectCondition = new MethodInvokingEventCondition(MailAdapterEvent.class, "getMessageSubject",
            "Antrag");
        builder.createStartTrigger(new ImapEmailStartProcessEvent(subjectCondition, null), startIncomingMessageEventNode);

        builder.createStartTrigger(new TimerStartProcessEvent(START_INTERVAL, null), startTimerEventNode);

        ProcessDeActivationPattern activationPattern = new RegisterAllStartEventPattern();
        builder.addDeActivationPattern(activationPattern);
        
        StartProcessInstantiationPattern startInstantiationPattern = new EventBasedInstanceCreationPattern();
        builder.addStartInstantiationPattern(startInstantiationPattern);
        
        return builder.buildDefinition();
    }
}
