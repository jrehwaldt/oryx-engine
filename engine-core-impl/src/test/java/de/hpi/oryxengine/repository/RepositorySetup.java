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
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    /** The Constant PROCESS_1PLUS1PROCESS_UUID. */
    // TODO @Alle Bitte schaut mal warum Checkstyle hier meckert (Fragen an Gerardo stellen)
    public static UUID process1Plus1ProcessUUID;

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
        int[] integers = { 1, 1 };
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
