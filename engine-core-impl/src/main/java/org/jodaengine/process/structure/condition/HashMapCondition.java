package org.jodaengine.process.structure.condition;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.Condition;
import org.jodaengine.process.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class HashMapCondition evaluates process instance context variables. The values to compare these with are to be
 * provided in a hashmap. Currently, only some simple operators for comparison are supported. For more, have a look at
 * {@link JuelExpressionCondition}.
 * 
 * @author Jannik Streek
 */
public class HashMapCondition implements Condition {

    private String compareWith;
    private boolean nullEvaluatesToTrue;

    private Set<?> set;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new condition with a given comparator.
     * 
     * @param variablesToCheck
     *            the variables to check
     * @param compareWith
     *            the comparator, yet == <= >= are possible for integers only
     * @param nullEvaluatesToTrue
     *            weather null evaluates to true
     */
    public HashMapCondition(Map<String, Object> variablesToCheck, String compareWith, boolean nullEvaluatesToTrue) {

        this.set = variablesToCheck.entrySet();
        this.compareWith = compareWith;
        this.nullEvaluatesToTrue = nullEvaluatesToTrue;
    }

    /**
     * Instantiates a new condition with a given comparator.
     * 
     * @param variablesToCheck
     *            the variables to check
     * @param compareWith
     *            the comparator, yet == <= >= are possible for integers only
     */
    public HashMapCondition(Map<String, Object> variablesToCheck, String compareWith) {

        this(variablesToCheck, compareWith, false);
    }

    /**
     * Instantiates a new hash map condition.
     */
    public HashMapCondition() {

        set = Collections.EMPTY_SET;
    }

    @Override
    public boolean evaluate(Token token) {

        Iterator<?> i = set.iterator();
        boolean result = true;
        logger.debug("HashMapCondition variables to check: {}", set);
        logger.debug("instance context variables: {}", token.getInstance().getContext().getVariableMap());

        while (i.hasNext()) {
            ProcessInstanceContext context = token.getInstance().getContext();
            @SuppressWarnings("unchecked")
            Map.Entry<String, Object> me = (Map.Entry<String, Object>) i.next();
            Object contextValue = context.getVariable((String) me.getKey());

            if ((!this.nullEvaluatesToTrue && contextValue == null) || me.getValue() == null) {

                result = false;
                break;
            } else if (this.nullEvaluatesToTrue && contextValue == null) {

                break;
            }

            if (contextValue instanceof java.lang.String && this.compareWith.equals("==")
                && !contextValue.equals(me.getValue())) {

                result = false;
                break;

            } else if (contextValue instanceof java.lang.String && this.compareWith.equals("!=")
                && contextValue.equals(me.getValue())) {

                result = false;
                break;

            } else if (!(contextValue instanceof java.lang.String) && this.compareWith.equals("==")
                && !sameAs(Integer.parseInt(contextValue.toString()), Integer.parseInt(me.getValue().toString()))) {

                result = false;
                break;

            } else if (!(contextValue instanceof java.lang.String) && this.compareWith.equals(">=")
                && !greaterThan(Integer.parseInt((String) contextValue), Integer.parseInt((String) me.getValue()))) {

                result = false;
                break;

            } else if (!(contextValue instanceof java.lang.String) && this.compareWith.endsWith("<=")
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
     * @param value1
     *            the context Value parsed as Integer
     * @param value2
     *            the wanted value parsed as Integer
     * @return true, if context value is greater than the wanted value
     */
    private boolean greaterThan(int value1, int value2) {

        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation > 0;

    }

    /**
     * Lower than functionality.
     * 
     * @param value1
     *            the context Value parsed as Integer
     * @param value2
     *            the wanted value parsed as Integer
     * @return true, if context value is lower than the wanted value
     */
    private boolean lowerThan(int value1, int value2) {

        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation < 0;

    }

    /**
     * Same as functionality.
     * 
     * @param value1
     *            the context Value parsed as Integer
     * @param value2
     *            the wanted value parsed as Integer
     * @return true, if context value is lower than the wanted value
     */
    private boolean sameAs(int value1, int value2) {

        int comparation = Integer.valueOf(value1).compareTo(value2);
        return comparation == 0;

    }
}
