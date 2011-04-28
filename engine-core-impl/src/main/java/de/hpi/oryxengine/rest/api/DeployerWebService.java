package de.hpi.oryxengine.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.BPMNActivityFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.Node;

/**
 * API servlet providing an interface for the deployer. It can be used to deploy process definitions.
 */
@Path("/deployer")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class DeployerWebService {

    private DeploymentBuilder deploymentBuilder;

    /**
     * Instantiates a new deployer web service. Initializes the Deplyoment builder.
     */
    public DeployerWebService() {

        this.deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
    }

    /**
     * Deploys an instance, more customizable version to come.
     *
     * @return the string
     */
    @Path("/deploy")
    @GET
    @Produces("text/plain")
    public String deployInstance() {

        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();

        int[] integers = {1, 1};
        Node node1 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);

        Node node2 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);

        Node node3 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);

        Node node4 = BPMNActivityFactory.createBPMNAddNumbersAndStoreNode(builder, "result", integers);

        Node endNode = BPMNActivityFactory.createBPMNEndEventNode(builder);

        BPMNActivityFactory.createTransitionFromTo(builder, node1, node2);
        BPMNActivityFactory.createTransitionFromTo(builder, node2, node3);
        BPMNActivityFactory.createTransitionFromTo(builder, node3, node4);
        BPMNActivityFactory.createTransitionFromTo(builder, node4, endNode);

        ProcessDefinition definition = null;
        try {
            definition = builder.buildDefinition();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
        }

        return definition.getID().toString();
    }

}
