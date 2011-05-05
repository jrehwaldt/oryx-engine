package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.deployment.DeploymentBuilder;
import de.hpi.oryxengine.deployment.importer.ProcessDefinitionImporter;
import de.hpi.oryxengine.deployment.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilderImpl;
import de.hpi.oryxengine.repository.RepositorySetup;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests our repository web service.
 */
public class RepositoryWebServiceTest extends AbstractJsonServerTest {

    private final static String URL = "/repository/process-definitions";
    
    private final static JavaType TYPE_REF = TypeFactory.collectionType(Set.class, ProcessDefinition.class);
    
    @Override
    protected Class<?> getResource() {

        return RepositoryWebService.class;
    }
    
    /**
     * Creates another process definition, which is empty but deployed into the repository.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    public void createAnotherProcessDefinition() throws IllegalStarteventException {
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        builder.setName("Empty").setDescription("Really an empty dummy process");
        ProcessDefinition definition = builder.buildDefinition();
        ProcessDefinitionImporter rawProDefImporter = new RawProcessDefintionImporter(definition);
        
        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        deploymentBuilder.deployProcessDefinition(rawProDefImporter);
    }

    /**
     * Test get process definitions with 1 definition deployed.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws DefinitionNotFoundException
     *             the definition not found exception
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @Test
    public void testGetProcessDefinitions()
    throws URISyntaxException, IOException, DefinitionNotFoundException, IllegalStarteventException {
        // fills repository with one processdefinition
        RepositorySetup.fillRepository();
        
        String json = makeGETRequestReturningJson(URL);
        Assert.assertNotNull(json);
        // assert we don't get back an empty JSON set.
        Assert.assertFalse("[]".equals(json));
        
        Set<ProcessDefinition> definitions = this.mapper.readValue(json, TYPE_REF);
        
        Assert.assertEquals(definitions.size(), 1);
        // it is really our Element
        Assert.assertEquals(definitions.iterator().next().getID(), RepositorySetup.getProcess1Plus1ProcessUUID());

    }

    /**
     * Test get process definitions when the repository is empty.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetProcessDefinitionsWhenEmpty()
    throws IllegalStarteventException, URISyntaxException, IOException {
        String json = makeGETRequestReturningJson(URL);
        // nothing therer
        Assert.assertEquals(json, "[]");
        
        Set<ProcessDefinition> definitions = this.mapper.readValue(json, TYPE_REF);
        Assert.assertEquals(definitions.size(), 0);
    }
    
    /**
     * Test get multiple (2) process definitions.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetMultipleProcessDefinitions() throws IllegalStarteventException, URISyntaxException, IOException {
        RepositorySetup.fillRepository();
        createAnotherProcessDefinition(); 
        String json = makeGETRequestReturningJson(URL);
        Assert.assertNotNull(json);
        // assert we don't get back an empty JSON set.
        Assert.assertFalse("[]".equals(json));
        
        Set<ProcessDefinition> definitions = this.mapper.readValue(json, TYPE_REF);
        Assert.assertEquals(definitions.size(), 2);
    }

}
