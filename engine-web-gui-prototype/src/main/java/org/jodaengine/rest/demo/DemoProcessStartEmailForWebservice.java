package org.jodaengine.rest.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jodaengine.ServiceFactory;
import org.jodaengine.correlation.adapter.EventTypes;
import org.jodaengine.correlation.adapter.mail.InboundMailAdapterConfiguration;
import org.jodaengine.correlation.adapter.mail.MailAdapterEvent;
import org.jodaengine.correlation.registration.EventCondition;
import org.jodaengine.correlation.registration.EventConditionImpl;
import org.jodaengine.deployment.importer.RawProcessDefintionImporter;
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
import org.jodaengine.resource.IdentityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */

public final class DemoProcessStartEmailForWebservice {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoProcessStartEmailForWebservice.class);
    private static final String PATH_TO_XML = "org/jodaengine/deployment/bpmn/SimpleUserTask.bpmn.xml";
    private static IdentityBuilder builder;
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
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return builder;
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
        int[] ints = { 1, 1 };
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
        UUID exampleProcessUUID = ServiceFactory.getRepositoryService().getDeploymentBuilder()
        .deployProcessDefinition(new RawProcessDefintionImporter(def));

        // Create a mail adapater event here.
        // TODO @TobiP Could create a builder for this later.
        InboundMailAdapterConfiguration config = InboundMailAdapterConfiguration.dalmatinaGoogleConfiguration();
        EventCondition subjectCondition = null;
        try {
            subjectCondition = new EventConditionImpl(MailAdapterEvent.class.getMethod("getMessageTopic"), "Hallo");
            List<EventCondition> conditions = new ArrayList<EventCondition>();
            conditions.add(subjectCondition);

            // StartEvent event = new StartEventImpl( exampleProcessUUID);

            builder.createStartTrigger(EventTypes.Mail, config, conditions, node1);

            ServiceFactory.getRepositoryService().activateProcessDefinition(exampleProcessUUID);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (JodaEngineRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
