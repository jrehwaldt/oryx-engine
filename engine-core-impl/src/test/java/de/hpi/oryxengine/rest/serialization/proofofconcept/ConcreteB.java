package de.hpi.oryxengine.rest.serialization.proofofconcept;


/**
 * Bidirectional reference.
 */
public class ConcreteB {
    private transient AbstractA ref;
    
    private String test = "Hallo";
    
    /**
     * Circular reference test.
     */
    protected ConcreteB() { }
    
    /**
     * Default constructor.
     * 
     * @param ref a circular reference
     */
    public ConcreteB(AbstractA ref) {
        this.ref = ref;
    }
    
    /**
     * Getter.
     * 
     * @return a ref
     */
    public AbstractA getRef() {
        return this.ref;
    }
    
    /**
     * Getter.
     * 
     * @return a string
     */
    public String getTest() {
        return this.test;
    }
}
