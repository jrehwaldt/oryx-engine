package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.RepositoryService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionImpl;
import de.hpi.oryxengine.repository.RepositorySetup;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests our repository web service.
 */
public class RepositoryWebServiceTest extends AbstractJsonServerTest {

    private RepositoryService repositoryService;
    
    @Override
    protected Class<?> getResource() {

        return RepositoryWebService.class;
    }
    
    /**
     * Sets the up.
     *
     * @throws IllegalStarteventException the illegal startevent exception
     */
    @BeforeClass
    public void setUp() throws IllegalStarteventException {
        RepositorySetup.fillRepository();
        repositoryService = ServiceFactory.getRepositoryService();
        
    }
    
    /**
     * Test get process definitions.
     * @throws URISyntaxException 
     * @throws IOException 
     * @throws DefinitionNotFoundException 
     */
    @Test
    public void testGetProcessDefinitions() throws URISyntaxException, IOException, DefinitionNotFoundException {
        
     // set up our request
        MockHttpRequest request = MockHttpRequest.get("/repository/processdefinitions");
        MockHttpResponse response = new MockHttpResponse();
        // invoke the request
        dispatcher.invoke(request, response);
        
        String json = response.getContentAsString();
        logger.debug(json);
        Assert.assertNotSame(json, "[]");
        
        // TODO again this pesky damn hack to deserialize Lists/Sets
        ProcessDefinition[] definitions = this.mapper.readValue(json, ProcessDefinitionImpl[].class);
        Set<ProcessDefinition> set = new HashSet<ProcessDefinition>(Arrays.asList(definitions));
                
        Assert.assertEquals(set.size(), 1);
        Assert.assertEquals(definitions[0].getID(), RepositorySetup.getProcess1Plus1ProcessUUID());
        
    }

}
