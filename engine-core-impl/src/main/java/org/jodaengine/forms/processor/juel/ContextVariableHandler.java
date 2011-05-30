package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jodaengine.forms.Form;
import org.jodaengine.forms.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

/**
 * Reads and writes formField-Data according to variable specified in the {@link JodaFormField}.
 */
public class ContextVariableHandler extends AbstractFormFieldHandler {

    /**
     * Iterates over all the formFields of the form. For every form field, the output variable is determined and if
     * present, the value of the corresponding context variable is inserted into the form HTML.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void setInternally(Form form,
                                 List<FormField> formFields,
                                 ProcessInstanceContext context,
                                 OutputDocument output) {

        Iterator<FormField> it = formFields.iterator();
        while (it.hasNext()) {
            FormField field = it.next();
            JodaFormField jodaField = form.getFormField(field.getName());
            String variable = jodaField.getOutputVariable();

            if (variable != null) {
                Object value = context.getVariable(variable);
                if (value != null) {
                    field.setValue(value.toString());

                    it.remove();
                }
            }

        }

    }

    /**
     * Iterates over all the supplied form input pairs of form-field-name and value. Sets them to the context variables,
     * if an inputVariable exists in the corresponding {@link JodaFormField} object.
     * 
     * {@inheritDoc}
     */
    @Override
    protected void readInternally(Map<String, String> formInput, Form form, ProcessInstanceContext context) {

        Iterator<Entry<String, String>> it = formInput.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            String variableToSet = formField.getInputVariable();

            // only set the variable, if it has been specified in the JodaFormField
            if (variableToSet != null) {
                context.setVariable(variableToSet, objectToSet);
                it.remove();
            }

        }
    }

}
