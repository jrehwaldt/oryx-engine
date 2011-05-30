package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.resource.allocation.Form;
import org.jodaengine.resource.allocation.JodaFormField;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

/**
 * Reads and writes formField-Data according to variable specified in the {@link JodaFormField}.
 */
public class ContextVariableHandler extends AbstractFormFieldHandler {

    @Override
    protected void setInternally(Form form,
                                 List<FormField> formFields,
                                 ProcessInstanceContext context,
                                 OutputDocument output) {

        Iterator<FormField> it = formFields.iterator();
        while (it.hasNext()) {
            FormField field = it.next();
            JodaFormField jodaField = form.getFormField(field.getName());
            String variable = jodaField.getReadVariable();

            if (variable != null) {
                Object value = context.getVariable(variable);
                if (value != null) {
                    field.setValue(value.toString());
//                    output.replace(field.getFormControl());

                    it.remove();
                }
            }

        }

    }

    @Override
    protected void readInternally(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {
        
        // TODO REVIEW enteredValues enthält auch die Schlüssel?
        Iterator<Entry<String, String>> it = enteredValues.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            String variableToSet = formField.getWriteVariable();

            // only set the variable, if it has been specified in the JodaFormField
            if (variableToSet != null) {
                context.setVariable(variableToSet, objectToSet);
                it.remove();
            }

        }
    }

}
