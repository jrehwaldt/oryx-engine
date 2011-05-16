package org.jodaengine.rest.api;

import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.mock.MockHttpResponse;

import org.jodaengine.ServiceFactory;
import org.jodaengine.rest.demo.DemoDataForWebservice;
import org.jodaengine.util.testing.AbstractJsonServerTest;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Tests our Demo WebService. It shall not generate data twice and it shall return a sucessful response.
 */
public class DemoWebServiceTest extends AbstractJsonServerTest {

    private static final String DEMO_URL = "/demo/generate";
    private static final String BENCHMARK_URL = "/demo/reference-without-participants";

    @Override
    protected Object getResourceSingleton() {

        return new DemoWebService(jodaEngineServices);
    }

    /**
     * Reset invoked after each method, since this context doesn't get "dirtied".
     */
    @AfterMethod
    public void resetInvoked() {

        DemoDataForWebservice.resetInvoked();
    }

    /**
     * Invoked one time the response should be ok and appropriate data shall be created.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testNormalInvocation()
    throws URISyntaxException {

        MockHttpResponse response = makePOSTFormRequest(DEMO_URL, null);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode());
        // one process should be defined
        Assert.assertEquals(ServiceFactory.getRepositoryService().getProcessDefinitions().size(), 1);
        // We shall start NUMBER_OF_PROCESSINSTANCES instances
        Assert.assertEquals(ServiceFactory.getNavigatorService().getRunningInstances().size(),
            DemoDataForWebservice.NUMBER_OF_PROCESSINSTANCES);

        // There should be at least some participants
        Assert.assertFalse(ServiceFactory.getIdentityService().getParticipants().isEmpty());
    }

    /**
     * Tests the behaviour when it gets invoked twice.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testInvokedTwice()
    throws URISyntaxException {

        makePOSTRequest(DEMO_URL, "", MediaType.TEXT_PLAIN);
        makePOSTRequest(DEMO_URL, "", MediaType.TEXT_PLAIN);

        // one process should be defined
        Assert.assertEquals(ServiceFactory.getRepositoryService().getProcessDefinitions().size(), 1);
        // We shall start NUMBER_OF_PROCESSINSTANCES instances
        Assert.assertEquals(ServiceFactory.getNavigatorService().getRunningInstances().size(),
            DemoDataForWebservice.NUMBER_OF_PROCESSINSTANCES);

    }

    /**
     * Regression test just invoking this rest service one time to check that the everything works (form file was
     * missing at one point).
     * 
     * @throws URISyntaxException
     */
    @Test
    public void testReferenceWithoutParticipant()
    throws URISyntaxException {

        makePOSTRequest(BENCHMARK_URL, "", MediaType.TEXT_PLAIN);

        // one process should be defined
        Assert.assertEquals(ServiceFactory.getRepositoryService().getProcessDefinitions().size(), 1);
    }

}
