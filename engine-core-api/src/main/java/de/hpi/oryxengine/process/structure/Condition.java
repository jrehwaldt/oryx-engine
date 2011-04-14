package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Interface for conditions.
 * 
 * @author Jannik Streek
 */
public interface Condition {

    /**
     * Evaluates the condition.
     * 
     * @param instance
     *            the instance
     * @return true, if successful
     */
    boolean evaluate(Token instance);

}
