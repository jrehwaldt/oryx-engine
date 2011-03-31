package de.hpi.oryxengine.process.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.hpi.oryxengine.process.instance.ProcessInstanceContext;
import de.hpi.oryxengine.process.token.Token;

/**
 * The Class ConditionImpl.
 * It should be a Condition and it should be evaluated properly. (to true or false)
 * @author Jannik Streek
 */
public class ConditionImpl implements Condition {
    
    private boolean result;
    
    private Set<?> set;
    
    /**
     * Instantiates a new condition impl.
     *
     * @param variablesToCheck the variables to check
     */
    public ConditionImpl(Map<String, Object> variablesToCheck) {

        set = variablesToCheck.entrySet();
        result = true;
    }
    
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
     * @param token the token
     * @return true or false depending on the condition.
     * @see de.hpi.oryxengine.process.structure.Condition#evaluate()
     */
    public boolean evaluate(Token token) {

        Iterator<?> i = set.iterator();
        
        while (i.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> me = (Map.Entry<String, Object>) i.next();
            ProcessInstanceContext context = token.getInstance().getContext();
            if (context.getVariable((String) me.getKey()) != me.getValue()) {
                result = false;
                break;
            }
        }
        return result;
    }

}
