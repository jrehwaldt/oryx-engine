package org.jodaengine.process.token.petri;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

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
import org.jodaengine.process.token.TokenUtil;
import org.jodaengine.process.token.builder.PetriTokenBuilder;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * The test for the process instance.
 */
public class MediumComplexPetriTokenTest {

    /** The process tokens. */
    private Token token, token2, token3;

    /** Different Nodes. */
    private Node firstStartPlace, firstEndPlace, secondStartPlace, secondEndPlace;
    
    private Node petriTransition, secondPetriTransition;
    
    private ProcessInstance instance;
    private TokenUtil util;

    /**
     * Set up.   
     *   
     * Overview, T = Token:
     * (TT) O -> | -> O
     *          \  
     *  (T) O -> | -> O
     */
    @BeforeMethod
    public void setUp() {

        // Place
        firstStartPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        firstStartPlace.setAttribute("name", "1");
        // Transition
        petriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        petriTransition.setAttribute("name", "2");
        // Place
        firstEndPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        firstEndPlace.setAttribute("name", "3");
        // Transition
        secondPetriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        secondPetriTransition.setAttribute("name", "4");
        // Place
        secondStartPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        secondStartPlace.setAttribute("name", "5");
        // Place
        secondEndPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        secondEndPlace.setAttribute("name", "6");

        firstStartPlace.transitionTo(petriTransition);
        petriTransition.transitionTo(firstEndPlace);
        firstStartPlace.transitionTo(secondPetriTransition);
        secondStartPlace.transitionTo(secondPetriTransition);
        secondPetriTransition.transitionTo(secondEndPlace);
        
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        token = instance.createToken(firstStartPlace);
        token2 = instance.createToken(secondStartPlace);
        token3 = instance.createToken(firstStartPlace);
        
        util = new TokenUtil();
    }

    /**
     * Tests the lower path.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testTheLowerPetriStepExecution() throws Exception {

        Token newToken;
        token2.executeStep();
        assertEquals(instance.getAssignedTokens().size(), 2, "There should be one token consumed,"
            + " and one left at the end and one left at the beginning node.");
        assertEquals(util.getTokensWhichAreOnNode(secondEndPlace, instance).size(), 1);
        newToken = util.getTokensWhichAreOnNode(secondEndPlace, instance).get(0);
        assertFalse(newToken == token2, "A new token should be produced, and not the old one reused");
        assertEquals(token3.getCurrentNode(), firstStartPlace, "The third token should not be affected");
        
    }

    
    /**
     * Tests both ways of the example petri net. First the lower way is taken, then the upper way.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testBothSimplePetriStepExecution() throws Exception {
        
        token2.executeStep();
        // We don't now which token is left, so lookup one of it
        Token remainingToken = util.getTokensWhichAreOnNode(firstStartPlace, instance).get(0);
        remainingToken.executeStep();

        assertEquals(instance.getAssignedTokens().size(), 2, "There should one token be consumed and two at the end.");
        assertEquals(util.getTokensWhichAreOnNode(firstEndPlace, instance).size(), 1);
        assertEquals(util.getTokensWhichAreOnNode(secondEndPlace, instance).size(), 1);
        
    }
    
    /**
     * Tests token execute on a token on a place, where are no following nodes.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testdeadEndPetriStepExecution() throws Exception {
        
        token2.executeStep();
        // We dont now which token is left, so lookup one of it
        Token remainingToken;
        remainingToken = util.getTokensWhichAreOnNode(firstStartPlace, instance).get(0);
        remainingToken.executeStep();
        
        // Both paths are at the end, now try to execute one of the tokens, which are located at the end.
        remainingToken = util.getTokensWhichAreOnNode(firstEndPlace, instance).get(0);
        remainingToken.executeStep();
        
        // There should not occur an error, nor should there be new tokens created.
        assertEquals(instance.getAssignedTokens().size(), 2, "There should be no new tokens.");
        assertEquals(util.getTokensWhichAreOnNode(firstEndPlace, instance).size(), 1);
        assertEquals(util.getTokensWhichAreOnNode(secondEndPlace, instance).size(), 1);
        
    }
    
    /**
     * Tests dead end at the beginning. The upper tokens are deleted, so the lower token can't do anything.
     * 
     * @throws Exception if it fails
     */
    @Test
    public void testdeadEndAtBeginningPetriStepExecution() throws Exception {
        
        instance.removeToken(token3);
        instance.removeToken(token);
        token2.executeStep();
       
        assertEquals(instance.getAssignedTokens().size(), 1, "There should be no new tokens.");
        assertEquals(util.getTokensWhichAreOnNode(firstStartPlace, instance).size(), 0);
        assertEquals(util.getTokensWhichAreOnNode(secondStartPlace, instance).size(), 1, 
            "There are not enough tokens to let the PetriTransition fire, so the token has to wait there.");
        
    }


}
