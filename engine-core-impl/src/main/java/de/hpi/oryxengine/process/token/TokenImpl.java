package de.hpi.oryxengine.process.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hpi.oryxengine.exception.IllegalNavigationException;
import de.hpi.oryxengine.process.definition.AbstractProcessDefinitionImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.structure.Transition;
import de.hpi.oryxengine.process.token.Token;

/**
 * The implementation of a process token.
 */
public class TokenImpl
implements Token {

    /** The id. */
    private UUID id;

    /** The current node. */
    private Node currentNode;

    /** The parent token. */
    private Token parentToken;

    /** The child tokens. */
    private List<Token> childTokens;

    /** The token variables. */
    private Map<String, Object> contextVariables;

    /**
     * Instantiates a new process token impl.
     * 
     * @param processDefinition the process definition
     * @param startNumber the start number
     */
    public TokenImpl(AbstractProcessDefinitionImpl processDefinition,
                               Integer startNumber) {

        // choose a start Node from the possible List of Nodes
        // TODO how to choose the start node?
        ArrayList<Node> startNodes = processDefinition.getStartNodes();
        currentNode = startNodes.get(startNumber);

    }

    /**
     * Instantiates a new process token impl.
     * 
     * @param startNode
     *            the start node
     */
    public TokenImpl(Node startNode) {
        this(startNode, null);
    }

    /**
     * Instantiates a new process token impl.
     * 
     * @param startNode
     *            the start node
     * @param parentToken
     *            the parent token
     */
    public TokenImpl(Node startNode,
                               Token parentToken) {

        currentNode = startNode;
        this.parentToken = parentToken;
        this.childTokens = new ArrayList<Token>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token getParentToken() {
        return parentToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentToken(Token token) {
        this.parentToken = token;
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
     * {@inheritDoc}
     */
    @Override
    public void setCurrentNode(Node node) {
        currentNode = node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Token> getChildTokens() {

        return childTokens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setChildTokens(List<Token> childTokens) {

        this.childTokens = childTokens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getID() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVariable(String name,
                            Object value) {

        getTokenVariables().put(name, value);
    }

    /**
     * Gets the variable.
     * 
     * @param name
     *            of the variable
     * @return the variable
     * @see de.hpi.oryxengine.process.token.Token#getVariable(java.lang.String)
     */
    public Object getVariable(String name) {

        return getTokenVariables().get(name);
    }

    /**
     * Gets the token variables.
     * 
     * @return the token variables
     */
    private Map<String, Object> getTokenVariables() {

        if (contextVariables == null) {
            contextVariables = new HashMap<String, Object>();
        }
        return contextVariables;
    }

    /**
     * Execute step.
     * 
     * @return list of process tokens
     * @see de.hpi.oryxengine.process.token.Token#executeStep()
     */
    public List<Token> executeStep() {

        return this.currentNode.execute(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Token> navigateTo(List<Node> nodeList)
    throws IllegalNavigationException {

        validateNodeList(nodeList);
        List<Token> tokensToNavigate = new ArrayList<Token>();
        if (nodeList.size() == 1) {
            Node node = nodeList.get(0);
            this.setCurrentNode(node);
            tokensToNavigate.add(this);
        } else {
            for (Node node: nodeList) {
                Token childToken = createChildToken(node);
                tokensToNavigate.add(childToken);
            }
        }
        return tokensToNavigate;

    }

    /**
     * Validate node list.
     * 
     * @param nodeList
     *            the node list
     * @throws IllegalNavigationException
     *             the illegal navigation exception
     */
    private void validateNodeList(List<Node> nodeList)
    throws IllegalNavigationException {

        ArrayList<Node> destinations = new ArrayList<Node>();
        for (Transition transition : currentNode.getTransitions()) {
            destinations.add(transition.getDestination());
        }
        if (!destinations.containsAll(nodeList)) {
            throw new IllegalNavigationException();
        }
    }

    /**
     * Take single transition.
     * 
     * @param t
     *            the transition to take
     * @return list of process tokens
     * @see de.hpi.oryxengine.process.token.Token
     *      #takeSingleTransition(de.hpi.oryxengine.process.structure.Transition)
     */
    public List<Token> takeSingleTransition(Transition t) {

        List<Token> tokensToNavigate = new LinkedList<Token>();
        this.currentNode = t.getDestination();
        tokensToNavigate.add(this);
        return tokensToNavigate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token createChildToken(Node node) {

        Token childToken = new TokenImpl(node);
        childToken.setParentToken(this);
        this.childTokens.add(childToken);
        return childToken;
    }

}
