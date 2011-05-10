package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.NavigatorImplMock;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.JodaEngineException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.process.token.TokenImpl;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

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
    public void setUp()
    throws IllegalStarteventException, ResourceNotAvailableException {

        instanceDefinition = new ShortenedReferenceProcessDeployer();
        this.uuid = instanceDefinition.deploy();
        try {
            processInstance = new ProcessInstanceImpl(ServiceFactory.getRepositoryService().getProcessDefinition(uuid));
        } catch (DefinitionNotFoundException e) {
            System.out.println("Definition nicht gefunden! ");
            e.printStackTrace();
        }
        Navigator navigator = new NavigatorImplMock();
        token = new TokenImpl(processInstance.getDefinition().getStartNodes().get(0), processInstance, navigator);
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
        token.getInstance().getContext().setVariable("neue Aspekte", "ja");
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
     * @param participant the participant
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
