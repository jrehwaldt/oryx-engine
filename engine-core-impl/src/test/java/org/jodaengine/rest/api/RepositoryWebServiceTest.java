package org.jodaengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.jodaengine.ServiceFactory;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.deployment.ProcessDefinitionImporter;
import org.jodaengine.deployment.importer.definition.RawProcessDefintionImporter;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.node.factory.bpmn.BpmnProcessDefinitionModifier;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionBuilderImpl;
import org.jodaengine.repository.RepositorySetup;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Tests our repository web service.
 */
public class RepositoryWebServiceTest extends AbstractJsonServerTest {

    private final static String URL = "/repository/process-definitions";

    private final static JavaType TYPE_REF = TypeFactory.collectionType(Set.class, ProcessDefinition.class);

    @Override
    protected Object getResourceSingleton() {

        return new RepositoryWebService(jodaEngineServices);
    }

    /**
     * Creates another process definition, which is empty but deployed into the repository.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    public void createAnotherProcessDefinition()
    throws IllegalStarteventException {

        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilderImpl();
        builder.setName("Empty").setDescription("Really an empty dummy process");
        BpmnProcessDefinitionModifier.decorateWithDefaultBpmnInstantiationPattern(builder);
        ProcessDefinition definition = builder.buildDefinition();

        DeploymentBuilder deploymentBuilder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
        Deployment deployment = deploymentBuilder.addProcessDefinition(definition).buildDeployment();
        
        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);
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
        Assert.assertEquals(definitions.iterator().next().getID(), RepositorySetup.getProcess1Plus1ProcessID());

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
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetMultipleProcessDefinitions()
    throws IllegalStarteventException, URISyntaxException, IOException {

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
