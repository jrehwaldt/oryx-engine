package org.jodaengine.rest.api;

import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jodaengine.ServiceFactory;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.factory.resource.ParticipantFactory;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.forms.AbstractForm;
import org.jodaengine.forms.FormImpl;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.Token;
import org.jodaengine.process.token.TokenBuilder;
import org.jodaengine.process.token.builder.BpmnTokenBuilder;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.allocation.CreationPattern;
import org.jodaengine.resource.allocation.PushPattern;
import org.jodaengine.resource.allocation.pattern.push.AllocateSinglePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.util.io.StringStreamSource;
import org.jodaengine.util.mock.MockUtils;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class WorklistWebserviceFormPopulationTest.
 */
public class WorklistWebserviceFormPopulationTest extends AbstractJsonServerTest {

    private CreationPattern pattern;
    private AbstractParticipant jannik = null;
    private AbstractProcessInstance instance = null;

    private static final String FORM_LOCATION = "src/test/resources/testforms/testForm.html";

    private static final String POPULATED_FORM_LOCATION = "src/test/resources/testforms/populatedTestForm.html";

    private String formContent = "";
    private String populatedForm = "";

    /**
     * Setup of a worklist item and a participant.
     * 
     * @throws IOException
     *             one of the form input files might not have been read correctly.
     */
    @BeforeMethod
    public void beforeMethod()
    throws IOException {

        JodaEngine.start();
        jannik = ParticipantFactory.createJannik();
        pattern = CreationPatternFactory.createDirectDistributionPattern(jannik);

        formContent = readFile(FORM_LOCATION);
        populatedForm = readFile(POPULATED_FORM_LOCATION);

        // deploy the artifact together with a definition
        ProcessDefinition definition = MockUtils.mockProcessDefinition();
        AbstractForm form = new FormImpl("form", new StringStreamSource(formContent));
        DeploymentBuilder builder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
        Deployment deployment = builder.addProcessDefinition(definition).addForm(form)
        .buildDeployment();
        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);

        Whitebox.setInternalState(pattern, "formID", "form");
        Navigator nav = mock(Navigator.class);
        TokenBuilder tokenBuilder = new BpmnTokenBuilder(nav, null);
        instance = new ProcessInstance(definition, tokenBuilder);
        Token token = instance.createToken(mock(Node.class));
//        ServiceFactory.getTaskDistribution().distribute(pattern, token);
        AbstractWorklistItem item = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeItem(ServiceFactory.getInteralWorklistService(), item);
    }

    /**
     * We create the two context variables as specified in the form and then check,
     * whether the values appear in the returned form.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     */
    @Test
    public void testFormPopulationWithContext()
    throws URISyntaxException {

        // set the values that should appear in the form
        instance.getContext().setVariable("claimPoint1", "Point 1");
        instance.getContext().setVariable("claimPoint2", "Point 2");

        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        String json = makeGETRequestReturningJson("/worklist/items/" + item.getID() + "/form?participantId="
            + jannik.getID());
        Assert.assertEquals(json, populatedForm, "the form should be correctly populated.");
    }

    /**
     * Test that the files have been read.
     */
    @Test
    public void testFileIn() {

        Assert.assertFalse("".equals(populatedForm),
            "The populatedForm is empty, maybe file locations and file reading code");
        Assert.assertFalse("".equals(formContent), "The form is empty, maybe file locations and file reading code");
    }

    @Override
    protected Object getResourceSingleton() {

        return new WorklistWebService(jodaEngineServices);
    }

    /**
     * Reads a file and returns its content as a String.
     * 
     * @param fileName
     *            the file name
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private String readFile(String fileName)
    throws IOException {

        String fileContent = "";
        File file = new File(fileName);
        FileReader input = new FileReader(file);
        BufferedReader reader = new BufferedReader(input);

        String nextLine = reader.readLine();
        while (nextLine != null) {
            fileContent = fileContent.concat(nextLine);
            nextLine = reader.readLine();
        }

        reader.close();
        input.close();

        return fileContent;
    }
}
