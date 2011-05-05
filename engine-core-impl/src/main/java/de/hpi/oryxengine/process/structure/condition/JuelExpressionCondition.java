package de.hpi.oryxengine.process.structure.condition;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;
import de.hpi.oryxengine.process.structure.Condition;
import de.hpi.oryxengine.process.token.Token;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * This {@link Condition} accepts JuelExpression. It means that this Condition is able to process a juelExpression
 */
public class JuelExpressionCondition implements Condition {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String juelExpression;

    public JuelExpressionCondition(String juelEspression) {

        this.juelExpression = juelEspression;
    }

    @Override
    public boolean evaluate(Token token) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();

        // TODO @Alle: Improve implementation of the JuelEspression; Isn't there a batter way to set the variables into
        // the JuelFactory? Other then extracting all variables from the process context.

        Map<String, Object> variableMap = token.getInstance().getContext().getVariableMap();
        if (variableMap != null) {

            // Binding the variables that are in the processContext with the declared variables in the expression
            for (Entry<String, Object> theEntry : variableMap.entrySet()) {

                String theEntryKey = theEntry.getKey();
                Object theEntryValue = theEntry.getValue();

                ValueExpression valueExpression = factory
                .createValueExpression(theEntryValue, theEntryValue.getClass());
                context.setVariable(theEntryKey, valueExpression);
            }
        }

        ValueExpression e = factory.createValueExpression(context, juelExpression, boolean.class);

        try {

            return (Boolean) e.getValue(context);

        } catch (PropertyNotFoundException propertyNotFoundException) {

            String errorMessage = "The expression '" + juelExpression
                + "'contains a variable that could not be resolved properly. See message: "
                + propertyNotFoundException.getMessage();
            logger.error(errorMessage, propertyNotFoundException);
            throw new DalmatinaRuntimeException(errorMessage, propertyNotFoundException);
        }
    }
}
