package de.hpi.oryxengine.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.bootstrap.JodaEngine;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.EventConditionImpl;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.registration.StartEventImpl;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineRuntimeException;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.node.factory.bpmn.BpmnCustomNodeFactory;
import de.hpi.oryxengine.node.factory.bpmn.BpmnNodeFactory;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;

/**
 * The Class ExampleMailStartProcess. This is an example process that is started
 * by an arriving email.
 */
public final class ExampleMailStartProcess {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleMailStartProcess.class);
	
	/** no public/default constructor for this Demothingie. */
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
	public static void main(String... args) throws IllegalStarteventException {

		String exampleProcessName = "exampleMailProcess";

		// the main
		JodaEngine.start();
		NavigatorImpl navigator = (NavigatorImpl) ServiceFactory
				.getNavigatorService();
		navigator.registerPlugin(NavigatorListenerLogger.getInstance());

		DeploymentBuilder deploymentBuilder = ServiceFactory
				.getRepositoryService().getDeploymentBuilder();

		// Building the ProcessDefintion
		ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

		Node startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

		// Building Node1
		int[] ints = {1, 1};
		Node node1 = BpmnCustomNodeFactory
				.createBpmnAddNumbersAndStoreNode(builder, "result", ints);

		// Building Node2
		Node node2 = BpmnCustomNodeFactory
				.createBpmnPrintingVariableNode(builder, "result");

		BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
		BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);

		builder.setDescription("description").setName(exampleProcessName);

		ProcessDefinition def = builder.buildDefinition();
		UUID exampleProcessUUID = deploymentBuilder
				.deployProcessDefinition(new RawProcessDefintionImporter(def));

		// Create a mail adapater event here.
		// TODO @TobiP Could create a builder for this later.
		MailAdapterConfiguration config = MailAdapterConfiguration
				.dalmatinaGoogleConfiguration();
		EventCondition subjectCondition = null;
		try {
			subjectCondition = new EventConditionImpl(MailAdapterEvent.class.getMethod("getMessageTopic"),
														"Hallo");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		List<EventCondition> conditions = new ArrayList<EventCondition>();
		conditions.add(subjectCondition);

		StartEvent event = new StartEventImpl(EventTypes.Mail,
												config,
												conditions,
												exampleProcessUUID);

		try {
			builder.createStartTrigger(event, node1);
		} catch (JodaEngineRuntimeException e) {
			
			LOGGER.error(e.getMessage(), e);
		}

		navigator.start();

		// Thread.sleep(SLEEP_TIME);

		// navigator.stop();
	}
}
