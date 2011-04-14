package de.hpi.oryxengine.rest.serialization;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotSame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.AllocationStrategies;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.allocation.TaskImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.ResourceType;
import de.hpi.oryxengine.resource.worklist.WorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemImpl;
import de.hpi.oryxengine.rest.api.WorklistWebService;
import de.hpi.oryxengine.rest.provider.ListMessageBodyWriter;

/**
 * The Class GsonWorklistItemTest.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class GsonWorklistItemTest extends AbstractTestNGSpringContextTests {
    public static final String TMP_PATH = "./target/";
    private Dispatcher dispatcher = null;

    private AbstractResource<?> thomas = null;

    /**
     * Test worklist serialization.
     * 
     * @throws IOException test fails
     */
    @Test
    public void testWorklistSerialization()
    throws IOException {

        List<WorklistItem> items = ServiceFactory.getWorklistService().getWorklistItems(thomas);
        assertEquals(items.size(), 1, "There should be one item in the list");

        // write the list to a file
        File jsonFile = new File(TMP_PATH + "worklistThomasTest.txt");
        FileWriter writer = new FileWriter(jsonFile);

        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(items);
        writer.write(json);

        writer.close();
    }

    /**
     * Tests the serialization of the worklist by accessing the REST api.
     *
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
  public void testWebServiceSerialization() throws URISyntaxException, IOException {
      MockHttpRequest request = MockHttpRequest.get(
          "/worklist/items/" + ResourceType.PARTICIPANT + "/" + thomas.getID());
      MockHttpResponse response = new MockHttpResponse();
      
      dispatcher.invoke(request, response);
      
      File jsonFile = new File(TMP_PATH + "worklistThomasRESTCall.txt");
      FileWriter writer = new FileWriter(jsonFile);
      
      
      writer.write(response.getContentAsString());
      
      writer.close();
      
      // TODO check the output in a more sophisticated way
      assertNotSame("", response.getContentAsString(), "The response should not be empty");
  }

    /**
     * Creates a worklist item for a participant.
     */
    @BeforeClass
    public void setUpWorklistItem() {

        IdentityBuilder builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        thomas = builder.createParticipant("Thomas Strunz");

        
        Task task = new TaskImpl("Kaffee holen", "Bohnenkaffee", mock(AllocationStrategies.class), thomas);
        Token token = mock(Token.class);
        WorklistItem item = new WorklistItemImpl(task, token);
        thomas.getWorklist().addWorklistItem(item);
    }

    /**
     * Set up.
     */
    @BeforeClass
    public void setUpAndStartServer() {

        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory factory = new POJOResourceFactory(WorklistWebService.class);

        dispatcher.getRegistry().addResourceFactory(factory);
        dispatcher.getProviderFactory().addMessageBodyWriter(ListMessageBodyWriter.class);

        Service nav = (Service) ServiceFactory.getNavigatorService();
        nav.start();
    }
}
