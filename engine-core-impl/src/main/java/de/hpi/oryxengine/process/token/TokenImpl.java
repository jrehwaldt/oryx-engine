package de.hpi.oryxengine.process.token;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.instance.ProcessInstanceContextImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;

/**
 * The implementation of a process token.
 */
public class TokenImpl implements Token {

    /** The id. */
    private UUID id;

    /** The current node. */
    private Node currentNode;

    /** The context. */
    private ProcessInstanceContext context;

    /** The last taken transition. */
    private Transition lastTakenTransition;
    
    /** The navigator. */
    private Navigator navigator;

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     * @param context
     *            the context
     */
    public TokenImpl(Node startNode, ProcessInstanceContext context) {

        this(startNode, null, context, null);
    }

    /**
     * Instantiates a new process token impl.
     * 
     * @param startNode
     *            the start node
     * @param parentToken
     *            the parent token
     * @param context
     *            the context
     */
    public TokenImpl(Node startNode, Token parentToken, ProcessInstanceContext context, Navigator navigator) {

        currentNode = startNode;
        this.context = context;
        this.navigator = navigator;
    }

    /**
     * Instantiates a new token impl.
     * 
     * @param startNode
     *            the start node
     */
    public TokenImpl(Node startNode) {

        this(startNode, null, new ProcessInstanceContextImpl(), null);
    }

    /**
     * Gets the current node. So the position where the execution of the Processtoken is at.
     * 
     * @return the current node
     * @see de.hpi.oryxengine.process.token.Token#getCurrentNode()
     */
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

    /**
     * Gets the iD.
     * 
     * @return the iD {@inheritDoc}
     */
    @Override
    public UUID getID() {

        return id;
    }

    /**
     * Execute step.
     *
     * @return list of process tokens
     * @throws Exception the exception
     * @see de.hpi.oryxengine.process.token.Token#executeStep()
     */
    public List<Token> executeStep() throws Exception {

        return this.currentNode.execute(this);
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

        Token newToken = new TokenImpl(node, this.context);
        return newToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean joinable() {

        return this.context.allIncomingTransitionsSignaled(this.currentNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token performJoin() {

        Token token = new TokenImpl(currentNode, context);
        context.removeIncomingTransitions(currentNode);
        return token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProcessInstanceContext getContext() {

        return context;
    }

    /**
     * {@inheritDoc}
     */
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

}
