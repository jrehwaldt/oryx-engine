package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.ProcessDefinitionImporter;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
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
        
        Node startNode = BPMNActivityFactory.createBPMNStartEventNode(builder);
        
        int[] integers = {1, 1 };
        Node node1 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);
        Node node2 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);
        
        BPMNActivityFactory.createTransitionFromTo(builder, startNode, node1);
        BPMNActivityFactory.createTransitionFromTo(builder, node1, node2);
        
        builder.setName(processName).setDescription(processDescription);
        
        return builder.buildDefinition();
    }
}
