package org.jodaengine.forms.processor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jodaengine.allocation.JodaFormAttributes;
import org.jodaengine.forms.processor.juel.JuelFormProcessor;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.resource.allocation.Form;
import org.jodaengine.resource.allocation.JodaFormField;
import org.jodaengine.resource.allocation.JodaFormFieldImpl;
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

        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(JodaFormAttributes.READ_VARIABLE, "var1");
        attributes.put(JodaFormAttributes.READ_EXPRESSION, "#{var2}");
        field1 = new JodaFormFieldImpl("field1", attributes, String.class);

        when(form.getFormField(Mockito.matches("field1"))).thenReturn(field1);
    }

    /**
     * Tests that, if an expression AND a variable is supplied (and the expression evaluates correctly), the expression
     * takes effect and not the variable setting.
     */
    @Test
    public void expressionOverridesVariable() {

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
