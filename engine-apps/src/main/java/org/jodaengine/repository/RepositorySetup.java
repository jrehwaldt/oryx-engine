package org.jodaengine.repository;

import org.jodaengine.RepositoryService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.RawProcessDefintionImporter;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.bpmn.BpmnCustomNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnNodeFactory;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.structure.Node;

/**
 * It helps previously fill the RepositoryService.
 * 
 * ATTENTION: This class is duplicated (yes Gerardo it is) it is also present in the impl tests.
 * So if you notice a bug here be sure to correct it there to until we find a workaround.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    private static ProcessDefinitionID process1Plus1ProcessID;

    /**
     * Gets the process1 plus1 process uuid.
     * 
     * @return the process1 plus1 process uuid
     */
    public static ProcessDefinitionID getProcess1Plus1ProcessID() {

        return process1Plus1ProcessID;
    }

    /**
     * Sets the process1 plus1 process uuid.
     * 
     * @param process1Plus1ProcessUUID
     *            the new process1 plus1 process uuid
     */
    public static void setProcess1Plus1ProcessID(ProcessDefinitionID process1Plus1ProcessUUID) {

        RepositorySetup.process1Plus1ProcessID = process1Plus1ProcessUUID;
    }

    /**
     * Fill repository.
     * 
     * @throws IllegalStarteventException
     *             if there is no start event, the exception is thrown
     */
    public static void fillRepository()
    throws IllegalStarteventException {

        RepositoryService repo = ServiceFactory.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();

        // Deploying the process with a simple ProcessImporter
        ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(get1Plus1Process());
        process1Plus1ProcessID = deploymentBuilder.deployProcessDefinition(rawProDefImporter);
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

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        Node startNode = BpmnCustomNodeFactory.createBpmnNullStartNode(builder);

        int[] integers = { 1, 1 };
        Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);

        Node node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);

        BpmnNodeFactory.createTransitionFromTo(builder, startNode, node1);
        BpmnNodeFactory.createTransitionFromTo(builder, node1, node2);

        builder.setName(processName).setDescription(processDescription);

        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);

        return builder.buildDefinition();
    }
}

// package org.jodaengine.repository;
//
// import java.util.UUID;
//
// import org.jodaengine.RepositoryService;
// import org.jodaengine.ServiceFactory;
// import org.jodaengine.activity.impl.AddNumbersAndStoreActivity;
// import org.jodaengine.exception.IllegalStarteventException;
// import org.jodaengine.process.definition.NodeParameter;
// import org.jodaengine.process.definition.NodeParameterImpl;
// import org.jodaengine.process.definition.ProcessDefinitionBuilder;
// import org.jodaengine.process.definition.ProcessBuilderImpl;
// import org.jodaengine.process.definition.ProcessDefinition;
// import org.jodaengine.process.structure.ActivityBlueprint;
// import org.jodaengine.process.structure.ActivityBlueprintImpl;
// import org.jodaengine.process.structure.Node;
// import org.jodaengine.repository.importer.RawProcessDefintionImporter;
// import org.jodaengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
// import org.jodaengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
//

// /**
// * The Class RepositorySetup.
// */
// public final class RepositorySetup {
//
// /**
// * Hidden constructor.
// */
// private RepositorySetup() {
//
// }
//
// /** The Constant FIRST_EXAMPLE_PROCESS_ID. */
// public static final UUID FIRST_EXAMPLE_PROCESS_ID = UUID.randomUUID();
//
// /**
// * Fill repository.
// * @throws IllegalStarteventException
// */
// public static void fillRepository() throws IllegalStarteventException {
//
// RepositoryService repo = ServiceFactory.getRepositoryService();
// DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();
//
// // Deploying the process with a simple ProcessImporter
// ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(get1Plus1Process());
// deploymentBuilder.deployProcessDefinition(rawProDefImporter);
// }
//
// /**
// * Creates the process 'Plus1Process'.
// *
// * @return the process definition
// * @throws IllegalStarteventException
// */
// private static ProcessDefinition get1Plus1Process()
// throws IllegalStarteventException {
//
// String processName = "1Plus1Process";
// String processDescription = "The process stores the result of the calculation '1 + 1' .";
//
// ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
// Class<?>[] conSig = {String.class, int[].class};
// int[] ints = {1, 1};
// Object[] conArgs = {"result", ints};
// ActivityBlueprint blueprint = new ActivityBlueprintImpl(AddNumbersAndStoreActivity.class, conSig, conArgs);
// NodeParameter param = new NodeParameterImpl(blueprint,
// new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
//
// Node node1 = builder.createStartNode(param);
//
// Node node2 = builder.createNode(param);
// builder.createTransition(node1, node2).setName(processName).setDescription(processDescription);
// return builder.buildDefinition();
// }
// }
