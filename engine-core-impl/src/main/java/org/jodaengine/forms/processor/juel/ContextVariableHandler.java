package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.StartTag;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;

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
            StartTag tag = field.getFormControl().getFirstStartTag();
            Attributes attributes = tag.getAttributes();
            
            JodaFormField jodaField = form.getFormField(attributes.getValue("name"));
            String variable = jodaField.getReadVariable();
            
            if (variable != null) {
                Object value = context.getVariable(variable);
                if (value != null) {
                    Map<String, String> replacements = output.replace(attributes, false);
                    replacements.put("value", value.toString());
                    
                    it.remove();
                }
            }
            
            
            
        }
        
        
    }

    @Override
    protected void readInternally(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {

        for (Entry<String, String> entry : enteredValues.entrySet()) {
            String fieldName = entry.getKey();
            String enteredValue = entry.getValue();

            JodaFormField formField = form.getFormField(fieldName);
            Object objectToSet = convertStringInput(enteredValue, formField.getDataClazz());
            String variableToSet = formField.getWriteVariable();
            
            // only set the variable, if it has been specified in the JodaFormField
            if (variableToSet != null) {
                context.setVariable(variableToSet, objectToSet);
                enteredValues.remove(fieldName);
            }
            
        }
    }

}
