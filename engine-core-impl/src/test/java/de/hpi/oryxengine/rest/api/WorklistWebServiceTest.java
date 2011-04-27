package de.hpi.oryxengine.rest.api;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.URISyntaxException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * Tests the interaction with our WorklistWebService.
 */
public class WorklistWebServiceTest extends AbstractJsonServerTest {

    private Task task;
    private AbstractParticipant jannik;

    @Override
    protected Class<?> getResource() {

        return WorklistWebService.class;
    }

    /**
     * Sets the users and worklists up.
     */
    @BeforeMethod
    public void setUpUsersAndWorklists() {

        // We need to start the engine in order to start the WorklistManager who then gets the identityService
        OryxEngine.start();
        task = TaskFactory.createJannikServesGerardoTask();
        TokenImpl token = mock(TokenImpl.class);
        ServiceFactory.getTaskDistribution().distribute(task, token);
        // System.out.println(ServiceFactory.getIdentityService().getParticipants());
        jannik = (AbstractParticipant) task.getAssignedResources().toArray()[0];
    }

    /**
     * Test get the worklist with one item in the worklist.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetWorklist()
    throws URISyntaxException, IOException {

        String json = makeGETRequest("/worklist/items?id=" + jannik.getID());
        Assert.assertNotSame(json, "[]");

        AbstractWorklistItem[] items = this.mapper.readValue(json, AbstractWorklistItem[].class);

        Assert.assertNotNull(items);
        Assert.assertEquals(items.length, 1);
        // worklistitem is a task, tasks don't have an ID so we deceide to test it that way
        // compare against the values from the Factory
        Assert.assertEquals(items[0].getSubject(), TaskFactory.SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(items[0].getDescription(), TaskFactory.SIMPLE_TASK_DESCRIPTION);
    }

}
