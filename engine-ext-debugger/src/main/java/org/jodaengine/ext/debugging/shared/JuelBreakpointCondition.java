package org.jodaengine.ext.debugging.shared;

import javax.annotation.Nonnull;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jodaengine.ext.debugging.api.BreakpointCondition;
import org.jodaengine.process.token.Token;
import org.jodaengine.util.juel.ProcessELContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.odysseus.el.ExpressionFactoryImpl;

/**
 * This class represents a condition, which may be attached to a {@link BreakpointImpl}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-24
 */
public class JuelBreakpointCondition implements BreakpointCondition {
    private static final long serialVersionUID = 3286057388232773948L;
    
    @JsonIgnore
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @JsonProperty
    private String expression;
    
    /**
     * Default constructor. Creates a {@link BreakpointCondition} using Juel EL
     * and available context data.
     * 
     * @param juelExpression the Juel expression
     */
    @JsonCreator
    public JuelBreakpointCondition(@Nonnull @JsonProperty("expression") String juelExpression) {
        this.expression = juelExpression;
    }
    
    @Override
    public boolean evaluate(Token token) {
        
        ELContext context = new ProcessELContext(token.getInstance().getContext());
        
        ExpressionFactory factory = new ExpressionFactoryImpl();
        
        try {
            ValueExpression e = factory.createValueExpression(context, expression, boolean.class);
            return ((Boolean) e.getValue(context)).booleanValue();
        } catch (Exception e) {
            logger.warn("The condition '" + expression + "' contains an expression that could not be evaluated.", e);
            return true;
        }
    }
}
