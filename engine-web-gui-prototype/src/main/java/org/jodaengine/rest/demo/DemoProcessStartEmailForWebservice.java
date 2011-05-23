package org.jodaengine.rest.demo;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.eventmanagement.adapter.EventTypes;
import org.jodaengine.eventmanagement.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.eventmanagement.adapter.mail.MailAdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.MethodInvokingEventCondition;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.structure.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class DemoDataForWebservice generates some example data when called.
 */

public final class DemoProcessStartEmailForWebservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoProcessStartEmailForWebservice.class);
    private static boolean invoked = false;

    /**
     * Instantiates a new demo data for webservice.
     */
    private DemoProcessStartEmailForWebservice() {

    }

    /**
     * Resets invoked, to be honest mostly for testing purposed after each method.
     */
    public synchronized static void resetInvoked() {

        invoked = false;
    }

    /**
     * Generate example Participants.
     */
    public static synchronized void generate() {

        if (!invoked) {
            invoked = true;
            generateDemoParticipants();
            try {
                generateDemoWorklistItems();
            } catch (DefinitionNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalStarteventException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Generate demo participants.
     */
    private static void generateDemoParticipants() {

    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws IllegalStarteventException
     *             illegal start event
     * @throws DefinitionNotFoundException
     *             no such definition found in repo
     */
    private static void generateDemoWorklistItems()
    throws IllegalStarteventException, DefinitionNotFoundException {

        // TODO why not use the getBuilder-method? special reason?
        // Building the ProcessDefintion
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node startNode, node1, node2, endNode;

        startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        // Building Node1
        int[] ints = {1, 1};
        node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

        // Building Node2
        node2 = BpmnCustomNodeFactory.createBpmnPrintingVariableNode(builder, "result");

        endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);
        BpmnNodeFactory.createTransitionFromTo(builder, node2, endNode);

        builder.setDescription("description").setName("Demoprocess with Email start event");

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition def = builder.buildDefinition();
        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        deploymentBuilder.addProcessDefinition(def);

        Deployment deployment = deploymentBuilder.buildDeployment();
        ServiceFactory.getRepositoryService().deployInNewScope(deployment);
        
        // Create a mail adapater event here.
        // TODO @TobiP Could create a builder for this later.
        InboundMailAdapterConfiguration config = InboundMailAdapterConfiguration.jodaGoogleConfiguration();
        EventCondition subjectCondition = null;
        try {
            subjectCondition = new MethodInvokingEventCondition(MailAdapterEvent.class, "getMessageTopic", "Hallo");
            List<EventCondition> conditions = new ArrayList<EventCondition>();
            conditions.add(subjectCondition);

            // StartEvent event = new StartEventImpl( exampleProcessUUID);

            builder.createStartTrigger(EventTypes.Mail, config, conditions, node1);

            ServiceFactory.getRepositoryService().activateProcessDefinition(def.getID());
        } catch (JodaEngineRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
