package de.hpi.oryxengine.process.structure;

import de.hpi.oryxengine.process.structure.Condition;

/**
 * The Class ConditionImpl.
 * It should be a Condition and it should be evaluated properly. (to true or false)
 */
public class ConditionImpl implements Condition {

    /**
     * evaluates a Condition, returning true or false.
     *
     * @return true or false depending on the condition.
     * @see de.hpi.oryxengine.process.structure.Condition#evaluate()
     */
    public boolean evaluate() {

        // TODO Atm for testing just return true
        return true;
    }

}
