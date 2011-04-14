package de.hpi.oryxengine.rest.api;

import java.net.URISyntaxException;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.hpi.oryxengine.Service;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilder;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class NavigatorWebServiceTest.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class NavigatorWebServiceTest extends AbstractTestNGSpringContextTests {

    // TODO @Jan extend these tests as soon as it is possible to deploy a real process. We need some more integration tests.
    private Dispatcher dispatcher;

    /**
     * Tests the get statistic method.
     * 
     * @throws URISyntaxException test fails
     */
    @Test
    public void testGetStatistic()
    throws URISyntaxException {

        MockHttpRequest request = MockHttpRequest.get("/navigator/statistic");
        MockHttpResponse response = new MockHttpResponse();
        
        dispatcher.invoke(request, response);
        
        // TODO @Jan: we need json first, then we can deserialize the output, because it is no longer just a simple str
//        assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 0);
    }
    
    /**
     * Tests the get statistic method.
     * 
     * @throws URISyntaxException test fails
     */
    @Test
    public void testFinishedInstances()
    throws URISyntaxException {

        MockHttpRequest request = MockHttpRequest.get("/navigator/endedinstances");
        MockHttpResponse response = new MockHttpResponse();

        dispatcher.invoke(request, response);

        Assert.assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 0);
    }

    /**
     * Tests the staring of an process instance via our Rest-interface.
     * @throws IllegalStarteventException test fails
     * @throws URISyntaxException test fails
     * @throws InterruptedException test fails
     */
    @Test
    public void testStartInstance() throws IllegalStarteventException, URISyntaxException, InterruptedException {
     // TODO: [@Gerardo:] mal wieder auskommentieren
        // create simple process
        ProcessBuilder builder = new ProcessBuilderImpl();
        NodeParameter param = new NodeParameterImpl();
        param.setActivity(new AddNumbersAndStoreActivity("result", 1, 1));
        param.makeStartNode(true);
        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
        param.setIncomingBehaviour(new SimpleJoinBehaviour());
        Node startNode = builder.createNode(param);

        param.setActivity(new EndActivity());
        param.makeStartNode(false);
        Node endNode = builder.createNode(param);

        builder.createTransition(startNode, endNode);
        ProcessDefinition definition = builder.buildDefinition();

        // deploy it

        DeploymentBuilder deplomentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        deplomentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
        String id = definition.getID().toString();

        // run it via REST request
        MockHttpRequest request = MockHttpRequest.get(String.format("/navigator/instance/%s/start", id));
        MockHttpResponse response = new MockHttpResponse();
        
        dispatcher.invoke(request, response);
        
        // TODO @Jan: fix, if statistic works with json
        // check, if it has finished after two seconds.
        Thread.sleep(1500);
        
        request = MockHttpRequest.get("/navigator/endedinstances");
        response = new MockHttpResponse();
        
        dispatcher.invoke(request, response);
        
        Assert.assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 1);
    }

    /**
     * Set up.
     */
    @BeforeClass
    public void setUpAndStartServer() {

        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory factory = new POJOResourceFactory(NavigatorWebService.class);

        dispatcher.getRegistry().addResourceFactory(factory);
        
        Service nav = (Service) ServiceFactory.getNavigatorService();
        nav.start();
    }
}
