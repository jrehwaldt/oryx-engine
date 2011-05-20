package org.jodaengine.rest.api;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.RepositoryService;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.deployment.importer.definition.bpmn.BpmnXmlParseListener;
import org.jodaengine.ext.service.ExtensionService;
import org.jodaengine.process.definition.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private ExtensionService extensionService;

    /**
     * Instantiates a new deployer web service. Initializes the Deployment builder.
     *
     * @param engineServices the engine services
     */
    public RepositoryWebService(JodaEngineServices engineServices) {
        
        this.repositoryService = engineServices.getRepositoryService();
        this.deploymentBuilder = this.repositoryService.getDeploymentBuilder();
        this.extensionService = engineServices.getExtensionService();
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
        
        //
        // get the xml process definition
        //
        int xmlStart = file.indexOf(XML_START);
        // it is the end so we need to add back the length of the found element
        int xmlEnd = file.indexOf(XML_END) + XML_END.length();
        String xmlContent = file.substring(xmlStart, xmlEnd);
        logger.debug("Parsing process definition: {}", xmlContent);
        
        //
        // get parse listener
        //
        List<BpmnXmlParseListener> listeners = this.extensionService.getExtensions(BpmnXmlParseListener.class);
        BpmnXmlParseListener[] listenersArray = listeners.toArray(new BpmnXmlParseListener[listeners.size()]);
        
        //
        // construct bpmn xml importer with all provided listeners
        //
        BpmnXmlImporter importer = new BpmnXmlImporter(xmlContent, listenersArray);
        
        //
        // deploys the process definition
        //
        ProcessDefinition definition = importer.createProcessDefinition();
        
        this.deploymentBuilder.addProcessDefinition(definition);
        this.repositoryService.deployInNewScope(this.deploymentBuilder.buildDeployment());
    }
}
