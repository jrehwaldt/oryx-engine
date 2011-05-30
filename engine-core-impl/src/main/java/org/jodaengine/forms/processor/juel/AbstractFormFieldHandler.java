package org.jodaengine.forms.processor.juel;

import java.util.List;
import java.util.Map;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.forms.Form;
import org.jodaengine.process.instance.ProcessInstanceContext;

import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.OutputDocument;

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
    public void generateOutputValues(Form form,
                              List<FormField> formFields,
                              ProcessInstanceContext context,
                              OutputDocument output) {

        generateOutputValuesInternally(form, formFields, context, output);
        if (next != null) {
            next.generateOutputValues(form, formFields, context, output);
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
    protected abstract void generateOutputValuesInternally(Form form,
                                          List<FormField> formFields,
                                          ProcessInstanceContext context,
                                          OutputDocument output);

    /**
     * Reads the input of a form. A handler can remove the value from the value-Map, if not succeeding handlers should
     * take notice of this input.
     * 
     * @param formInput
     *            mapping the form input id to the entered value
     * @param form
     *            the form
     * @param context
     *            the context
     */
    public void readInput(Map<String, String> formInput, Form form, ProcessInstanceContext context) {

        readInputInternally(formInput, form, context);
        if (next != null) {
            next.readInput(formInput, form, context);
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
    protected abstract void readInputInternally(Map<String, String> enteredValues,
                                           Form form,
                                           ProcessInstanceContext context);

    // TODO @Thorben-Refactoring make this cooler, e.g. chain of responsibility
    /**
     * Converts string input to an object of the class as specified in the {@link JodaFormField}.
     *
     * @param value the value
     * @param classToConvertTo the clazz to convert to
     * @return the object
     * @throws JodaEngineException thrown if the supplied String value could not be converted to the desired class.
     */
    protected Object convertStringInput(String value, Class<?> classToConvertTo) throws JodaEngineException {
        
        // TODO REVIEW Wo findet die Fehlerbehandlung statt?
        //      Beim Fehler wird die Kette nicht weiterverfolgt und bricht ab (wird bei readInput(...) durchgereicht.)
        
        try {

            Object object = null;
            if (classToConvertTo == String.class) {
                return value;
            } else if (classToConvertTo == Integer.class) {
                return Integer.valueOf(value);
            // TODO REVIEW Test für Boolean?
            } else if (classToConvertTo == Boolean.class) {
                return Boolean.valueOf(value);
            }
            return object;
        } catch (Exception e) {
            throw new JodaEngineException("Could not convert '" + value + "' to class " + classToConvertTo, e);
        }
    }
}
