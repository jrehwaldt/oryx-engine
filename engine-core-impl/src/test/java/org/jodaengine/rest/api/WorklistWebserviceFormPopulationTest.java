package org.jodaengine.rest.api;

import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.definition.AbstractProcessArtifact;
import org.jodaengine.process.definition.ProcessArtifact;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.BpmnProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnTokenImpl;
import org.jodaengine.process.token.SuspendableToken;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
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

    private ConcreteResourcePattern pattern;
    private AbstractParticipant jannik;
    private AbstractProcessInstance<BpmnTokenImpl> instance;

    private static final String FORM_LOCATION = "src/test/resources/testforms/testForm.html";

    private static final String POPULATED_FORM_LOCATION = "src/test/resources/testforms/populatedTestForm.html";

    private String form = "";
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
        pattern = CreationPatternFactory.createJannikServesGerardoCreator();

        form = readFile(FORM_LOCATION);
        populatedForm = readFile(POPULATED_FORM_LOCATION);

        // deploy the artifact together with a definition
        ProcessDefinition definition = MockUtils.mockProcessDefinition();
        AbstractProcessArtifact processArtifact = new ProcessArtifact("form", new StringStreamSource(form));
        DeploymentBuilder builder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
        Deployment deployment = builder.addProcessDefinition(definition).addProcessArtifact(processArtifact)
        .buildDeployment();
        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);

        Whitebox.setInternalState(pattern, "formID", "form");

        instance = new BpmnProcessInstance(definition);
        SuspendableToken token = instance.createNewToken(mock(Node.class), mock(Navigator.class));
//        ServiceFactory.getTaskDistribution().distribute(pattern, token);
        AbstractWorklistItem item = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeWorkitem(ServiceFactory.getWorklistQueue(), item);
        jannik = (AbstractParticipant) pattern.getAssignedResources().iterator().next();
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
        Assert.assertFalse("".equals(form), "The form is empty, maybe file locations and file reading code");
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
