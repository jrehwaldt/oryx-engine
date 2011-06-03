package org.jodaengine.forms.processor.juel;

import java.util.ArrayList;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.jodaengine.forms.Form;
import org.jodaengine.forms.processor.FormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.util.juel.ProcessELContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.odysseus.el.ExpressionFactoryImpl;

import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.FormField;
import net.htmlparser.jericho.FormFields;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

/**
 * This class uses JUEL to fill in a form and pass results.
 * Variables are set and read hierarchically.
 * See method comments.
 */
public class JuelFormProcessor implements FormProcessor {

    private AbstractFormFieldHandler firstHandler;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Instantiates a new juel form processor using a chain of responsibility to realize the hierarchy as described in
     * the interface {@link FormProcessor}.
     */
    public JuelFormProcessor() {

        firstHandler = new JuelExpressionHandler();
        firstHandler.setNext(new ContextVariableHandler());
    }

    @Override
    public String prepareForm(Form form, ProcessInstanceContext context) {

        Config.CurrentCompatibilityMode.setFormFieldNameCaseInsensitive(false);
        String formContent = form.getFormContentAsHTML();
        Source source = new Source(formContent);
        FormFields formFields = source.getFormFields();
        OutputDocument document = new OutputDocument(source);

        firstHandler.generateOutputValues(form, new ArrayList<FormField>(formFields), context, document);
        document.replace(formFields);

        String preparedFormFieldsString = document.toString();
        return evaluateJuelString(context, preparedFormFieldsString);

    }

    @Override
    public void processFormInput(Map<String, String> enteredValues, Form form, ProcessInstanceContext context) {

        firstHandler.readInput(enteredValues, form, context);
    }

    /**
     * Evaluates the expressionText using the variables from the context. Undefined variables are replaced by "".
     * If some of the contained expressions can still not be resolved (such as "#{object.property}", if object is
     * undefined, the original text is returned.
     * 
     * @param context
     *            the context
     * @param expressionText
     *            a String, that may contain multiple juel expression and literals
     * @return the string
     */
    private String evaluateJuelString(ProcessInstanceContext context, String expressionText) {

        ExpressionFactory factory = new ExpressionFactoryImpl();
        ProcessELContext elContext = new ProcessELContext(context);
        ValueExpression expr = factory.createValueExpression(elContext, expressionText, String.class);
        try {
            String evaluatedText = (String) expr.getValue(elContext);
            return evaluatedText;
        } catch (PropertyNotFoundException e) {
            logger.error("Could not resolve some of the specified properties. Returning the original Text.", e);
            return expressionText;
        }
        
    }

}
