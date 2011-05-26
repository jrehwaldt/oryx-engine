package org.jodaengine.factories.process;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
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
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenImpl;
import org.jodaengine.resource.Participant;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
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
            processInstance = new ProcessInstanceImpl(ServiceFactory.getRepositoryService().getProcessDefinition(id));
        } catch (DefinitionNotFoundException e) {
            System.out.println("Definition nicht gefunden! ");
            e.printStackTrace();
        }
        Navigator navigator = new NavigatorImplMock();
        token = new TokenImpl(processInstance.getDefinition().getStartNodes().get(0), processInstance, navigator);
        tobi = instanceDefinition.getTobi();
        jannik = instanceDefinition.getJannik();
        jan = instanceDefinition.getJan();
        worklistManager = engineServices.getWorklistService();
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
