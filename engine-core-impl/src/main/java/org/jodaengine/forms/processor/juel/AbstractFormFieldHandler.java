package org.jodaengine.forms.processor.juel;

import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.resource.allocation.Form;

/**
 * Realizes a chain of responsibility for form field resolution.
 */
public abstract class AbstractFormFieldHandler {
    protected AbstractFormFieldHandler next;

    /**
     * Sets the next handler in this chain.
     * 
     * @param next
     *            the next
     * @return the abstract form field handler
     */
    public AbstractFormFieldHandler setNext(AbstractFormFieldHandler next) {

        this.next = next;
        return next;
    }

    /**
     * Fills form fields (in the {@link OutputDocument}) with values. If a form field was filled successfully, it should
     * be removed from the formFields, otherwise it might be overriden by later {@link AbstractFormFieldHandler}s.
     * 
     * @param form
     *            the form
     * @param formFields
     *            the form fields
     * @param context
     *            the context
     * @param output
     *            the document to fill the values in
     */
    public void setFormValues(Form form,
                              List<FormField> formFields,
                              ProcessInstanceContext context,
                              OutputDocument output) {

        setInternally(form, formFields, context, output);
        if (next != null) {
            next.setFormValues(form, formFields, context, output);
        }
    }

    /**
     * Processes the formFields according to the strategy that is implemented in the respective realization. For example
     * a handler could take the JUEL readExpression of the {@link JodaFormField} and evaluate it and set the result as
     * the form value.
     * 
     * @param form
     *            the form
     * @param formFields
     *            the form fields
     * @param context
     *            the context
     * @param output
     *            the output
     */
    protected abstract void setInternally(Form form,
                                          List<FormField> formFields,
                                          ProcessInstanceContext context,
                                          OutputDocument output);

    /**
     * Reads the input of a form. A handler can remove the value from the value-Map, if not succeeding handlers should
     * take notice of this input.
     * 
     * @param enteredValues
     *            a map of entered values, mapping the form input id to the entered value
     * @param form
     *            the form
     * @param context
     *            the context
     */
    public void readInput(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {

        readInternally(enteredValues, form, context);
        if (next != null) {
            next.readInput(enteredValues, form, context);
        }
    }

    /**
     * Does the internal processing of the entered Values. Override this to implement a custom behaviour for what to do
     * with entered form values.
     * 
     * @param enteredValues
     *            the entered values
     * @param form
     *            the form
     * @param context
     *            the context
     */
    protected abstract void readInternally(Map<String, String> enteredValues, Form form, ProcessInstanceContext context);
}
