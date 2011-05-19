package org.jodaengine.rest.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.navigator.NavigatorStatistic;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.rest.TestUtils;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The Class NavigatorWebServiceTest.
 */
public class NavigatorWebServiceTest extends AbstractJsonServerTest {

    private Navigator navigator = null;
    private final static String STATISTIC_URL = "/navigator/status/statistic";  
    private final static String STATUS_IDLE_URL = "/navigator/status/is-idle";
    
    protected static final int WAIT_FOR_PROCESSES_TO_FINISH = 100;
    protected static final int TRIES_UNTIL_PROCESSES_FINISH = 100;
    protected static final short NUMBER_OF_INSTANCES_TO_START = 2;
    
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
    protected Object getResourceSingleton() {
        return new NavigatorWebService(jodaEngineServices);
    }

    /**
     * Tests the get statistic method with json deserialization.
     * 
     * @throws URISyntaxException
     *             test fails
     * @throws IOException
     *             test fails
     */
    @Test
    public void testGetStatistic()
    throws URISyntaxException, IOException {
        String json = makeGETRequestReturningJson(STATISTIC_URL);
        
        NavigatorStatistic stats = this.navigator.getStatistics();
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
     * @throws URISyntaxException
     *             test fails
     * @throws IOException
     *             test fails
     */
    @Test
    public void testGetIsIdle()
    throws URISyntaxException, IOException {
        boolean isIdle = this.navigator.isIdle();
        String result = makeGETRequestReturningJson(STATUS_IDLE_URL);
        
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

        ProcessDefinition definition = TestUtils.deploySimpleProcess();

        Assert.assertTrue(this.navigator.isIdle());
        // run it via REST request
        MockHttpRequest request;
        MockHttpResponse response;
        for (int i = 0; i < NUMBER_OF_INSTANCES_TO_START; i++) {
            request = MockHttpRequest.post(String.format("/navigator/process-definitions/%s/start",
                definition.getID()));
            response = new MockHttpResponse();

            this.dispatcher.invoke(request, response);
            // FIXME @Jan&Gerardo
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

        String jsonFinished = makeGETRequestReturningJson("/navigator/status/finished-instances");
        Assert.assertNotNull(jsonFinished);

        JavaType typeRef = TypeFactory.collectionType(List.class, AbstractProcessInstance.class);
        List<AbstractProcessInstance> finInstances = this.mapper.readValue(jsonFinished, typeRef);
        Assert.assertNotNull(finInstances);

        Assert.assertEquals(finInstances.size(), NUMBER_OF_INSTANCES_TO_START);
        for (AbstractProcessInstance ins : finInstances) {
            Assert.assertEquals(ins.getDefinition().getID(), definition.getID());
        }

        String jsonRunning = makeGETRequestReturningJson("/navigator/status/running-instances");
        Assert.assertNotNull(jsonRunning);

        List<AbstractProcessInstance> runInstances = this.mapper.readValue(jsonRunning, typeRef);
        Assert.assertNotNull(runInstances);

        Assert.assertEquals(runInstances.size(), 0);

        String jsonIdle = makeGETRequestReturningJson(STATUS_IDLE_URL);
        this.logger.debug(jsonIdle);
        Assert.assertNotNull(jsonIdle);

        boolean isIdle = this.mapper.readValue(jsonIdle, boolean.class);
        Assert.assertTrue(isIdle);

        //
        // test statistics
        //
        String jsonStats = makeGETRequestReturningJson(STATISTIC_URL);
        this.logger.debug(jsonStats);
        Assert.assertNotNull(jsonStats);
        NavigatorStatistic stats = this.mapper.readValue(jsonStats, NavigatorStatistic.class);
        Assert.assertNotNull(stats);

        Assert.assertEquals(stats.getNumberOfFinishedInstances(), NUMBER_OF_INSTANCES_TO_START);
        Assert.assertEquals(stats.getNumberOfRunningInstances(), 0);
        Assert.assertTrue(stats.isNavigatorIdle());
    }

    /**
     * Tests the serialization of our navigation statistics. This is necessary because occasionally serializing of
     * "boolean x = true" was not correctly deserialized.
     * 
     * @throws IOException
     *             test fails
     * @throws JAXBException
     *             test fails
     * @throws IllegalStarteventException
     *             test fails (someone killed the process definition)
     * @throws InterruptedException
     *             test fails
     * @throws DefinitionNotFoundException
     *             test fails
     */
//    @Test
//    public void testSerializationAndDesirializationOfRealWorldProcessInstance()
//    throws JAXBException, IOException, IllegalStarteventException, InterruptedException, DefinitionNotFoundException {
//
//        File xml = new File(TMP_PATH + "RealWorldProcessInstance.js");
//        if (xml.exists()) {
//            Assert.assertTrue(xml.delete());
//        }
//
//        ProcessDefinition definition = TestUtils.deploySimpleProcess();
//
//        this.navigator = ServiceFactory.getNavigatorService();
//        this.navigator.start();
//
//        this.navigator.startProcessInstance(definition.getID());
//
//        // wait for the service to be finished
//        // TODO needs to be viewed again, got a strange bug there:
//        // assert on ended instances fails on my machine but only my mine as it seems (Tobi M)
//        for (int i = 0; (!this.navigator.isIdle()) && (this.navigator.getEndedInstances().size() == 0); i++) {
//            Thread.sleep(WAIT_FOR_PROCESSES_TO_FINISH);
//
//            if (i == TRIES_UNTIL_PROCESSES_FINISH) {
//                this.logger.error("Process instance never finished");
//                throw new IllegalStateException("Process instance never finished");
//            }
//        }
//
//        Assert.assertTrue(this.navigator.getEndedInstances().size() > 0);
//        AbstractProcessInstance instance = this.navigator.getEndedInstances().get(0);
//
//        this.mapper.writeValue(xml, instance);
//
//        Assert.assertTrue(xml.exists());
//        Assert.assertTrue(xml.length() > 0);
//
//        AbstractProcessInstance desInstance = this.mapper.readValue(xml, AbstractProcessInstance.class);
//        Assert.assertNotNull(desInstance);
//
//        Assert.assertEquals(instance.getDefinition().getID(), definition.getID());
//    }
}
