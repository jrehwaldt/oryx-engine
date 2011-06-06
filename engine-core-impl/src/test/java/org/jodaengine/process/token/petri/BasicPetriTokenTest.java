package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;

import org.jodaengine.navigator.Navigator;
import org.jodaengine.node.activity.NullActivity;
import org.jodaengine.node.incomingbehaviour.petri.TransitionJoinBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.PlaceSplitBehaviour;
import org.jodaengine.node.outgoingbehaviour.petri.TransitionSplitBehaviour;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.NodeImpl;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the process instance.
 */
public class BasicPetriTokenTest {

    private Token token = null;

    /** Different Nodes. */
    private Node startPlace = null, secondPlace = null, endPlace = null;
    
    private Node petriTransition, secondPetriTransition;
    
    private ProcessInstance instance;

    /**
     * Set up.     
     * 
     * Overview:
     * O -> | -> O -> | -> O
     */
    @BeforeMethod
    public void setUp() {

        // Place
        startPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        startPlace.setAttribute("name", "1");
        // Transition
        petriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        petriTransition.setAttribute("name", "2");
        // Place
        secondPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        secondPlace.setAttribute("name", "3");
        // Transition
        secondPetriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        secondPetriTransition.setAttribute("name", "4");
        // Place
        endPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        endPlace.setAttribute("name", "5");

        startPlace.transitionTo(petriTransition);
        petriTransition.transitionTo(secondPlace);
        secondPlace.transitionTo(secondPetriTransition);
        secondPetriTransition.transitionTo(endPlace);
        
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(startPlace);
    }

    /**
     * Test for taking all transitions.
     * Two new tokens shall be ready for execution if the parent token goes along all edges.
     * The new tokens should then point to the succeeding nodes of the initial token's node.
     *
     * @throws Exception the exception
     */
    @Test
    public void testTakeTransition() throws Exception {

        Node currentNode = token.getCurrentNode();
        
        List<Token> newTokens = token.navigateTo(currentNode.getOutgoingTransitions());
        
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one moved token.");
        assertEquals(newTokens.get(0).getCurrentNode(), petriTransition,
            "The new token should point to the following node.");
    }

    /**
     * Test the taking of a single transition.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testSimplePetriStepExecution() throws Exception {

        Token newToken;
        token.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be one new token created");
        newToken = instance.getAssignedTokens().get(0);
        assertFalse(newToken == token, "A new token should be produced, and not the old one reused");
        assertEquals(newToken.getCurrentNode(), secondPlace,
            "After one step the token should be located on the next place and NOT on the Transition");
        
        newToken.executeStep();
        newToken = instance.getAssignedTokens().get(0);
        assertEquals(newToken.getCurrentNode(), endPlace,
        "After two steps the token should be locatedon the last place");
        
    }


}
