package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface for conditions.
 */
public interface Condition {


    /**
     * Evaluate.
     *
     * @param instance the instance
     * @return true, if successful
     */
    boolean evaluate(Token instance);
    
    /**
     * Sets condition to false.
     */
    void setFalse();

}
