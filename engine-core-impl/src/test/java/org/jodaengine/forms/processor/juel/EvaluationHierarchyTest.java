package org.jodaengine.forms.processor.juel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jodaengine.forms.Form;
import org.jodaengine.forms.JodaFormField;
import org.jodaengine.forms.JodaFormFieldArguments;
import org.jodaengine.forms.JodaFormFieldImpl;
import org.jodaengine.forms.processor.FormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the hierarchy of evaluation of reading/writing variables from/to fields in a form as specified in the
 * {@link FormProcessor} documentation.
 */
public class EvaluationHierarchyTest {

    private static final String FORM_PATH = "src/test/resources/org/jodaengine/forms/processor/";
    private static final String SINGLE_FIELD_FORM_LOCATION = FORM_PATH + "singleFieldForm.html";
    private static final String SINGLE_FIELD_FORM = readFile(SINGLE_FIELD_FORM_LOCATION);

    private Form form;
    private ProcessInstanceContext context;
    private JodaFormField field1;

    /**
     * Creates a context, a mock form.
     */
    @BeforeMethod
    public void setUp() {

        context = new ProcessInstanceContextImpl();

        form = mock(Form.class);

        // this form does not contain the joda-tags, as it mocks the already parsed state
        when(form.getFormContentAsHTML()).thenReturn(SINGLE_FIELD_FORM);

        JodaFormFieldArguments attributes = new JodaFormFieldArguments();
        attributes.setInputVariable("var1");
        attributes.setInputExpression("#{var2}");
        attributes.setOutputVariable("var1");
        attributes.setOutputExpression("#{var2}");
        field1 = new JodaFormFieldImpl("field1", attributes, String.class);

        when(form.getFormField("field1")).thenReturn(field1);
    }

    /**
     * Tests that, if an expression AND a variable is supplied (and the expression evaluates correctly), the expression
     * takes effect and not the variable setting.
     */
    @Test
    public void readExpressionOverridesVariable() {

        context.setVariable("var1", "Variable1");
        context.setVariable("var2", "Variable2");

        FormProcessor processor = new JuelFormProcessor();
        String resultHtml = processor.prepareForm(form, context);
        Assert.assertTrue(resultHtml.contains("Variable2"), "The form should be filled with the expression result.");
        Assert.assertFalse(resultHtml.contains("Variable1"), "The form should not be filled with the readVariable.");
    }

    /**
     * Tests, that the readVariable is filled in, if the readExpression cannot be resolved.
     */
    @Test
    public void variableOverridesExpressionOnFail() {

        context.setVariable("var1", "Variable1");

        FormProcessor processor = new JuelFormProcessor();
        String resultHtml = processor.prepareForm(form, context);
        Assert.assertTrue(resultHtml.contains("Variable1"),
            "The form should be filled with the value of var1, as the readExpression could not be resolved.");
    }

    /**
     * Tests that the writeVariable of a form field is used, if the evaluation of the writeExpression fails.
     */
    @Test
    public void writeVariableOverridesExpressionOnFail() {

        FormProcessor processor = new JuelFormProcessor();

        // this should fail, as JUEL is not able to assign a value to a literal, that #{var2} will be resolved to
        context.setVariable("var2", "var 2");
        Map<String, String> formFields = new HashMap<String, String>();
        formFields.put("field1", "a value");
        processor.processFormInput(formFields, form, context);
        Assert.assertEquals(context.getVariable("var1"), "a value");
    }

    /**
     * Tests that a writeExpression, that creates a new JUEL RootProperty (e.g. #{var2}, if var2 does not exist in the
     * instance context) also writes it result back to the context. The input is set for "field1", as this form field is
     * the on the joda-attributes from the BeforeMethod-Part are associated to.
     */
    @Test
    public void testRootPropertyWriting() {

        FormProcessor processor = new JuelFormProcessor();

        Map<String, String> formFields = new HashMap<String, String>();
        formFields.put("field1", "a value");
        processor.processFormInput(formFields, form, context);
        Assert.assertEquals(context.getVariable("var2"), "a value");
    }

    /**
     * Tests that the writeExpression is used, if it can be resolved.
     */
    @Test
    public void writeExpressionOverridesVariable() {

        // create the context object
        DummyPOJO dummy = new DummyPOJO();
        dummy.setValue("a value");
        context.setVariable("dummy", dummy);
        context.setVariable("var1", "var 1");

        JodaFormFieldArguments attributes = new JodaFormFieldArguments();
        attributes.setInputVariable("var1");
        attributes.setInputExpression("#{dummy.value}");
        field1 = new JodaFormFieldImpl("field1", attributes, String.class);

        when(form.getFormField(Mockito.matches("field1"))).thenReturn(field1);

        FormProcessor processor = new JuelFormProcessor();
        Map<String, String> formFields = new HashMap<String, String>();
        formFields.put("field1", "a value");
        processor.processFormInput(formFields, form, context);

        Assert.assertEquals(context.getVariable("var1"), "var 1", "The value of var1 should not have changed.");
        Assert.assertEquals(dummy.getValue(), "a value",
            "The POJO value should have changed, as it has been evaluated in the expression.");
    }

    // TODO write test for writeExpression auf nicht initialisierte variable (null pointer exception müsste
    // wahrscheinlich abgefangen werden.

    /**
     * Reads a file and returns its content as a String.
     * 
     * @param fileName
     *            the file name
     * @return the string
     */
    private static String readFile(String fileName) {

        String fileContent = "";
        File file = new File(fileName);
        FileReader input;
        try {
            input = new FileReader(file);
            BufferedReader reader = new BufferedReader(input);

            String nextLine = reader.readLine();
            while (nextLine != null) {
                fileContent = fileContent.concat(nextLine);
                nextLine = reader.readLine();
            }

            reader.close();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileContent = fileContent.trim();
        return fileContent;
    }
}
