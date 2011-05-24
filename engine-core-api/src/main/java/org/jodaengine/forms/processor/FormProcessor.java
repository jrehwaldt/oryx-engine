package org.jodaengine.forms.processor;

import java.util.Map;

import org.jodaengine.allocation.Form;
import org.jodaengine.process.instance.ProcessInstanceContext;

/**
 * The Interface FormProcessor provides methods to fill a form-string with values from a {@link ProcessInstanceContext}
 * and to read a filled {@link Form} in.
 */
public interface FormProcessor {
    String prepareForm(Form form, ProcessInstanceContext context);
    
    void readFilledForm(Map<String, String> formFields, Form form, ProcessInstanceContext context);
}
