package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.process.instance.ProcessInstance;

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
    boolean evaluate(ProcessInstance instance);
    
    /**
     * Sets condition to false.
     */
    void setFalse();

}
