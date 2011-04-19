package de.hpi.oryxengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.impl.AddNumbersAndStoreActivity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.process.definition.NodeParameterBuilder;
import de.hpi.oryxengine.process.definition.NodeParameterBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.definition.ProcessDefinitionBuilder;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.repository.DeploymentBuilder;
import de.hpi.oryxengine.repository.importer.RawProcessDefintionImporter;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * The Class NavigatorWebServiceTest.
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class NavigatorWebServiceTest extends AbstractJsonServerTest {
    
    private Navigator navigator = null;
    
    private static final int WAIT_FOR_PROCESSES_TO_FINISH = 100;
    private static final int TRIES_UNTIL_PROCESSES_FINISH = 100;
    private static final short NUMBER_OF_INSTANCES_TO_START = 2;
    
    /**
     * Set up.
     */
    @BeforeMethod
    public void setUpNavigatorService() {
        this.logger.debug("Start navigator");
        
        this.navigator = ServiceFactory.getNavigatorService();
        this.navigator.start();
    }
    
    @Override
    protected Class<?> getResource() {
        return NavigatorWebService.class;
    }
    
    /**
     * Tests the get statistic method with json deserialization.
     * 
     * @throws URISyntaxException test fails
     * @throws IOException test fails
     */
    @Test
    public void testGetStatistic()
    throws URISyntaxException, IOException {
        
        MockHttpRequest request = MockHttpRequest.get("/navigator/status/statistic");
        MockHttpResponse response = new MockHttpResponse();
        
        NavigatorStatistic stats = this.navigator.getStatistics();
        dispatcher.invoke(request, response);
        
        String json = response.getContentAsString();
        logger.debug(json);
        Assert.assertNotNull(json);
        
        NavigatorStatistic callStats = this.mapper.readValue(json, NavigatorStatistic.class);
        Assert.assertNotNull(callStats);
        
        Assert.assertEquals(callStats.getNumberOfFinishedInstances(), stats.getNumberOfFinishedInstances());
        Assert.assertEquals(callStats.getNumberOfExecutionThreads(), stats.getNumberOfExecutionThreads());
        Assert.assertEquals(callStats.getNumberOfRunningInstances(), stats.getNumberOfRunningInstances());
        Assert.assertEquals(callStats.isNavigatorIdle(), stats.isNavigatorIdle());
    }
    
    /**
     * Tests the get statistic method with json deserialization.
     * 
     * @throws URISyntaxException test fails
     * @throws IOException test fails
     */
    @Test
    public void testGetIsIdle()
    throws URISyntaxException, IOException {
        
        MockHttpRequest request = MockHttpRequest.get("/navigator/status/is-idle");
        MockHttpResponse response = new MockHttpResponse();
        
        boolean isIdle = this.navigator.isIdle();
        dispatcher.invoke(request, response);
        
        String result = response.getContentAsString();
        logger.debug(result);
        Assert.assertNotNull(result);
        
        boolean callIdIdle = Boolean.valueOf(result);
        Assert.assertEquals(isIdle, callIdIdle);
    }
    
    /**
     * Tests the staring of an process instance via our Rest-interface.
     * 
     * @throws IllegalStarteventException
     *             test fails
     * @throws URISyntaxException
     *             test fails
     * @throws InterruptedException
     *             test fails
     * @throws IOException
     *             test fails
     */
    @Test
    public void testStartInstance()
    throws IllegalStarteventException, URISyntaxException, InterruptedException, IOException {
        
        // create simple process
        ProcessDefinitionBuilder builder = new ProcessBuilderImpl();
        NodeParameterBuilder nodeParamBuilder = new NodeParameterBuilderImpl(
            new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        nodeParamBuilder.setActivityBlueprintFor(NullActivity.class);
        Node startNode = builder.createStartNode(nodeParamBuilder.buildNodeParameter());
        
        nodeParamBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        int[] ints = {1, 1};
        nodeParamBuilder
            .setActivityBlueprintFor(AddNumbersAndStoreActivity.class)
            .addConstructorParameter(String.class, "result")
            .addConstructorParameter(int[].class, ints);
        Node node1 = builder.createNode(nodeParamBuilder.buildNodeParameter());
        Node node2 = builder.createNode(nodeParamBuilder.buildNodeParameter());
        builder.createTransition(startNode, node1).createTransition(node1, node2);
        
        nodeParamBuilder = new NodeParameterBuilderImpl(new SimpleJoinBehaviour(), new TakeAllSplitBehaviour());
        nodeParamBuilder.setActivityBlueprintFor(EndActivity.class);
        Node endNode = builder.createNode(nodeParamBuilder.buildNodeParameter());
        builder.createTransition(node2, endNode);

        // deploy it
        ProcessDefinition definition = builder.buildDefinition();

        DeploymentBuilder deploymentBuilder = ServiceFactory.getRepositoryService().getDeploymentBuilder();
        UUID processId = deploymentBuilder.deployProcessDefinition(new RawProcessDefintionImporter(definition));
        
        Assert.assertTrue(this.navigator.isIdle());
        // run it via REST request
        MockHttpRequest request;
        MockHttpResponse response;
        for (int i = 0; i < NUMBER_OF_INSTANCES_TO_START; i++) {
            request = MockHttpRequest.post(String.format("/navigator/process/%s/start", processId));
            response = new MockHttpResponse();
            
            this.dispatcher.invoke(request, response);
            Assert.assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        }
        
        // wait for the service to be finished
        for (int i = 0; !this.navigator.isIdle(); i++) {
            Thread.sleep(WAIT_FOR_PROCESSES_TO_FINISH);
            
            if (i == TRIES_UNTIL_PROCESSES_FINISH) {
                this.logger.error("Process instance never finished");
                throw new IllegalStateException("Process instance never finished");
            }
        }
        
        //
        // test finished instances
        //
        request = MockHttpRequest.get("/navigator/status/finished-instances");
        response = new MockHttpResponse();
        
        this.dispatcher.invoke(request, response);
        String jsonFinished = response.getContentAsString();
        this.logger.debug(jsonFinished);
        Assert.assertNotNull(jsonFinished);
        
        JavaType typeRef = TypeFactory.collectionType(List.class, AbstractProcessInstance.class);
        List<AbstractProcessInstance> finInstances = this.mapper.readValue(jsonFinished, typeRef);
        Assert.assertNotNull(finInstances);
        
        Assert.assertEquals(finInstances.size(), NUMBER_OF_INSTANCES_TO_START);
        for (AbstractProcessInstance ins: finInstances) {
            Assert.assertEquals(ins.getDefinition().getID(), processId);
        }
        
        //
        // test running instances
        //
        request = MockHttpRequest.get("/navigator/status/running-instances");
        response = new MockHttpResponse();
        
        this.dispatcher.invoke(request, response);
        String jsonRunning = response.getContentAsString();
        this.logger.debug(jsonRunning);
        Assert.assertNotNull(jsonRunning);
        
        List<AbstractProcessInstance> runInstances = this.mapper.readValue(jsonRunning, typeRef);
        Assert.assertNotNull(runInstances);
        
        Assert.assertEquals(runInstances.size(), 0);
        
        //
        // test running instances
        //
        request = MockHttpRequest.get("/navigator/status/is-idle");
        response = new MockHttpResponse();
        
        this.dispatcher.invoke(request, response);
        String jsonIdle = response.getContentAsString();
        this.logger.debug(jsonIdle);
        Assert.assertNotNull(jsonIdle);
        
        boolean isIdle = this.mapper.readValue(jsonIdle, boolean.class);
        Assert.assertTrue(isIdle);
        
        //
        // test statistics
        //
        request = MockHttpRequest.get("/navigator/status/statistic");
        response = new MockHttpResponse();
        
        this.dispatcher.invoke(request, response);
        String jsonStats = response.getContentAsString();
        this.logger.debug(jsonStats);
        Assert.assertNotNull(jsonStats);
        NavigatorStatistic stats = this.mapper.readValue(jsonStats, NavigatorStatistic.class);
        Assert.assertNotNull(stats);
        
        Assert.assertEquals(stats.getNumberOfFinishedInstances(), NUMBER_OF_INSTANCES_TO_START);
        Assert.assertEquals(stats.getNumberOfRunningInstances(), 0);
        Assert.assertTrue(stats.isNavigatorIdle());
    }
}
