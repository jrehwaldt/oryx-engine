package de.hpi.oryxengine.process.token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.exception.DalmatinaException;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.ProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The implementation of a process token.
 */
public class TokenImpl implements Token {

    private UUID id;

    private Node currentNode;

    private ProcessInstance instance;

    private Transition lastTakenTransition;

    private Navigator navigator;

    private boolean suspended;

    private List<Token> lazySuspendedProcessingTokens;

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     */
    public TokenImpl(Node startNode, ProcessInstance instance) {

        this(startNode, instance, null);
    }

    /**
     * Instantiates a new process token impl.
     * 
     * @param startNode
     *            the start node
     * @param instance
     *            the instance
     * @param navigator
     *            the navigator
     */
    public TokenImpl(Node startNode, ProcessInstance instance, Navigator navigator) {

        this.currentNode = startNode;
        this.instance = instance;
        this.navigator = navigator;
    }

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     */
    public TokenImpl(Node startNode) {

        this(startNode, new ProcessInstanceImpl(null), null);
    }

    /**
     * Gets the current node. So the position where the execution of the Processtoken is at.
     * 
     * @return the current node
     * @see de.hpi.oryxengine.process.token.Token#getCurrentNode()
     */
    @Override
    public Node getCurrentNode() {

        return currentNode;
    }

    /**
     * Sets the current node.
     * 
     * @param node
     *            the new current node {@inheritDoc}
     */
    @Override
    public void setCurrentNode(Node node) {

        currentNode = node;
    }

    @Override
    public UUID getID() {

        return id;
    }

    /**
     * Execute step.
     * 
     * @throws Exception
     *             the exception
     * @see de.hpi.oryxengine.process.token.Token#executeStep()
     */
    public void executeStep()
    throws Exception {

        lazySuspendedProcessingTokens = getCurrentNode().getIncomingBehaviour().join(this);
        getCurrentNode().getActivity().execute(this);

        // Aborting the further execution of the process by the token, because it was suspended
        if (suspended) {
            return;
        }

        List<Token> splitedTokens = getCurrentNode().getOutgoingBehaviour().split(getLazySuspendedProcessingToken());

        for (Token token : splitedTokens) {
            navigator.addWorkToken(token);
        }

        lazySuspendedProcessingTokens = null;
    }

    /**
     * Navigate to.
     * 
     * @param transitionList
     *            the node list
     * @return the list
     */
    @Override
    public List<Token> navigateTo(List<Transition> transitionList) {

        List<Token> tokensToNavigate = new ArrayList<Token>();
        if (transitionList.size() == 1) {
            Transition transition = transitionList.get(0);
            Node node = transition.getDestination();
            this.setCurrentNode(node);
            this.lastTakenTransition = transition;
            tokensToNavigate.add(this);
        } else {
            for (Transition transition : transitionList) {
                Node node = transition.getDestination();
                Token newToken = createNewToken(node);
                newToken.setLastTakenTransition(transition);
                tokensToNavigate.add(newToken);
            }

            // this is needed, as the this-token would be left on the node that triggers the split.
            instance.removeToken(this);
        }
        return tokensToNavigate;

    }

    /**
     * Creates a new token in the same context.
     * 
     * @param node
     *            the node
     * @return the token {@inheritDoc}
     */
    @Override
    public Token createNewToken(Node node) {

        Token newToken = instance.createToken(node, navigator);
        return newToken;
    }

    @Override
    public boolean joinable() {

        return this.instance.getContext().allIncomingTransitionsSignaled(this.currentNode);
    }

    @Override
    public Token performJoin() {

        Token token = new TokenImpl(currentNode, instance, navigator);
        instance.getContext().removeIncomingTransitions(currentNode);
        return token;
    }

    @Override
    public ProcessInstance getInstance() {

        return instance;
    }

    @Override
    public Transition getLastTakenTransition() {

        return lastTakenTransition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastTakenTransition(Transition t) {

        this.lastTakenTransition = t;
    }

    @Override
    public void suspend() {

        suspended = true;
        navigator.addSuspendToken(this);
    }

    @Override
    public void resume()
    throws DalmatinaException {

        navigator.removeSuspendToken(this);
        getCurrentNode().getActivity().signal(this);
        List<Token> splittedTokens = getCurrentNode().getOutgoingBehaviour().split(getLazySuspendedProcessingToken());

        for (Token token : splittedTokens) {
            navigator.addWorkToken(token);
        }

        lazySuspendedProcessingTokens = null;
    }

    private List<Token> getLazySuspendedProcessingToken() {

        if (lazySuspendedProcessingTokens == null) {
            lazySuspendedProcessingTokens = new ArrayList<Token>();
        }

        return lazySuspendedProcessingTokens;
    }

    @Override
    public Navigator getNavigator() {

        return navigator;
    }

}
