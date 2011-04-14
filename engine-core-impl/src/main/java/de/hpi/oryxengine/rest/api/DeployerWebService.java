package de.hpi.oryxengine.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * API servlet providing an interface for the deployer. It can be used to deploy process definitions.
 */
@Path("/deployer")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class DeployerWebService {

    private DeploymentBuilder deploymentBuilder;

    public DeployerWebService() {

        this.deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
    }

    @Path("/deploy")
    @GET
    @Produces("text/plain")
    public String deployInstance() {

        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());

        // param.setActivity(AddNumbersAndStoreActivity("result", 1, 1));
        int[] integers = { 1, 1 };
        nodeParamBuilder.setDefaultActivityBlueprintFor(AddNumbersAndStoreActivity.class)
        .addConstructorParameter(String.class, "result").addConstructorParameter(int[].class, integers);
        Node node1 = builder.createStartNode(nodeParamBuilder.finishedNodeParameter());

        // param.setActivity(new AddNumbersAndStoreActivity("result", 2, 2));
        Node node2 = builder.createNode(nodeParamBuilder.finishedNodeParameter());

        // param.setActivity(new AddNumbersAndStoreActivity("result", 3, 3));
        Node node3 = builder.createNode(nodeParamBuilder.finishedNodeParameter());

        // param.setActivity(new AddNumbersAndStoreActivity("result", 4, 4));
        Node node4 = builder.createNode(nodeParamBuilder.finishedNodeParameter());

        nodeParamBuilder = new NodeParameterBuilderImpl();
        nodeParamBuilder.setDefaultActivityBlueprintFor(EndActivity.class);
        Node endNode = builder.createNode(nodeParamBuilder.finishedNodeParameter());

        builder.createTransition(node1, node2).createTransition(node2, node3).createTransition(node3, node4)
        .createTransition(node4, endNode);

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
