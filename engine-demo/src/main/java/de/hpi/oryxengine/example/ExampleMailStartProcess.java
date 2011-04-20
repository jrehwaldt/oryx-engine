package de.hpi.oryxengine.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.PrintingVariableActivity;
import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.correlation.adapter.EventTypes;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.mail.MailAdapterEvent;
import de.hpi.oryxengine.correlation.registration.EventCondition;
import de.hpi.oryxengine.correlation.registration.EventConditionImpl;
import de.hpi.oryxengine.correlation.registration.StartEvent;
import de.hpi.oryxengine.correlation.registration.StartEventImpl;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.NavigatorImpl;
import de.hpi.oryxengine.plugin.navigator.NavigatorListenerLogger;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class ExampleMailStartProcess. This is an example process that is started
 * by an arriving email.
 */
public final class ExampleMailStartProcess {

    /** no public/default constructor for this Demothingie. */
    private ExampleMailStartProcess() {

    };

    /**
     * Creating and starting the example process. The example process is
     * triggered by an Email.
     */
    public static void main(String... args) throws IllegalStarteventException {

	String exampleProcessName = "exampleMailProcess";

	// the main
	OryxEngine.start();
	NavigatorImpl navigator = (NavigatorImpl) ServiceFactory
		.getNavigatorService();
	navigator.registerPlugin(NavigatorListenerLogger.getInstance());

	DeploymentBuilder deploymentBuilder = ServiceFactory
		.getRepositoryService().getDeploymentBuilder();

	// Building the ProcessDefintion
	ProcessDefinitionBuilder builder = new ProcessBuilderImpl();

	// Building Node1
	NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl();
	int[] ints = {1, 1};
	nodeParamBuilder
		.setActivityBlueprintFor(AddNumbersAndStoreActivity.class)
		.addConstructorParameter(String.class, "result")
		.addConstructorParameter(int[].class, ints);
	Node node1 = builder.createStartNode(nodeParamBuilder
		.buildNodeParameter());

	// Building Node2
	nodeParamBuilder = new NodeParameterBuilderImpl();
	nodeParamBuilder.setActivityBlueprintFor(PrintingVariableActivity.class)
		.addConstructorParameter(String.class, "result");
	Node node2 = builder.createNode(nodeParamBuilder.buildNodeParameter());

	builder.createTransition(node1, node2).setDescription("description")
		.setName(exampleProcessName);

	ProcessDefinition def = builder.buildDefinition();
	UUID exampleProcessUUID = deploymentBuilder
		.deployProcessDefinition(new RawProcessDefintionImporter(def));

	// Create a mail adapater event here.
	// TODO @TobiP Could create a builder for this later.
	MailAdapterConfiguration config = MailAdapterConfiguration
		.dalmatinaGoogleConfiguration();
	EventCondition subjectCondition = null;
	try {
	    subjectCondition = new EventConditionImpl(
		    MailAdapterEvent.class.getMethod("getMessageTopic"),
		    "Hallo");
	} catch (SecurityException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	}
	List<EventCondition> conditions = new ArrayList<EventCondition>();
	conditions.add(subjectCondition);

	StartEvent event = new StartEventImpl(EventTypes.Mail, config,
		conditions, exampleProcessUUID);

	try {
	    builder.createStartTrigger(event, node1);
	} catch (DalmatinaException e) {
	    e.printStackTrace();
	}

	navigator.start();

	// Thread.sleep(SLEEP_TIME);

	// navigator.stop();
    }
}
