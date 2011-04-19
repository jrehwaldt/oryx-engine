package de.hpi.oryxengine.rest.api;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorStatistic;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * The Class NavigatorWebServiceTest.
 */
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class NavigatorWebServiceTest extends AbstractJsonServerTest {
    
    private Navigator navigator = null;
    
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
        
        MockHttpRequest request = MockHttpRequest.get("/navigator/statistic");
        MockHttpResponse response = new MockHttpResponse();
        
        NavigatorStatistic stats;
        synchronized (this.navigator) {
            stats = this.navigator.getStatistics();
            dispatcher.invoke(request, response);
        }
        
        String json = response.getContentAsString();
        logger.debug(json);
        
        NavigatorStatistic callStats = this.mapper.readValue(json, NavigatorStatistic.class);
        
        Assert.assertNotNull(callStats);
        Assert.assertEquals(
            callStats.getNumberOfFinishedInstances(), stats.getNumberOfFinishedInstances());
        Assert.assertEquals(
            callStats.getNumberOfExecutionThreads(), stats.getNumberOfExecutionThreads());
        Assert.assertEquals(
            callStats.getNumberOfRunningInstances(), stats.getNumberOfRunningInstances());
        Assert.assertEquals(callStats.isNavigatorIdle(), stats.isNavigatorIdle());
    }
    
    /**
     * Tests the serialization of our navigation statistics. This is necessary because occasionally
     * serializing of "boolean x = true" was not correctly deserialized.
     * 
     * @throws IOException test fails
     * @throws JAXBException test fails
     */
    @Test
    public void testSerializationAndDesirializationOfNavigationStatistics() throws JAXBException, IOException {
        File xml = new File(TMP_PATH + "NavigatorStatistics.js");
        if (xml.exists()) {
            Assert.assertTrue(xml.delete());
        }
        
        NavigatorStatistic stats = new NavigatorStatistic(1, 1, 1, true);
        this.mapper.writeValue(xml, stats);
        
        Assert.assertTrue(xml.exists());
        Assert.assertTrue(xml.length() > 0);
        
        NavigatorStatistic desStats = this.mapper.readValue(xml, NavigatorStatistic.class);
        Assert.assertNotNull(desStats);

        Assert.assertEquals(
            desStats.getNumberOfFinishedInstances(), stats.getNumberOfFinishedInstances());
        Assert.assertEquals(
            desStats.getNumberOfExecutionThreads(), stats.getNumberOfExecutionThreads());
        Assert.assertEquals(
            desStats.getNumberOfRunningInstances(), stats.getNumberOfRunningInstances());
        Assert.assertEquals(desStats.isNavigatorIdle(), stats.isNavigatorIdle());
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
     */
    @Test
    public void testStartInstance()
    throws IllegalStarteventException, URISyntaxException, InterruptedException {

    // TODO: [@Gerardo:] mal wieder auskommentieren
// create simple process
//        ProcessBuilder builder = new ProcessBuilderImpl();
//        NodeParameter param = new NodeParameterImpl();
//        
//        Class<?>[] constructorSig = {String.class, int[].class};
//        int[] ints = {1, 1};
//        Object[] params = {"result", ints};
//        ActivityBlueprint blueprint = new ActivityBlueprintImpl(AddNumbersAndStoreActivity.class, constructorSig,
//            params);
//        param.setActivityBlueprint(blueprint);
//        param.setOutgoingBehaviour(new TakeAllSplitBehaviour());
//        param.setIncomingBehaviour(new SimpleJoinBehaviour());
//        Node startNode = builder.createStartNode(param);
//
//        param.setActivityClassOnly(EndActivity.class);
//        Node endNode = builder.createNode(param);
//
//        builder.createTransition(startNode, endNode);
//        ProcessDefinition definition = builder.buildDefinition();
//
//        // deploy it
//
//        ServiceFactory.getDeplyomentService().deploy(definition);
//        String id = definition.getID().toString();
//
//        // run it via REST request
//        MockHttpRequest request = MockHttpRequest.get(String.format("/navigator/instance/%s/start", id));
//        MockHttpResponse response = new MockHttpResponse();
//
//        dispatcher.invoke(request, response);

        // TODO @Jan: fix, if statistic works with json
        // // check, if it has finished after two seconds.
        // Thread.sleep(1500);
        //
        // request = MockHttpRequest.get("/navigator/endedinstances");
        // response = new MockHttpResponse();
        //
        // dispatcher.invoke(request, response);
        //
        // assertEquals(Integer.valueOf(response.getContentAsString()).intValue(), 1);
    }
}
