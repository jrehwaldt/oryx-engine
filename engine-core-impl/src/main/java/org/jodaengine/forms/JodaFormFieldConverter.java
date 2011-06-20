package org.jodaengine.forms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.OutputDocument;



/**
 * The Class JodaFormFieldConverterImpl.
 */
public class JodaFormFieldConverter {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Converts joda:*-Form-Attributes to JodaFormFields. If the supplied class cannot be resolved, {@link String} is
     * used per default.
     * 
     * @param formAttributes
     *            the form attributes
     * @return the joda form field
     */
    public JodaFormField convert(Attributes formAttributes) {

        String name = formAttributes.getValue("name");

        // remove the attributes from the form field, as we have read them and they are no valid html.
        JodaFormFieldArguments jodaArgs = new JodaFormFieldArguments();
        jodaArgs.setOutputVariable(formAttributes.getValue(JodaFormAttributes.OUTPUT_VARIABLE));
        jodaArgs.setOutputExpression(formAttributes.getValue(JodaFormAttributes.OUTPUT_EXPRESSION));
        jodaArgs.setInputVariable(formAttributes.getValue(JodaFormAttributes.INPUT_VARIABLE));
        jodaArgs.setInputExpression(formAttributes.getValue(JodaFormAttributes.INPUT_EXPRESSION));

        String className = formAttributes.getValue(JodaFormAttributes.CLASS_NAME);

        Class<?> dataClass;
        if (className != null) {
            try {
                dataClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.error("Class {} not found", className);
                dataClass = String.class;
            }
        } else {
            dataClass = String.class;
        }
        
        JodaFormField formField = new JodaFormFieldImpl(name, jodaArgs, dataClass);

        return formField;
    }

    /**
     * Removes all JodaAttributes from the OutputDocument.
     *
     * @param attributes the {@link Attributes} object that contains joda:*-attributes and is to be cleaned
     * @param document the document the changes should be applied to
     */
    public void cleanJodaAttributes(Attributes attributes, OutputDocument document) {

        Map<String, String> replacements = document.replace(attributes, false);
        replacements.remove(JodaFormAttributes.OUTPUT_VARIABLE);
        replacements.remove(JodaFormAttributes.INPUT_VARIABLE);
        replacements.remove(JodaFormAttributes.OUTPUT_EXPRESSION);
        replacements.remove(JodaFormAttributes.INPUT_EXPRESSION);
    }

}
