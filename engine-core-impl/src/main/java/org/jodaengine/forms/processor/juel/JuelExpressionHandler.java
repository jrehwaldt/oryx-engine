package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.forms.Form;
import org.jodaengine.forms.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.util.juel.ProcessELContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.RootPropertyResolver;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

/**
 * The Class JUELExpressionHandler. Tries to evaluate all JUEL conditions for the forms, if they exist.
 */
public class JuelExpressionHandler extends AbstractFormFieldHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Iterates over all the form fields and evaluates the supplied output expressions.
     * If successful the determined value is inserted into the HTML and the formField is removed from the formFields
     * list as the list is used by the succeeding handlers.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void generateOutputValuesInternally(Form form,
                                                  List<FormField> formFields,
                                                  ProcessInstanceContext context,
                                                  OutputDocument output) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ProcessELContext elContext = new ProcessELContext(context, false);

        Iterator<FormField> it = formFields.iterator();

        while (it.hasNext()) {
            FormField field = it.next();
            JodaFormField jodaField = form.getFormField(field.getName());

            // only evaluate the outputExpression if it was supplied
            String outputExpression = jodaField.getOutputExpression();
            if (outputExpression != null) {
                ValueExpression e = factory.createValueExpression(elContext, outputExpression, String.class);
                try {
                    String result = (String) e.getValue(elContext);
                    field.setValue(result);

                    // remove the formField from the formFields list, as it has been processed sucessfully.
                    it.remove();
                } catch (PropertyNotFoundException exception) {
                    // requested variable does not exist.
                    // do not change the value of the input field.
                    logger.debug("The expression {} cannot be resolved.", outputExpression);
                }
            }
        }

    }

    /**
     * Identifies the form fields of the supplied formInput and evalutes the inputExpression via JUEL, if it exists.
     * If successful, the map-entry that was looked at is removed from the formInput-Map to prevent it from being
     * executed by following handlers.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void readInputInternally(Map<String, String> formInput, Form form, ProcessInstanceContext context) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ProcessELContext elContext = new ProcessELContext(context, false);

        Iterator<Entry<String, String>> it = formInput.entrySet().iterator();
        while (it.hasNext()) {
            try {
                Entry<String, String> entry = it.next();
                String fieldName = entry.getKey();
                String enteredValue = entry.getValue();

                JodaFormField formField = form.getFormField(fieldName);
                Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
                String expressionToEvaluate = formField.getInputExpression();

                // only set the variable, if it has been specified in the JodaFormField
                if (expressionToEvaluate != null) {
                    ValueExpression e = factory.createValueExpression(elContext, expressionToEvaluate, String.class);
                    try {
                        e.setValue(elContext, objectToSet);

                        // remove the formField from the formFields list, as it has been processed successfully.
                        it.remove();
                    } catch (PropertyNotFoundException exception) {
                        // requested variable does not exist.
                        // do not change the value of the input field.
                        logger.debug("The expression {} cannot be resolved.", expressionToEvaluate);
                    } catch (ELException exception) {
                        logger.debug("The expression {} cannot be set because of: {}.", expressionToEvaluate,
                            exception.getMessage());
                    }

                }
            } catch (JodaEngineException e) {
                logger.error("Cannot set input to context variable");
                logger.error(e.getMessage(), e);
                continue;
            }

        }

        // write all set root properties to the context.
        RootPropertyResolver rootResolver = elContext.getRootPropertyResolver();

        Iterator<String> propertyIterator = rootResolver.properties().iterator();
        while (propertyIterator.hasNext()) {
            String propertyName = propertyIterator.next();
            Object propertyValue = rootResolver.getProperty(propertyName);
            context.setVariable(propertyName, propertyValue);
        }

    }

}
