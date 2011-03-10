package de.hpi.oryxengine.factory;

import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;

/**
 * A factory for creating HeavyComputationProcessToken objects / process instances.
 * 
 * Creates a process isntance with NUMBER_OF_NODES Hashcomputation nodes using the default algorithm.
 * This is used to create a somewhat higher load on the processinstances.
 * The results are stored in the variables hash1 to hash5.
 */
public class HeavyComputationProcessTokenFactory {
    private final static int NUMBER_OF_NODES = 5;
    private final static String[] PASSWORDS = {"Hallo", "toor", "278dahka!§-", "muhhhh", "HPI"};

    private Node[] nodes;
    private Token t;

    /**
     * Instantiates a new heavy computation process token factory.
     */
    public HeavyComputationProcessTokenFactory() {

        nodes = new Node[NUMBER_OF_NODES];
    }

    /**
     * Initialize this HashComputation nodes of the factory and connects them so we get an actual graph.
     */
    public void initializeNodes() {

        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            HashComputationNodeFactory factory = new HashComputationNodeFactory("hash" + String.valueOf(i + 1),
                PASSWORDS[i], "SHA-512");
            nodes[i] = factory.create();
            if (i > 0) {
                nodes[i - 1].transitionTo(nodes[i]);
            }
        }
    }

    /**
     * Initializes the token which is set to the first node.
     */
    public void initializeToken() {
        SimpleProcessTokenFactory factory = new SimpleProcessTokenFactory();
        t = factory.create(nodes[0]);
    }
    
    /**
     * Creates the Heavy Compuation process token.
     *
     * @return the token
     */
    public Token create() {
        this.initializeNodes();
        this.initializeToken();
        return this.t;
    }

}
