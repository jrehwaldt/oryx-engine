package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.NavigatorImplMock;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.ProcessInstance;
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
    private ProcessInstance processInstance = null;
    private ShortenedReferenceProcessDeployer instanceDefinition = null;
    private Participant tobi = null;
    private WorklistService worklistManager = null;

    @BeforeMethod
    @Override
    public void setUp()
    throws IllegalStarteventException {

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
        instanceDefinition.getJannik();
        instanceDefinition.getJan();
        worklistManager = ServiceFactory.getWorklistService();
    }

    /**
     * Test if process runs correctly.
     *
     * @throws DalmatinaException thrown if token can't be executed
     */
    @Test
    public void testProcessRuns()
    throws DalmatinaException {

        assertEquals(token.getCurrentNode(), instanceDefinition.getStartNode());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getSystem1());
        token.executeStep();
        assertEquals(token.getCurrentNode(), instanceDefinition.getHuman1());
        token.executeStep();
        // first human task, let somebody do it
        List<AbstractWorklistItem> items = tobi.getWorklist().getWorklistItems();
        assertEquals(items.size(), 1);
        worklistManager.claimWorklistItemBy(items.get(0), tobi);
        worklistManager.beginWorklistItemBy(items.get(0), tobi);
        worklistManager.completeWorklistItemBy(items.get(0), tobi);
        items = tobi.getWorklist().getWorklistItems();
        assertEquals(items.size(), 0);
        assertEquals(token.getCurrentNode(), instanceDefinition.getXor1());
        // can't go on testing here, because XOR and Conditions need to be refactored first
    }

}
