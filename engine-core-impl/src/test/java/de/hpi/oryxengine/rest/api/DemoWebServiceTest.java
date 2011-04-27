package de.hpi.oryxengine.rest.api;

import java.net.URISyntaxException;

import org.jboss.resteasy.mock.MockHttpResponse;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests our Demo WebService. It shall not generate data twice and it shall return a sucessful response.
 */
public class DemoWebServiceTest extends AbstractJsonServerTest {

    private static final String DEMO_URL = "/demo/generate";

    @Override
    protected Class<?> getResource() {

        return DemoWebService.class;
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

        MockHttpResponse response = makePOSTRequest(DEMO_URL);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK);
        // one process should be defined
        Assert.assertEquals(ServiceFactory.getRepositoryService().getProcessDefinitions().size(),
            1);
        // We shall start NUMBER_OF_PROCESSINSTANCES instances
        Assert.assertEquals(ServiceFactory.getNavigatorService().getRunningInstances().size(), 
            DemoDataForWebservice.NUMBER_OF_PROCESSINSTANCES);
        
        // There should be at least some participants
        Assert.assertFalse(ServiceFactory.getIdentityService().getParticipants().isEmpty());
    }

    /**
     * Tests the behaviour when it gets invoked twice.
     * @throws URISyntaxException 
     */
    @Test
    public void testInvokedTwice() throws URISyntaxException {
        makePOSTRequest(DEMO_URL);
        makePOSTRequest(DEMO_URL);
        
     // one process should be defined
        Assert.assertEquals(ServiceFactory.getRepositoryService().getProcessDefinitions().size(),
            1);
        // We shall start NUMBER_OF_PROCESSINSTANCES instances
        Assert.assertEquals(ServiceFactory.getNavigatorService().getRunningInstances().size(), 
            DemoDataForWebservice.NUMBER_OF_PROCESSINSTANCES);
        
    } 
}
