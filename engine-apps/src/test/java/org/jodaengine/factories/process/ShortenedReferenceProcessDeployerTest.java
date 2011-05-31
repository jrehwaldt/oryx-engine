package org.jodaengine.factories.process;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.jodaengine.NavigatorImplMock;
import org.jodaengine.ServiceFactory;
import org.jodaengine.WorklistService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.bpmn.BpmnHumanTaskActivity;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.jodaengine.resource.AbstractResource;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.Role;
import org.jodaengine.resource.allocation.pattern.creation.AbstractCreationPattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class ShortenedReferenceProcessDeployerTest. This is the test class for {@link ShortenedReferenceProcessDeployer}
 * .
 */
public class ShortenedReferenceProcessDeployerTest extends AbstractProcessDeployerTest {

    private Token token = null;
    private AbstractProcessInstance processInstance = null;
    private ShortenedReferenceProcessDeployer instanceDefinition = null;
    private Participant tobi = null;
    private WorklistService worklistManager = null;
    private Participant jannik = null;
    private Participant jan = null;

    @BeforeMethod
    @Override
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException, InstantiationException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {

        instanceDefinition = new ShortenedReferenceProcessDeployer();
        this.id = instanceDefinition.deploy(engineServices);
        try {
            // The builder is not used in this tested, therefore it can be null
            TokenBuilder builder = new BpmnTokenBuilder(null, null);
            processInstance = new ProcessInstance(
                ServiceFactory.getRepositoryService().getProcessDefinition(id),
                builder);
        } catch (DefinitionNotFoundException e) {
            System.out.println("Definition nicht gefunden! ");
            e.printStackTrace();
        }
        Navigator navigator = new NavigatorImplMock();
        token = new BpmnToken(processInstance.getDefinition().getStartNodes().get(0), processInstance, navigator);
        tobi = instanceDefinition.getTobi();
        jannik = instanceDefinition.getJannik();
        jan = instanceDefinition.getJan();
        worklistManager = engineServices.getWorklistService();
    }

    /**
     * Tests correct assignment of items to roles.
     */
    @Test
    void testCorrectAssignmentOfItemsToRoles() {

        BpmnHumanTaskActivity humanActivity1 = (BpmnHumanTaskActivity) instanceDefinition.getHuman1()
        .getActivityBehaviour();
        BpmnHumanTaskActivity humanActivity2 = (BpmnHumanTaskActivity) instanceDefinition.getHuman5()
        .getActivityBehaviour();
        AbstractCreationPattern pattern1 = (AbstractCreationPattern) humanActivity1.getCreationPattern();
        AbstractCreationPattern pattern2 = (AbstractCreationPattern) humanActivity2.getCreationPattern();
        Set<AbstractResource<?>> assignedResources1 = pattern1.getAssignedResources();
        Set<AbstractResource<?>> assignedResources2 = pattern2.getAssignedResources();
        Role objectionClerk = instanceDefinition.getObjectionClerk();
        Role allowanceClerk = instanceDefinition.getAllowanceClerk();
        Assert.assertTrue(assignedResources1.contains(objectionClerk));
        Assert.assertFalse(assignedResources1.contains(allowanceClerk));
        Assert.assertTrue(assignedResources2.contains(allowanceClerk));
        Assert.assertFalse(assignedResources2.contains(objectionClerk));
    }

    /**
     * Tests if the process can be executed on the whole.
     * 
     * @throws JodaEngineException
     *             thrown if token can't be executed
     */
    @Test
    public void testProcessRuns()
    throws JodaEngineException {

        assertEquals(token.getCurrentNode(), instanceDefinition.getStartNode());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getSystem1());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman1());
        token.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor1());
        // let the XOR take the "false" way
        token.getInstance().getContext().setVariable("widerspruch", "abgelehnt");
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman2());
        token.executeStep();
        executeHumanTaskBy(jannik);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor2());
        // let the XOR take the "true" way
        token.getInstance().getContext().setVariable("neueAspekte", "ja");
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman3());
        token.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor3());
        // let the XOR take the "true" way
        token.getInstance().getContext().setVariable("aufrecht", "ja");
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor4());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman4());
        token.executeStep();
        executeHumanTaskBy(jannik);
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman5());
        token.executeStep();
        executeHumanTaskBy(jan);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor5());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getSystem2());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getEndNode());
    }

    /**
     * Tests if the process can be executed on the whole.
     * 
     * @throws JodaEngineException
     *             thrown if token can't be executed
     */
    @Test
    public void testProcessRuns2()
    throws JodaEngineException {

        token.executeStep();
        token.executeStep();
        token.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor1());
        // let the XOR take the "right" way
        token.getInstance().getContext().setVariable("widerspruch", "stattgegeben");
        token.executeStep();

        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman5());
        token.executeStep();
        executeHumanTaskBy(jan);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor5());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getSystem2());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getEndNode());
    }

    /**
     * Execute human task by.
     * 
     * @param participant
     *            the participant
     */
    private void executeHumanTaskBy(Participant participant) {

        List<AbstractWorklistItem> items = participant.getWorklist().getWorklistItems();
        assertEquals(items.size(), 1);
        worklistManager.claimWorklistItemBy(items.get(0), participant);
        worklistManager.beginWorklistItemBy(items.get(0), participant);
        worklistManager.completeWorklistItemBy(items.get(0), participant);
        items = participant.getWorklist().getWorklistItems();
        assertEquals(items.size(), 0);
    }
}
