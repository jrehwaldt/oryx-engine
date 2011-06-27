package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.Collection;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;

/**
 * The class TakeAllSplitBehaviour will signal the first outgoing {@link ControlFlow},
 * of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    private String defaultFlowId;
    
    /**
     * Creates a {@link XORSplitBehaviour} with a default flow, identified by the provided id.
     *
     * @param defaultFlowId the default flow id
     */
    public XORSplitBehaviour(String defaultFlowId) {
        this.defaultFlowId = defaultFlowId;
    }
    
    /**
     * Contructor to use, if no default flow has been specified.
     */
    public XORSplitBehaviour() {
        this.defaultFlowId = null;
    }
    
    /**
     * Split according to the {@link ControlFlow}s.
     * 
     * @param tokens
     *            the tokens
     * @return the list
     * @throws NoValidPathException
     *             the no valid path exception {@inheritDoc}
     */
    @Override
    public Collection<Token> split(Collection<Token> tokens)
    throws NoValidPathException {
        
        if (tokens.isEmpty()) {
            return tokens;
        }
        
        Collection<ControlFlow> controlFlowList = new ArrayList<ControlFlow>();
        Collection<Token> tokensToNavigate = null;
        
        // we look through the outgoing {@link ControlFlow}s and try to find at least one,
        // which condition evaluates true and
        // then return it as the to-be-taken {@link ControlFlow}
        for (Token instance : tokens) {
            Node currentNode = instance.getCurrentNode();
            for (ControlFlow controlFlow : currentNode.getOutgoingControlFlows()) {
                // evaluate 
                if (!controlFlow.getID().equals(defaultFlowId)) {
                    if (controlFlow.getCondition().evaluate(instance)) {
                        controlFlowList.add(controlFlow);
                        break;
                    }
                }
            }
            
            if (controlFlowList.isEmpty()) {
                if (defaultFlowId != null) {
                    for (ControlFlow controlFlow : currentNode.getOutgoingControlFlows()) {
                        if (controlFlow.getID().equals(defaultFlowId)) {
                            controlFlowList.add(controlFlow);
                        }
                    }                   
                }
            }
            
            if (controlFlowList.size() == 0) {

                throw new NoValidPathException();
            }
            
            tokensToNavigate = instance.navigateTo(controlFlowList);
        }
        return tokensToNavigate;
    }
    
    /**
     * Gets the default flow id. This method is for testing purposes only.
     *
     * @return the default flow id
     */
    public String getDefaultFlowID() {
        return defaultFlowId;
    }

}
