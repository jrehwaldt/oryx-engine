package org.jodaengine.forms.processor;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.condition.ProcessELContext;
import org.jodaengine.resource.allocation.Form;
import org.jodaengine.resource.allocation.JodaFormField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.odysseus.el.ExpressionFactoryImpl;

/**
 * This class uses JUEL to fill in a form and pass results.
 */
public class JuelFormProcessor implements FormProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String prepareForm(Form form, ProcessInstanceContext context) {

        Config.CurrentCompatibilityMode.setFormFieldNameCaseInsensitive(false);

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ELContext elContext = new ProcessELContext(context);

        String formContent = form.getFormContentAsHTML();
        Source source = new Source(formContent);
        FormFields formFields = source.getFormFields();
        OutputDocument document = new OutputDocument(source);

        for (FormField field : formFields) {
            StartTag tag = field.getFormControl().getFirstStartTag();
            Attributes attributes = tag.getAttributes();

            // get the corresponding jodaField
            JodaFormField jodaField = form.getFormField(attributes.getValue("name"));

            // replace the value attribute with the result of the readExpression (if it exists)
            String readExpression = jodaField.getReadExpression();
            if (readExpression != null) {
                ValueExpression e = factory.createValueExpression(elContext, readExpression, String.class);
                try {
                    String result = (String) e.getValue(elContext);
                    Map<String, String> replacements = document.replace(attributes, false);

                    replacements.put("value", result);
                } catch (PropertyNotFoundException exception) {
                    // requested variable does not exist.
                    // do not change the value of the input field.
                    logger
                    .debug("The expression {} cannot be resolved. Using the specified default value.", readExpression);
                }

            }

        }

        return document.toString();

    }

    @Override
    public void readFilledForm(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {

        for (Entry<String, String> entry : enteredValues.entrySet()) {
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            String variableToSet = formField.getWriteVariable();
            context.setVariable(variableToSet, objectToSet);
        }
    }

    // TODO @Thorben-Refactoring make this cooler, e.g. chain of responsibility
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
