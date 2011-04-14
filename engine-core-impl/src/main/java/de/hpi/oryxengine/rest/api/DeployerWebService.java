package de.hpi.oryxengine.rest.api;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.deploy.Deployer;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.ActivityBlueprint;
import de.hpi.oryxengine.process.structure.ActivityBlueprintImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * API servlet providing an interface for the deployer. It can be used to deploy process definitions.
 */
@Path("/deployer")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class DeployerWebService {

    private Deployer deployer;

    public DeployerWebService() {

        deployer = ServiceFactory.getDeplyomentService();
    }

    @Path("/deploy")
    @GET
    @Produces("text/plain")
    public String deployInstance() {

        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl();
        
//        param.setActivity(AddNumbersAndStoreActivity("result", 1, 1));
        Class<?>[] conSig = {String.class, int[].class};
        int[] integers = {1, 1};
        Object[] conArgs = {"result", integers};
        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AddNumbersAndStoreActivity.class, conSig, conArgs);
        param.setActivityBlueprint(blueprint);
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        Node node1 = builder.createStartNode(param);

//        param.setActivity(new AddNumbersAndStoreActivity("result", 2, 2));
        Node node2 = builder.createNode(param);

//        param.setActivity(new AddNumbersAndStoreActivity("result", 3, 3));
        Node node3 = builder.createNode(param);

//        param.setActivity(new AddNumbersAndStoreActivity("result", 4, 4));
        Node node4 = builder.createNode(param);

        
        param.setActivityClassOnly(EndActivity.class);
        Node endNode = builder.createNode(param);

        builder.createTransition(node1, node2).createTransition(node2, node3).createTransition(node3, node4)
        .createTransition(node4, endNode).setID(UUID.randomUUID());

        ProcessDefinition definition = null;
        try {
            definition = builder.buildDefinition();
            deployer.deploy(definition);
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
        }

        return definition.getID().toString();
    }

}
