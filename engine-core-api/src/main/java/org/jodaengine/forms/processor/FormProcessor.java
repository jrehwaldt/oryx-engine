package org.jodaengine.forms.processor;

import java.util.Map;

import org.jodaengine.allocation.Form;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Interface FormProcessor provides methods to fill a form-string with values from a {@link ProcessInstanceContext}
 * and to read a filled {@link Form} in.
 */
public interface FormProcessor {

    /**
     * Fills a form with context variables.
     * 
     * @param form
     *            the form
     * @param context
     *            the context
     * @return the string
     */
    String prepareForm(Form form, ProcessInstanceContext context);

    /**
     * Reads the values of a form(provided as a {@link Map} of entries) and adds them to the
     * {@link ProcessInstanceContext}.
     * 
     * @param formFields
     *            the form fields
     * @param form
     *            the form
     * @param context
     *            the context
     */
    void readFilledForm(Map<String, String> formFields, Form form, ProcessInstanceContext context);
}
