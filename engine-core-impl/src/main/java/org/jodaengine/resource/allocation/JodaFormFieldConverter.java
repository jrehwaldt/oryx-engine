package org.jodaengine.resource.allocation;

import java.util.Map;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.OutputDocument;

import org.jodaengine.allocation.JodaFormField;

/**
 * The Class JodaFormFieldConverterImpl.
 */
public class JodaFormFieldConverter {

    public JodaFormField convert(Attributes formAttributes) {

        String name = formAttributes.getValue("name");
        
        // remove the attributes from the form field, as we have read them and the are not valid html.
        String readExpression = formAttributes.getValue("joda:readExpression");
        String writeExpression = formAttributes.getValue("joda:writeExpression");
        Class<?> dataClass = String.class;
        JodaFormField formField = new JodaFormFieldImpl(name, readExpression, writeExpression, dataClass);
        
        return formField;
    }
    
    /**
     * Removes all JodaAttributes from the OutputDocument.
     */
    public void cleanJodaAttributes(Attributes attributes, OutputDocument document) {
        Map<String, String> replacements = document.replace(attributes, false);
        replacements.remove("joda:readExpression");
        replacements.remove("joda:writeExpression");
    }

}
