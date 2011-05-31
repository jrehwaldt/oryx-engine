package org.jodaengine.node.incomingbehaviour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jodaengine.node.outgoingbehaviour.ComplexGatewayState;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.Token;

/**
 * Realizes the join behaviour of a complex gateway as defined in the BPMN 2.0 specification.
 */
public class ComplexJoinBehaviour extends AbstractIncomingBehaviour {

    public static final String STATE_VARIABLE_NAME = "state";
    private int triggerNumber;

    /**
     * Instantiates a new complex join behaviour.
     * 
     * @param triggerNumber
     *            sets how many incoming branches have to be signaled until the join can be performed. should be greater
     *            than the number of incoming transitions, as this lets the process instance die.
     */
    public ComplexJoinBehaviour(int triggerNumber) {

        this.triggerNumber = triggerNumber;
    }

    @Override
    public synchronized List<Token> join(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        context.setWaitingExecution(token.getLastTakenTransition());
        return super.join(token);
    }

    @Override
    protected boolean joinable(Token token) {

        ProcessInstanceContext context = token.getInstance().getContext();
        List<Transition> signaledTransitions = context.getWaitingExecutions(token.getCurrentNode());

        // make a set to ignore doubled entries, etc.
        Set<Transition> singaledTransitionsSet = new HashSet<Transition>(signaledTransitions);
        ComplexGatewayState state = getGatewayState(context, token.getCurrentNode());

        // TODO synchronization

        switch (state) {
            case WAITING_FOR_START:
                if (singaledTransitionsSet.size() >= triggerNumber) {
                    return true;
                }
                break;

            case WAITING_FOR_RESET:
                if (singaledTransitionsSet.size() == token.getCurrentNode().getIncomingTransitions().size()) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected List<Token> performJoin(Token token) {

        List<Token> proceedingTokens = new ArrayList<Token>();
        ProcessInstanceContext context = token.getInstance().getContext();
        ComplexGatewayState state = getGatewayState(context, token.getCurrentNode());
        switch (state) {
            case WAITING_FOR_START:
                proceedingTokens.add(token);
                setGatewayState(context, token.getCurrentNode(), ComplexGatewayState.WAITING_FOR_RESET);
                break;
            case WAITING_FOR_RESET:
                // we do not forward tokens here, as this is not specified by the discriminator pattern. Implement this,
                // if you want to implement the complete complex gateway behaviour as specified.
                setGatewayState(context, token.getCurrentNode(), ComplexGatewayState.WAITING_FOR_START);
                context.removeIncomingTransitions(token.getCurrentNode());

                // recursively trigger again as often, as its possible (should be usually only once, as the gateway
                // cannot be reset after anymore.
                proceedingTokens.addAll(super.join(token));
                break;
            default:
                break;
        }
        return proceedingTokens;
    }

    /**
     * Gets the {@link ComplexGatewayState} of the gateway, that is represented by the node.
     * 
     * @param context
     *            the context
     * @param node
     *            the node
     * @return the gateway state
     */
    private synchronized ComplexGatewayState getGatewayState(ProcessInstanceContext context, Node node) {

        String variableIdentifier = STATE_VARIABLE_NAME;
        Object variable = context.getNodeVariable(node, variableIdentifier);
        if (variable == null) {
            variable = ComplexGatewayState.WAITING_FOR_START;
            context.setNodeVariable(node, variableIdentifier, variable);
        }
        ComplexGatewayState state = (ComplexGatewayState) variable;

        return state;
    }

    /**
     * Sets the gateway state for the supplied node.
     * 
     * @param context
     *            the context
     * @param node
     *            the node
     * @param state
     *            the state
     */
    private void setGatewayState(ProcessInstanceContext context, Node node, ComplexGatewayState state) {

        String variableIdentifier = STATE_VARIABLE_NAME;
        context.setNodeVariable(node, variableIdentifier, state);
    }

}
