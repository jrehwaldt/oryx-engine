package org.jodaengine.forms.processor.juel;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.StartTag;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.resource.allocation.Form;
import org.jodaengine.resource.allocation.JodaFormField;

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

        
    }

}
