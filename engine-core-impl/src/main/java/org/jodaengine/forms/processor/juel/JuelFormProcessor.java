package org.jodaengine.forms.processor.juel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.forms.processor.FormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * This class uses JUEL to fill in a form and pass results.
 * Variables are set and read hierarchically.
 * See method comments.
 */
public class JuelFormProcessor implements FormProcessor {

    private AbstractFormFieldHandler firstHandler;

    /**
     * Instantiates a new juel form processor using a chain of responsibility to realize the hierachy as described in
     * the interface {@link FormProcessor}.
     */
    public JuelFormProcessor() {

        firstHandler = new JuelExpressionHandler();
        firstHandler.setNext(new ContextVariableHandler());
    }

    @Override
    public String prepareForm(Form form, ProcessInstanceContext context) {

        Config.CurrentCompatibilityMode.setFormFieldNameCaseInsensitive(false);
        String formContent = form.getFormContentAsHTML();
        Source source = new Source(formContent);
        FormFields formFields = source.getFormFields();
        OutputDocument document = new OutputDocument(source);

        firstHandler.setFormValues(form, new ArrayList<FormField>(formFields), context, document);

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
