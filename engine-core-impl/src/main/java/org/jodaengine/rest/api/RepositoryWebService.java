package org.jodaengine.rest.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.BpmnXmlImporter;
import org.jodaengine.process.definition.ProcessDefinition;


/**
 * API providing an interface for the {@link RepositoryService}. It can be used to deploy process definitions.
 */
@Path("/repository")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class RepositoryWebService {
//    TODO implements RepositoryService

    private static final String XML_START = "<?xml";
    private static final String XML_END = "</definitions>";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private DeploymentBuilder deploymentBuilder;
    private RepositoryService repositoryService;

    /**
     * Instantiates a new deployer web service. Initializes the Deployment builder.
     *
     * @param engineServices the engine services
     */
    public RepositoryWebService(JodaEngineServices engineServices) {
        
        this.repositoryService = engineServices.getRepositoryService();
        this.deploymentBuilder = this.repositoryService.getDeploymentBuilder();
    }
    
    // return status codes
    /**
     * Gets the process definitions via REST API.
     *
     * @return the process definitions
     */
    @Path("/process-definitions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProcessDefinition> getProcessDefinitions() {
        return this.repositoryService.getProcessDefinitions();
    }
    
    /**
     * Deploy a definition from an uploaded xml.
     *
     * @param file the xml representation
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Path("/process-definitions/deploy")
    @POST
    public void deployDefinitionFromXML(String file) throws IOException {
        logger.debug(file);
        int xmlStart = file.indexOf(XML_START);
        // it is the end so we need to add back the length of the found element
        int xmlEnd = file.indexOf(XML_END) + XML_END.length();
        String xmlContent = file.substring(xmlStart, xmlEnd);
        logger.debug(xmlContent);
        
        BpmnXmlImporter importer = new BpmnXmlImporter(xmlContent);
        
        // deploys the process definition
        this.deploymentBuilder.addProcessDefinition(importer.createProcessDefinition());
        this.repositoryService.deploy(this.deploymentBuilder.buildDeployment());
    }
    
    /*
    /**
     * Deploys an instance, more customizable version to come.
     *
     * @return the string
     * /
    @Path("/deploy")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String deployInstance() {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();

        int[] integers = {1, 1};
        Node node1 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node node2 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node node3 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node node4 = BpmnCustomNodeFactory.createBpmnAddNumbersAndStoreNode(builder, "result", integers);
        Node endNode = BpmnNodeFactory.createBpmnEndEventNode(builder);

        TransitionFactory.createTransitionFromTo(builder, node1, node2);
        TransitionFactory.createTransitionFromTo(builder, node2, node3);
        TransitionFactory.createTransitionFromTo(builder, node3, node4);
        TransitionFactory.createTransitionFromTo(builder, node4, endNode);

        ProcessDefinition definition = null;
        try {
            definition = builder.buildDefinition();
            deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
            return definition.getID().toString();
        } catch (IllegalStarteventException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

}
