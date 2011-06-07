package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;

/**
 * Realizes the split behaviour of the complex gateway as defined in the BPMN 2.0 specification.
 */
public class ComplexSplitBehaviour implements OutgoingBehaviour {

    @Override
    public List<Token> split(List<Token> tokens)
    throws NoValidPathException {

        if (tokens.size() == 0) {
            return tokens;
        }

        List<ControlFlow> transitionList = new ArrayList<ControlFlow>();
        List<Token> transitionsToNavigate = null;

        // we look through the outgoing transitions and try to find one at least, whose condition evaluates true and
        // then return it as the to-be-taken transition
        for (Token instance : tokens) {
            Node currentNode = instance.getCurrentNode();
            for (ControlFlow controlFlow : currentNode.getOutgoingControlFlows()) {
                if (controlFlow.getCondition().evaluate(instance)) {
                    transitionList.add(controlFlow);
                }
            }

            if (transitionList.size() == 0) {

                throw new NoValidPathException();

            }

            transitionsToNavigate = instance.navigateTo(transitionList);

        }
        return transitionsToNavigate;
    }

}
