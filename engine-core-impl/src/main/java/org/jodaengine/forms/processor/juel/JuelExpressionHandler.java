package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.jodaengine.forms.Form;
import org.jodaengine.forms.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.util.juel.ProcessELContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.RootPropertyResolver;
import de.odysseus.el.util.SimpleResolver;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

/**
 * The Class JUELExpressionHandler. Tries to evaluate all JUEL conditions for the forms, if they exist.
 */
public class JuelExpressionHandler extends AbstractFormFieldHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void setInternally(Form form,
                                 List<FormField> formFields,
                                 ProcessInstanceContext context,
                                 OutputDocument output) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ELContext elContext = new ProcessELContext(context);

        // TODO REVIEW hier gibt es keinerlei Kommentare
        
        Iterator<FormField> it = formFields.iterator();
        
        while (it.hasNext()) {
            FormField field = it.next();
            JodaFormField jodaField = form.getFormField(field.getName());
            
            String readExpression = jodaField.getReadExpression();
            if (readExpression != null) {
                ValueExpression e = factory.createValueExpression(elContext, readExpression, String.class);
                try {
                    String result = (String) e.getValue(elContext);
                    field.setValue(result);

                    // remove the formField from the formFields list, as it has been processed sucessfully.
                    it.remove();
                } catch (PropertyNotFoundException exception) {
                    // requested variable does not exist.
                    // do not change the value of the input field.
                    logger.debug("The expression {} cannot be resolved.", readExpression);
                }
            }
        }

    }

    @Override
    protected void readInternally(Map<String, String> formInput, Form form, ProcessInstanceContext context) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ELContext elContext = new ProcessELContext(context);

        Iterator<Entry<String, String>> it = formInput.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            String expressionToEvaluate = formField.getWriteExpression();

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

        }
        
        // write all set root properties to the context.
        SimpleResolver resolver = (SimpleResolver) elContext.getELResolver();
        RootPropertyResolver rootResolver = resolver.getRootPropertyResolver();
        
        Iterator<String> propertyIterator = rootResolver.properties().iterator();
        while (propertyIterator.hasNext()) {
            String propertyName = propertyIterator.next();
            Object propertyValue = rootResolver.getProperty(propertyName);
            context.setVariable(propertyName, propertyValue);
        }

    }

}
