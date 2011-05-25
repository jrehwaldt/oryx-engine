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

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.jodaengine.allocation.Form;
import org.jodaengine.allocation.JodaFormField;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.structure.condition.ProcessELContext;
import org.jodaengine.resource.allocation.JodaFormFieldImpl;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.odysseus.el.ExpressionFactoryImpl;

/**
 * Tests the Behaviour of the FormProcessors, namely JUEL.
 */
public class FormProcessorTest {

    private static final String FORM_PATH = "src/test/resources/org/jodaengine/forms/processor/";
    private static final String EMPTY_FORM_LOCATION = FORM_PATH + "testForm.html";
    private static final String POPULATED_FORM_LOCATION = FORM_PATH + "populatedTestForm.html";
    private static final String EMPTY_FORM_CONTENT = readFile(EMPTY_FORM_LOCATION);
    private static final String POPULATED_FORM_CONTENT = readFile(POPULATED_FORM_LOCATION);

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
        field1 = new JodaFormFieldImpl("claimPoint1", "#{claimPoint1}", "claimPoint1", String.class);
        field2 = new JodaFormFieldImpl("claimPoint2", "#{claimPoint2}", "claimPoint2", String.class);
        when(form.getFormField(Mockito.matches("claimPoint1"))).thenReturn(field1);
        when(form.getFormField(Mockito.matches("claimPoint2"))).thenReturn(field2);
        
        // this form does not contain the joda-tags, as it mocks the already parsed state
        when(form.getFormContentAsHTML()).thenReturn(EMPTY_FORM_CONTENT);
    }

    /**
     * Sets context varaibles and checks, if the resulting form is filled.
     */
    @Test
    public void testFormProcessing() {

        context.setVariable("claimPoint1", "Point 1");
        context.setVariable("claimPoint2", "Point 2");
        FormProcessor processor = new JuelFormProcessor();
        String resultHtml = processor.prepareForm(form, context);
        Assert.assertEquals(resultHtml, POPULATED_FORM_CONTENT, "The form should be filled with the correct values");
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
        processor.readFilledForm(formInput, form, context);
        Assert.assertEquals(context.getVariable("claimPoint1"), "Point 1", "The variable should be set");
        Assert.assertEquals(context.getVariable("claimPoint2"), "Point 2", "The variable should be set");
    }
    
//    @Test
//    public void simpleJuelTest() {
//        context = new ProcessInstanceContextImpl();
//        context.setVariable("asd", "asd");
//        ExpressionFactory factory = new ExpressionFactoryImpl();
//        ELContext elContext = new ProcessELContext(context);
//        ValueExpression e = factory.createValueExpression(elContext, "${asd}", String.class);
//
//        e.setValue(elContext, "asd");
//        System.out.println(context.getVariable("asd"));
//    }
    
    /**
     * Tests that the form input is converted to the correct types.
     */
    @Test
    public void testFormTypeInput() {

        field1 = new JodaFormFieldImpl("claimPoint1", "", "claimPoint1", Integer.class);
        when(form.getFormField(Mockito.matches("claimPoint1"))).thenReturn(field1);
        Map<String, String> formInput = new HashMap<String, String>();
        formInput.put("claimPoint1", "1");
        FormProcessor processor = new JuelFormProcessor();
        processor.readFilledForm(formInput, form, context);
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
