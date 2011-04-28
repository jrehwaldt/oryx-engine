package de.hpi.oryxengine.rest.api;

import static org.mockito.Mockito.mock;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;


import org.jboss.resteasy.mock.MockHttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.factory.resource.ParticipantFactory;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;
import de.hpi.oryxengine.util.webservice.JSONRequestBuilder;

/**
 * The Class WorklistItemClaimingTest.
 */
public class WorklistItemStatusTest extends AbstractJsonServerTest {

    private Task task;
    private AbstractRole assignedRole;
    private AbstractResource<?> participant;
    private AbstractResource<?> badParticipant;
    private JSONRequestBuilder builder;

    @Override
    protected Class<?> getResource() {

        return WorklistWebService.class;
    }
    
    /**
     * Make a REST call to complete a worklist item.
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testItemCompleting()
    throws URISyntaxException {
        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);
        
        //Begin the item
        ServiceFactory.getWorklistService().beginWorklistItemBy(item, participant);
        
        String json = builder.setAction("\"action\" : \"END\",").build();
        System.out.println(json);

        MockHttpResponse response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK, 
            "the result should be OK, that means, the request should have suceeded.");
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.COMPLETED, "the item should be completed now.");
        
        //refresh the item list
        participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        Assert.assertEquals(
            participantItems.size(),
            0,
            "the item should not be in the participant's worklist any longer.");

    }
    
    /**
     * Make a REST call to begin a worklist item.
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testItemBeginning()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);
        
        //Claim the item
        ServiceFactory.getWorklistService().claimWorklistItemBy(item, participant);
        
        String json = builder.setAction("\"action\" : \"BEGIN\",").build();

        MockHttpResponse response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK, 
            "the result should be OK, that means, the request should have suceeded.");
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.EXECUTING, "the item should be completed now.");
        
        List<AbstractWorklistItem> roleItems = ServiceFactory.getWorklistService().getWorklistItems(assignedRole);
        Assert.assertEquals(roleItems.size(), 0, "the item should not be in the role's worklist any longer.");

    }

    /**
     * Make a REST call to claim a worklist item.
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testItemClaiming()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);
        
        String json = builder.build();

        MockHttpResponse response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK, 
            "the result should be OK, that means, the request should have suceeded.");
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.ALLOCATED, "the item should be allocated now.");
        
        List<AbstractWorklistItem> roleItems = ServiceFactory.getWorklistService().getWorklistItems(assignedRole);
        Assert.assertEquals(roleItems.size(), 0, "the item should not be in the role's worklist any longer.");

    }
    
    /**
     * Make a REST concurrent call to claim a worklist item which was already claimed.
     * The bad participant should get a 404 http response, because the item is not longer 
     *
     * @throws URISyntaxException the uRI syntax exception
     */
    @Test
    public void testItemConcurrentClaiming()
    throws URISyntaxException {

        List<AbstractWorklistItem> participantItems = ServiceFactory.getWorklistService().getWorklistItems(participant);
        AbstractWorklistItem item = participantItems.get(0);
        
        String json = builder.build();

        MockHttpResponse response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK, 
            "the result should be OK, that means, the request should have suceeded.");
        
        
        json = builder.setParticipant("\"participantId\" : \"" + badParticipant.getID() + "\",").build();
        
        response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);
        
        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL, 
        "the result should be a status code 404, that means, the request has failed.");

    }

    /**
     * Sets the up task and gets the assigned role and one of the participants that belongs to this role.
     */
    @BeforeMethod
    public void setUpTask() {

        task = TaskFactory.createRoleTask();
        TokenImpl token = mock(TokenImpl.class);
        ServiceFactory.getTaskDistribution().distribute(task, token);

        // get the participants that are assigned to the role that this task was assigned to.
        Iterator<AbstractResource<?>> taskIt = task.getAssignedResources().iterator();
        assignedRole = (AbstractRole) taskIt.next();
        Iterator<AbstractParticipant> participantsIt = assignedRole.getParticipantsImmutable().iterator();
        participant = participantsIt.next();
        
        //For concurrent testing purposes, build another evil participant. He will try to sabotage the claiming process.
        badParticipant = ParticipantFactory.createJannik();
        
        //Initialize JSON Builder
        builder = new JSONRequestBuilder(
            "\"participantId\" : \"" + participant.getID() + "\",",
            "\"action\" : \"CLAIM\",",
            "\"@classifier\" : \"de.hpi.oryxengine.rest.WorklistActionWrapper\"");
    }

}
