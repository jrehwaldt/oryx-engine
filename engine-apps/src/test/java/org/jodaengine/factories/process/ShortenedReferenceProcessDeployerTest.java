package org.jodaengine.factories.process;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.jodaengine.NavigatorImplMock;
import org.jodaengine.ServiceFactory;
import org.jodaengine.WorklistService;
import org.jodaengine.exception.DefinitionNotFoundException;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class ShortenedReferenceProcessDeployerTest. This is the test class for {@link ShortenedReferenceProcessDeployer}
 * .
 */
public class ShortenedReferenceProcessDeployerTest extends AbstractProcessDeployerTest {

    private BPMNToken bPMNToken = null;
    private AbstractProcessInstance processInstance = null;
    private ShortenedReferenceProcessDeployer instanceDefinition = null;
    private Participant tobi = null;
    private WorklistService worklistManager = null;
    private Participant jannik = null;
    private Participant jan = null;

    @BeforeMethod
    @Override
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException {

        instanceDefinition = new ShortenedReferenceProcessDeployer();
        this.id = instanceDefinition.deploy(engineServices);
        try {
            processInstance = new ProcessInstanceImpl(ServiceFactory.getRepositoryService().getProcessDefinition(id));
        } catch (DefinitionNotFoundException e) {
            System.out.println("Definition nicht gefunden! ");
            e.printStackTrace();
        }
        Navigator navigator = new NavigatorImplMock();
        bPMNToken = new BPMNTokenImpl(processInstance.getDefinition().getStartNodes().get(0), processInstance, navigator);
        tobi = instanceDefinition.getTobi();
        jannik = instanceDefinition.getJannik();
        jan = instanceDefinition.getJan();
        worklistManager = ServiceFactory.getWorklistService();
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

        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getStartNode());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getSystem1());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman1());
        bPMNToken.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor1());
        // let the XOR take the "false" way
        bPMNToken.getInstance().getContext().setVariable("widerspruch", "abgelehnt");
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman2());
        bPMNToken.executeStep();
        executeHumanTaskBy(jannik);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor2());
        // let the XOR take the "true" way
        bPMNToken.getInstance().getContext().setVariable("neueAspekte", "ja");
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman3());
        bPMNToken.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor3());
        // let the XOR take the "true" way
        bPMNToken.getInstance().getContext().setVariable("aufrecht", "ja");
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor4());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman4());
        bPMNToken.executeStep();
        executeHumanTaskBy(jannik);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman5());
        bPMNToken.executeStep();
        executeHumanTaskBy(jan);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor5());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getSystem2());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getEndNode());
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

        bPMNToken.executeStep();
        bPMNToken.executeStep();
        bPMNToken.executeStep();
        executeHumanTaskBy(tobi);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor1());
        // let the XOR take the "right" way
        bPMNToken.getInstance().getContext().setVariable("widerspruch", "stattgegeben");
        bPMNToken.executeStep();

        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getHuman5());
        bPMNToken.executeStep();
        executeHumanTaskBy(jan);
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getXor5());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getSystem2());
        bPMNToken.executeStep();
        assertEquals(bPMNToken.getCurrentNode(), instanceDefinition.getEndNode());
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
