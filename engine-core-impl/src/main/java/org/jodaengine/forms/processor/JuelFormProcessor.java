package org.jodaengine.forms.processor;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.condition.ProcessELContext;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.RootPropertyResolver;
import de.odysseus.el.util.SimpleResolver;

/**
 * This class uses JUEL to fill in a form and pass results.
 */
public class JuelFormProcessor implements FormProcessor {

    @Override
    public String prepareForm(Form form, ProcessInstanceContext context) {

        String formHtml = form.getFormContentAsHTML();

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ELContext elContext = new ProcessELContext(context);

        ValueExpression e = factory.createValueExpression(elContext, formHtml, String.class);

        return (String) e.getValue(elContext);

    }

    @Override
    public void readFilledForm(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ELContext elContext = new ProcessELContext(context);

        for (Entry<String, String> entry : enteredValues.entrySet()) {
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            ValueExpression e = factory.createValueExpression(elContext, formField.getExpression(), String.class);

            e.setValue(elContext, objectToSet);
        }

        // set all root properties that were created in the instance context.
        SimpleResolver resolver = (SimpleResolver) elContext.getELResolver();
        RootPropertyResolver rootResolver = resolver.getRootPropertyResolver();
        for (String property : rootResolver.properties()) {
            context.setVariable(property, rootResolver.getProperty(property));
        }
    }

    // TODO make this cooler, e.g. chain of responsibility
    private Object convertStringInput(String value, Class<?> clazzToConvertTo) {

        Object object = null;
        if (clazzToConvertTo == String.class) {
            return value;
        } else if (clazzToConvertTo == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazzToConvertTo == Boolean.class) {
            return Boolean.valueOf(value);
        }
        return object;
    }

}
