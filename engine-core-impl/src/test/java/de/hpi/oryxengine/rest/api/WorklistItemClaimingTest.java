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
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.AbstractResource;
import de.hpi.oryxengine.resource.AbstractRole;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.resource.worklist.WorklistItemState;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;

/**
 * The Class WorklistItemClaimingTest.
 */
public class WorklistItemClaimingTest extends AbstractJsonServerTest {

    private Task task;
    private AbstractRole assignedRole;
    private AbstractResource<?> participant;

    @Override
    protected Class<?> getResource() {

        return WorklistWebService.class;
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
        
        String json = "{"             
        + "\"participantId\" : \"" + participant.getID() + "\","
        + "\"action\" : \"CLAIM\","
        + "\"@classifier\" : \"de.hpi.oryxengine.rest.WorklistActionWrapper\""
        + "}";

        MockHttpResponse response = makePUTRequestWithJson("/worklist/items/" + item.getID() + "/state", json);

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK, 
            "the result should be OK, that means, the request should have suceeded.");
        
        Assert.assertEquals(item.getStatus(), WorklistItemState.ALLOCATED, "the item should be allocated now.");
        
        List<AbstractWorklistItem> roleItems = ServiceFactory.getWorklistService().getWorklistItems(assignedRole);
        Assert.assertEquals(roleItems.size(), 0, "the item should not be in the role's worklist any longer.");

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
    }

}
