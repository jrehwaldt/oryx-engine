package org.jodaengine.forms.processor;

import java.util.Map;

import org.jodaengine.forms.Form;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Interface FormProcessor provides methods to fill a form-string with values from a {@link ProcessInstanceContext}
 * and to read a filled {@link Form} in.
 */
public interface FormProcessor {

    /**
     * Fills a form with initial values in a hierarchical way:
     * <ul>
     * <li>Use the outputExpression, if present (e.g. a JUEL-Expression)</li>
     * <li>Use the outputVariable, if present (a concrete context variable)</li>
     * <li>Use the default value attribute from the HTML form</li>
     * </ul>
     *  
     * @param form
     *            the form
     * @param context
     *            the context
     * @return the string
     */
    String prepareForm(Form form, ProcessInstanceContext context);

    /**
     * Reads the values of a form (provided as a {@link Map} of entries) and adds them to the
     * {@link ProcessInstanceContext}.
     * Does this in a hierarchical way:
     * 
     * <ul>
     * <li>Evaluates the inputExpression, if present</li>
     * <li>Sets the inputVariable to the input, if present and if the expression failed</li>
     * <li>drops the input</li>
     * </ul>
     * 
     * @param formFields
     *            the form fields
     * @param form
     *            the form
     * @param context
     *            the context
     */
    void processFormInput(Map<String, String> formFields, Form form, ProcessInstanceContext context);
}
