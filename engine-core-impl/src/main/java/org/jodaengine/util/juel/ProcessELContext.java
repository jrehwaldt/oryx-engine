package org.jodaengine.util.juel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

import org.jodaengine.process.instance.ProcessInstanceContext;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * This is a Juel {@link javax.el.ELContext} implementation, which is able to work
 * with our {@link ProcessInstanceContext}.
 * 
 * @author Jan Rehwaldt
 * @since 20111-05-20
 */
public class ProcessELContext extends SimpleContext {
    
    private ProcessInstanceContext instanceContext;
    private ExpressionFactory expressionFactory;
    
    private Variables variables;
    
    /**
     * Constructs a {@link ProcessELContext} using an {@link ProcessInstanceContext}
     * for variable resolution.
     * 
     * @param instanceContext the instance's context
     */
    public ProcessELContext(@Nonnull ProcessInstanceContext instanceContext) {
        super();
        this.instanceContext = instanceContext;
        this.expressionFactory = new ExpressionFactoryImpl();
        
    }

    @Override
    public VariableMapper getVariableMapper() {
        if (this.variables == null) {
            this.variables = new Variables();
        }
        return this.variables;
    }
    
    @Override
    public ValueExpression setVariable(String variable, ValueExpression expression) {
        return getVariableMapper().setVariable(variable, expression);
    }
    
    /**
     * Our {@link ProcessInstanceContext}-aware {@link VariableMapper}.
     */
    class Variables extends VariableMapper {
        
        /**
         * We need this map so we can
         * <ul>
         * <li>
         *   a) always return the same ValueExpression instance for the same variable
         * </li>
         * <li>
         * and
         *   b) allow overriding variables by setting them explicitly.
         * </li>
         * </ul>
         */
        private Map<String, ValueExpression> map = Collections.emptyMap(); 
        
        @Override
        public ValueExpression resolveVariable(String variable) {
            
            //
            // check the map if we already have a mapping resolved
            //
            if (this.map.containsKey(variable)) {
                return this.map.get(variable);
            }
            
            //
            // check the context and bind the resolved value into our map
            //
            Object contextValue = instanceContext.getVariable(variable);
            
            ValueExpression valueExpression = null;
            if (contextValue != null) {
                valueExpression = expressionFactory.createValueExpression(contextValue, contextValue.getClass());
            }
            setVariable(variable, valueExpression);
            
            return valueExpression;
        }
        
        @Override
        public ValueExpression setVariable(String variable, ValueExpression expression) {
            if (this.map.isEmpty()) {
                this.map = new HashMap<String, ValueExpression>();
            }
            
            return this.map.put(variable, expression);
        }
    }
}
