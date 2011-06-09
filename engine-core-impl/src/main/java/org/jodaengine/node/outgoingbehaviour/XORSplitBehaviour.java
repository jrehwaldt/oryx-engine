package org.jodaengine.node.outgoingbehaviour;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.ControlFlow;
import org.jodaengine.process.token.Token;

/**
 * The Class TakeAllSplitBehaviour. Will signal the first outgoing {@link ControlFlow}, of which the condition evaluates to true.
 */
public class XORSplitBehaviour implements OutgoingBehaviour {

    /**
     * Split according to the {@link ControlFlow}s.
     * 
     * @param tokens
     *            the instances
     * @return the list
     * @throws NoValidPathException
     *             the no valid path exception {@inheritDoc}
     */
    @Override
    public List<Token> split(List<Token> tokens)
    throws NoValidPathException {

        if (tokens.size() == 0) {
            return tokens;
        }

        List<ControlFlow> controlFlowList = new ArrayList<ControlFlow>();
        List<Token> controlFlowsToNavigate = null;

        // we look through the outgoing {@link ControlFlow}s and try to find one at least, whose condition evaluates true and
        // then return it as the to-be-taken {@link ControlFlow}
        for (Token instance : tokens) {
            Node currentNode = instance.getCurrentNode();
            for (ControlFlow controlFlow : currentNode.getOutgoingControlFlows()) {
                if (controlFlow.getCondition().evaluate(instance)) {
                    controlFlowList.add(controlFlow);
                    break;
                }
            }

            if (controlFlowList.size() == 0) {

                throw new NoValidPathException();

            }

            controlFlowsToNavigate = instance.navigateTo(controlFlowList);

        }
        return controlFlowsToNavigate;
    }

}
