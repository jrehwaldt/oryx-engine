package org.jodaengine.process.structure;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.jodaengine.process.token.Token;

/**
 * The Interface for conditions.
 * 
 * @author Jannik Streek
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "@classifier")
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
