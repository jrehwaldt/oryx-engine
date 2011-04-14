package de.hpi.oryxengine.repository;

import java.util.UUID;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * It helps previously fill the RepositoryService.
 */
public final class RepositorySetup {

    /**
     * Hidden constructor.
     */
    private RepositorySetup() {

    }

    /** The Constant FIRST_EXAMPLE_PROCESS_ID. */
    public static final UUID FIRST_EXAMPLE_PROCESS_ID = UUID.randomUUID();

    /**
     * Fill repository.
     * 
     * @throws IllegalStarteventException
     */
    public static void fillRepository()
    throws IllegalStarteventException {

        RepositoryService repo = ServiceFactory.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repo.getDeploymentBuilder();

        // Deploying the process with a simple ProcessImporter
        ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(get1Plus1Process());
        deploymentBuilder.deployProcessDefinition(rawProDefImporter);
    }

    /**
     * Creates the process 'Plus1Process'.
     * 
     * @return the process definition
     * @throws IllegalStarteventException
     */
    private static ProcessDefinition get1Plus1Process()
    throws IllegalStarteventException {

        String processName = "1Plus1Process";
        String processDescription = "The process stores the result of the calculation '1 + 1' .";

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl(new AddNumbersAndStoreActivity("result", 1, 1),
            new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        param.makeStartNode(true);

        Node node1 = builder.createNode(param);

        param.makeStartNode(false);
        Node node2 = builder.createNode(param);
        builder.createTransition(node1, node2).setName(processName).setDescription(processDescription);
        return builder.buildDefinition();
    }
}
