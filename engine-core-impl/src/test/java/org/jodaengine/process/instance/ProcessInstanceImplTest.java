package org.jodaengine.process.instance;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BPMNToken;
import org.jodaengine.process.token.BPMNTokenImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * The Class ProcessInstanceImplTest.
 */
public class ProcessInstanceImplTest {

    private AbstractProcessInstance<BPMNToken> instance = null;

    /**
     * F.
     */
    @Test
    public void testTokenCreation() {

        Navigator nav = mock(Navigator.class);
        Node<BPMNToken> node = mock(Node.class);
        BPMNToken token = new BPMNTokenImpl(node, instance, nav);
        instance.addToken(token);

        assertTrue(instance.getAssignedTokens().contains(token), "The instance should contain the token now.");

        assertNotNull(token.getInstance().getContext(), "The token should have a context to write to.");
    }

    /**
     * Before class.
     */
    @BeforeClass
    public void setUp() {

        ProcessDefinition definition = mock(ProcessDefinition.class);
        instance = new ProcessInstanceImpl<BPMNToken>(definition);

    }

}
