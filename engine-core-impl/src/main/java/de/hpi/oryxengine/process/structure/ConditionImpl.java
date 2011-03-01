package de.hpi.oryxengine.process.structure;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.hpi.oryxengine.process.instance.ProcessInstance;

/**
 * The Class ConditionImpl.
 * It should be a Condition and it should be evaluated properly. (to true or false)
 */
public class ConditionImpl implements Condition {
    
    /** The result. */
    private boolean result;
    
    /** The set. */
    private Set<?> set;
    
    /**
     * Instantiates a new condition impl.
     *
     * @param variablesToCheck the variables to check
     */
    public ConditionImpl(HashMap<String, Object> variablesToCheck) {

        set = variablesToCheck.entrySet();
        result = true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setFalse() {
        result = false;
    }
    
    /**
     * Instantiates a new condition impl.
     */
    public ConditionImpl() {
        set = Collections.EMPTY_SET;
        result = true;
    }

    /**
     * evaluates a Condition, returning true or false.
     *
     * @param instance the instance
     * @return true or false depending on the condition.
     * @see de.hpi.oryxengine.process.structure.Condition#evaluate()
     */
    public boolean evaluate(ProcessInstance instance) {

        Iterator<?> i = set.iterator();
        
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            if (instance.getVariable((String) me.getKey()) != me.getValue()) {
                result = false;
                break;
            }
        }
        return result;
    }

}
