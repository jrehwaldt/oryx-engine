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
                break;
            default:
                break;
        }
        return proceedingTokens;
    }

    private synchronized ComplexGatewayState getGatewayState(ProcessInstanceContext context, Node node) {

        String variableIdentifier = node.getID() + "-state";
        Object variable = context.getVariable(variableIdentifier);
        if (variable == null) {
            variable = ComplexGatewayState.WAITING_FOR_START;
            context.setVariable(variableIdentifier, variable);
        }
        ComplexGatewayState state = (ComplexGatewayState) variable;

        return state;
    }

    private void setGatewayState(ProcessInstanceContext context, Node node, ComplexGatewayState state) {

        String variableIdentifier = node.getID() + "-state";
        context.setVariable(variableIdentifier, state);
    }

}
