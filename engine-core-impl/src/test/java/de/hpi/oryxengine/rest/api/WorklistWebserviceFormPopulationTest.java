package de.hpi.oryxengine.rest.api;

import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.bootstrap.OryxEngine;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.process.definition.AbstractProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessArtifact;
import de.hpi.oryxengine.process.definition.ProcessDefinition;
import de.hpi.oryxengine.process.instance.AbstractProcessInstance;
import de.hpi.oryxengine.process.instance.ProcessInstanceImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.allocation.FormImpl;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;
import de.hpi.oryxengine.rest.AbstractJsonServerTest;
import de.hpi.oryxengine.util.io.StringStreamSource;

/**
 * The Class WorklistWebserviceFormPopulationTest.
 */
public class WorklistWebserviceFormPopulationTest extends AbstractJsonServerTest {

    private Task task;
    private AbstractParticipant jannik;
    private AbstractProcessInstance instance;

    private static final String FORM_LOCATION = "src/test/resources/testforms/testForm.html";

    private static final String POPULATED_FORM_LOCATION = "src/test/resources/testforms/populatedTestForm.html";

    private String form = "";
    private String populatedForm = "";

    /**
     * Setup of a worklist item and a participant.
     *
     * @throws IOException One of the form input files might not have been read correctly.
     */
    @BeforeMethod
    public void beforeMethod()
    throws IOException {

        OryxEngine.start();
        task = TaskFactory.createJannikServesGerardoTask();

        form = readFile(FORM_LOCATION);
        populatedForm = readFile(POPULATED_FORM_LOCATION);

        AbstractProcessArtifact processArtifact = new ProcessArtifact("form", new StringStreamSource(form));

        Whitebox.setInternalState(task, "form", new FormImpl(processArtifact));

        instance = new ProcessInstanceImpl(mock(ProcessDefinition.class));
        Token token = instance.createToken(mock(Node.class), mock(Navigator.class));
        ServiceFactory.getTaskDistribution().distribute(task, token);
        jannik = (AbstractParticipant) task.getAssignedResources().toArray()[0];
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
    protected Class<?> getResource() {

        return WorklistWebService.class;
    }

    /**
     * Reads a file and returns its content as a String.
     *
     * @param fileName the file name
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
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
