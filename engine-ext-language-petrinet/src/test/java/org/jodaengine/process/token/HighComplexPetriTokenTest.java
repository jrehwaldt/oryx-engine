package org.jodaengine.process.token;


import static org.testng.Assert.assertEquals;

import org.jodaengine.exception.JodaEngineException;
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
 * The test for a complex petri net.
 */
public class HighComplexPetriTokenTest {
    
    private Node petriTransition, secondPetriTransition, thirdPetriTransition;
    
    private ProcessInstance instance;
    private TokenUtil util;

    private Node firstStartPlace, secondStartPlace, thirdPlace, fourthPlace, endPlace;

    private Token token;

    /**
     * Set up.     
     * 
     * Overview:
     * O ->\      O -> | ->\
     *      | -><           O
     * O ->/      O -> | ->/
     * 
     */
    @BeforeMethod
    public void setUp() {

        // firstStartPlace
        firstStartPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        firstStartPlace.setAttribute("name", "1");
        // secondStartPlace
        secondStartPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        secondStartPlace.setAttribute("name", "2");
        // Transition
        petriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        petriTransition.setAttribute("name", "3");
        // Place
        thirdPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        thirdPlace.setAttribute("name", "4");
        // Place
        fourthPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        fourthPlace.setAttribute("name", "5");
        // Transition
        secondPetriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        secondPetriTransition.setAttribute("name", "4");
        // Transition
        thirdPetriTransition = new NodeImpl(new NullActivity(), new TransitionJoinBehaviour(),
            new TransitionSplitBehaviour());
        thirdPetriTransition.setAttribute("name", "4");
        // endPlace
        endPlace = new NodeImpl(new NullActivity(), null, new PlaceSplitBehaviour());
        endPlace.setAttribute("name", "6");

        firstStartPlace.transitionTo(petriTransition);
        secondStartPlace.transitionTo(petriTransition);
        petriTransition.transitionTo(thirdPlace);
        petriTransition.transitionTo(fourthPlace);
        thirdPlace.transitionTo(secondPetriTransition);
        fourthPlace.transitionTo(thirdPetriTransition);
        secondPetriTransition.transitionTo(endPlace);
        thirdPetriTransition.transitionTo(endPlace);
        
        
        TokenBuilder tokenBuilder = new PetriTokenBuilder(Mockito.mock(Navigator.class), null);
        instance = new ProcessInstance(null, tokenBuilder);
        instance.createToken(firstStartPlace);
        token = instance.createToken(secondStartPlace);
        
        util = new TokenUtil();
    }

    /**
     * Tests the whole petri net.
     * @throws JodaEngineException 
     */
    @Test
    public void testThePetriNet() throws JodaEngineException {

        Token newToken;
        token.executeStep();
        
        newToken = util.getTokensWhichAreOnNode(thirdPlace, instance).get(0);
        newToken.executeStep();
        
        newToken = util.getTokensWhichAreOnNode(fourthPlace, instance).get(0);
        newToken.executeStep();
        
        assertEquals(util.getTokensWhichAreOnNode(endPlace, instance).size(), 2,
            "There should be 2 tokens on the endPlace.");
        assertEquals(instance.getAssignedTokens().size(), 2);
        
    }
    
}
