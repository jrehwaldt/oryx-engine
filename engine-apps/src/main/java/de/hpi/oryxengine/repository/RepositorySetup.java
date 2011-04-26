package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.ProcessDefinitionImporter;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;

/**
 * It helps previously fill the RepositoryService.
 * 
 * ATTENTION: This class is duplicated (yes Gerardo it is) it is also present in the impl tests. 
 *              So if you notice a bug here be sure to correct it there to until we find a workaround.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    private static UUID process1Plus1ProcessUUID;

    /**
     * Gets the process1 plus1 process uuid.
     *
     * @return the process1 plus1 process uuid
     */
    public static UUID getProcess1Plus1ProcessUUID() {
    
        return process1Plus1ProcessUUID;
    }

    /**
     * Sets the process1 plus1 process uuid.
     *
     * @param process1Plus1ProcessUUID the new process1 plus1 process uuid
     */
    public static void setProcess1Plus1ProcessUUID(UUID process1Plus1ProcessUUID) {
    
        RepositorySetup.process1Plus1ProcessUUID = process1Plus1ProcessUUID;
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
        process1Plus1ProcessUUID = deploymentBuilder.deployProcessDefinition(rawProDefImporter);
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

        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
        NodeParameterBuilder nodeParameterBuilder = new NodeParameterBuilderImpl();
        int[] integers = {1, 1 };
        nodeParameterBuilder
            .setActivityBlueprintFor(AddNumbersAndStoreActivity.class)
            .addConstructorParameter(String.class, "result")
            .addConstructorParameter(int[].class, integers);
        Node node1 = builder.createStartNode(nodeParameterBuilder.buildNodeParameter());

        Node node2 = builder.createNode(nodeParameterBuilder.buildNodeParameter());
        builder.createTransition(node1, node2).setName(processName).setDescription(processDescription);
        return builder.buildDefinition();
    }
}


//package de.hpi.oryxengine.repository;
//
//import java.util.UUID;
//
//import de.hpi.oryxengine.RepositoryService;
//import de.hpi.oryxengine.ServiceFactory;
//import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
//import de.hpi.oryxengine.exception.IllegalStarteventException;
//import de.hpi.oryxengine.process.definition.NodeParameter;
//import de.hpi.oryxengine.process.definition.NodeParameterImpl;
//import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
//import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
//import de.hpi.oryxengine.process.definition.ProcessDefinition;
//import de.hpi.oryxengine.process.structure.ActivityBlueprint;
//import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
//import de.hpi.oryxengine.process.structure.Node;
//import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;
//import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
//import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;
//

///**
// * The Class RepositorySetup.
// */
//public final class RepositorySetup {
//
//    /**
//     * Hidden constructor.
//     */
//    private RepositorySetup() {
//
//    }
//
//    /** The Constant FIRST_EXAMPLE_PROCESS_ID. */
//    public static final UUID FIRST_EXAMPLE_PROCESS_ID = UUID.randomUUID();
//
//    /**
//     * Fill repository.
//     * @throws IllegalStarteventException 
//     */
//    public static void fillRepository() throws IllegalStarteventException {
//
//        RepositoryService repo = ServiceFactory.getRepositoryService();
//        DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();
//
//        // Deploying the process with a simple ProcessImporter
//        ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(get1Plus1Process());
//        deploymentBuilder.deployProcessDefinition(rawProDefImporter);
//    }
//
//    /**
//     * Creates the process 'Plus1Process'.
//     * 
//     * @return the process definition
//     * @throws IllegalStarteventException
//     */
//    private static ProcessDefinition get1Plus1Process()
//    throws IllegalStarteventException {
//
//        String processName = "1Plus1Process";
//        String processDescription = "The process stores the result of the calculation '1 + 1' .";
//
//        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
//        Class<?>[] conSig = {String.class, int[].class};
//        int[] ints = {1, 1};
//        Object[] conArgs = {"result", ints};
//        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AddNumbersAndStoreActivity.class, conSig, conArgs);
//        NodeParameter param = new NodeParameterImpl(blueprint,
//            new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
//
//        Node node1 = builder.createStartNode(param);
//
//        Node node2 = builder.createNode(param);
//        builder.createTransition(node1, node2).setName(processName).setDescription(processDescription);
//        return builder.buildDefinition();
//    }
//}
