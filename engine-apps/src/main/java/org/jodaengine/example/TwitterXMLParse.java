package org.jodaengine.example;

import java.io.InputStream;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.BpmnXmlImporter;
import org.jodaengine.exception.DefinitionNotActivatedException;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.util.ReflectionUtil;

/**
 * An example showcasing that a process with a TweetActivity gets deployed from an XML.
 */
public final class TwitterXMLParse {

    /**
     * Hidden.
     */
    private TwitterXMLParse() { };
    
    private final static String PATH_TO_TWEET_XML = 
        "twitter/SimpleIntermediateTwitterOutgoing.bpmn.xml";
    
    /**
     * Try to showcase that this reall works.
     *
     * @param args the arguments
     * @throws DefinitionNotFoundException the definition not found exception
     * @throws DefinitionNotActivatedException the definition not activated exception
     */
    public static void main(String[] args) throws DefinitionNotFoundException, DefinitionNotActivatedException {
        // Start all our brilliant services.
        JodaEngineServices jodaEngineServices = JodaEngine.start();
        
        DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();

        InputStream bpmnXmlInputStream = ReflectionUtil.getResourceAsStream(PATH_TO_TWEET_XML);
        ProcessDefinitionImporter processDefinitionImporter = new BpmnXmlImporter(bpmnXmlInputStream);
        ProcessDefinition definition = processDefinitionImporter.createProcessDefinition();
        
        deploymentBuilder.addProcessDefinition(definition);
        jodaEngineServices.getRepositoryService().deployInNewScope(deploymentBuilder.buildDeployment());
        jodaEngineServices.getRepositoryService().activateProcessDefinition(definition.getID());

        // START!
        jodaEngineServices.getNavigatorService().startProcessInstance(definition.getID());


    }
}
