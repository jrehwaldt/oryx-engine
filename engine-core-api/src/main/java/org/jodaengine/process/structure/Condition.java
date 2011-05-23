package org.jodaengine.process.structure;

import org.jodaengine.process.token.BPMNToken;

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
    boolean evaluate(BPMNToken instance);

}
