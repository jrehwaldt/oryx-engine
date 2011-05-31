package org.jodaengine.forms.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jodaengine.forms.Form;
import org.jodaengine.forms.JodaFormField;
import org.jodaengine.forms.JodaFormFieldArguments;
import org.jodaengine.forms.JodaFormFieldImpl;
import org.jodaengine.forms.processor.juel.JuelFormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the Behaviour of the FormProcessors, namely JUEL.
 */
public class FormProcessorTest {

    private static final String FORM_PATH = "src/test/resources/org/jodaengine/forms/processor/";

    private static final String EMPTY_FORM_LOCATION = FORM_PATH + "testForm.html";
    private final String unpopulatedFormContent = readFile(EMPTY_FORM_LOCATION);

    private static final String POPULATED_FORM_LOCATION = FORM_PATH + "populatedTestForm.html";
    private final String populatedFormContent = readFile(POPULATED_FORM_LOCATION);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Form form;
    private ProcessInstanceContext context;
    private JodaFormField field1, field2;

    /**
     * Creates a form mock.
     */
    @BeforeMethod
    public void setUp() {

        context = new ProcessInstanceContextImpl();

        form = mock(Form.class);
        field1 = new JodaFormFieldImpl("claimPoint1", createAttributesArguments("claimPoint1"), String.class);
        field2 = new JodaFormFieldImpl("claimPoint2", createAttributesArguments("claimPoint2"), String.class);
        when(form.getFormField(Mockito.matches("claimPoint1"))).thenReturn(field1);
        when(form.getFormField(Mockito.matches("claimPoint2"))).thenReturn(field2);

        // this form does not contain the joda:*-tags for variable and expression specification, as it mocks the already
        // parsed state
        when(form.getFormContentAsHTML()).thenReturn(unpopulatedFormContent);
    }

    /**
     * Creates a {@link JodaFormFieldArguments} object that uses the variableName as input and output variable and
     * '#{variableName}' as input and output expression.
     * 
     * @param variableName
     *            the variable name
     * @return the joda form field arguments
     */
    private JodaFormFieldArguments createAttributesArguments(String variableName) {

        String variableNameExpression = "#{" + variableName + "}";

        JodaFormFieldArguments attributes = new JodaFormFieldArguments();
        attributes.setInputVariable(variableName);
        attributes.setInputExpression(variableNameExpression);
        attributes.setOutputVariable(variableName);
        attributes.setOutputExpression(variableNameExpression);

        return attributes;
    }

    /**
     * Sets context variables and checks, whether the resulting form is filled.
     */
    @Test
    public void testFormProcessing() {

        context.setVariable("claimPoint1", "Point 1");
        context.setVariable("claimPoint2", "Point 2");
        FormProcessor processor = new JuelFormProcessor();
        String resultHtml = processor.prepareForm(form, context);
        Assert.assertEquals(resultHtml, populatedFormContent, "The form should be filled with the correct values");
    }

    /**
     * Test the reading of input fields.
     */
    @Test
    public void testFormReading() {

        Map<String, String> formInput = new HashMap<String, String>();
        formInput.put("claimPoint1", "Point 1");
        formInput.put("claimPoint2", "Point 2");
        FormProcessor processor = new JuelFormProcessor();

        processor.processFormInput(formInput, form, context);
        Assert.assertEquals(context.getVariable("claimPoint1"), "Point 1", "The variable should be set");
        Assert.assertEquals(context.getVariable("claimPoint2"), "Point 2", "The variable should be set");
    }

    /**
     * Tests that the form input is converted to the correct types.
     */
    @Test
    public void testFormTypeInput() {

        JodaFormFieldArguments attributes = new JodaFormFieldArguments();
        attributes.setInputVariable("claimPoint1");

        field1 = new JodaFormFieldImpl("claimPoint1", attributes, Integer.class);
        when(form.getFormField("claimPoint1")).thenReturn(field1);
        Map<String, String> formInput = new HashMap<String, String>();
        formInput.put("claimPoint1", "1");
        FormProcessor processor = new JuelFormProcessor();
        processor.processFormInput(formInput, form, context);
        Assert.assertEquals(context.getVariable("claimPoint1").getClass(), Integer.class,
            "The variable should be an integer not a String");
    }

    // TODO @Thorben-Refactoring add more complex test
    /**
     * Reads a file and returns its content as a String.
     * 
     * @param fileName
     *            the file name
     * @return the string
     */
    private String readFile(String fileName) {

        String fileContent = null;
        try {
            fileContent = FileUtils.readFileToString(new File(fileName));
            fileContent = fileContent.trim();
        } catch (IOException e) {
            logger.error("Could not find/read the specified test file: {}", fileName);
        }

        return fileContent;
    }
}
