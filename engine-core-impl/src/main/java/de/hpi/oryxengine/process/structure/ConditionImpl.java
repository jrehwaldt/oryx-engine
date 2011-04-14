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
    
    private String compareWith;
    
    private Set<?> set;
    
    /**
     * Instantiates a new condition with a given comparator.
     *
     * @param variablesToCheck the variables to check
     * @param compareWith the comparator, yet == <= >= are possible for integers only
     */
    public ConditionImpl(Map<String, Object> variablesToCheck, String compareWith) {

        this.set = variablesToCheck.entrySet();
        this.compareWith = compareWith;
    }

    /**
     * Instantiates a new condition impl.
     */
    public ConditionImpl() {
        set = Collections.EMPTY_SET;
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
        boolean result = true;
        
        while (i.hasNext()) {
            ProcessInstanceContext context = token.getInstance().getContext();
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> me = (Map.Entry<String, Object>) i.next();
            Object contextValue = context.getVariable((String) me.getKey());
            
            if (contextValue == null || me.getValue() == null) {
                result = false;
                break;
            }

            // TODO Jannik make more generic with Juels, atm only integers allowed with == <= >=
            if (this.compareWith == "=="
                && !sameAs(Integer.parseInt(contextValue.toString()), Integer.parseInt(me.getValue().toString()))) {
                
                result = false;
                break;
                
            } else if (this.compareWith == ">="
                && !greaterThan(Integer.parseInt((String) contextValue), Integer.parseInt((String) me.getValue()))) {
                
                result = false;
                break;
                
            } else if (this.compareWith == "<="
                && !lowerThan(Integer.parseInt(contextValue.toString()), Integer.parseInt(me.getValue().toString()))) {
                
                result = false;
                break;
                
            }
                 
            
        }
        return result;
    }
    
    /**
     * Greater than functionality.
     *
     * @param value1 the context Value parsed as Integer
     * @param value2 the wanted value parsed as Integer
     * @return true, if context value is greater than the wanted value
     */
    private  boolean greaterThan(int value1, int value2) {
        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation > 0;
        
    }
    
    /**
     * Lower than functionality.
     *
     * @param value1 the context Value parsed as Integer
     * @param value2 the wanted value parsed as Integer
     * @return true, if context value is lower than the wanted value
     */
    private  boolean lowerThan(int value1, int value2) {
        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation < 0;
        
    }

    
    /**
     * Same as functionality.
     *
     * @param value1 the context Value parsed as Integer
     * @param value2 the wanted value parsed as Integer
     * @return true, if context value is lower than the wanted value
     */
    private  boolean sameAs(int value1, int value2) {
        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation == 0;
        
    }
}
