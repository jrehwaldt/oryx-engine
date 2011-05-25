package org.jodaengine.forms.processor;

import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.structure.condition.ProcessELContext;
import org.jodaengine.resource.allocation.JodaFormFieldConverter;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.RootPropertyResolver;
import de.odysseus.el.util.SimpleResolver;

/**
 * This class uses JUEL to fill in a form and pass results.
 */
public class JuelFormProcessor implements FormProcessor {

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
            
            // replace the value attribute with the result of the writeExpression (if it exists)
            String readExpression = jodaField.getReadExpression();           
            if (readExpression != null) {
                ValueExpression e = factory.createValueExpression(elContext, readExpression, String.class);
                String result = (String) e.getValue(elContext);
                
                Map<String, String> replacements = document.replace(attributes, false);
                
                replacements.put("value", result);
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
