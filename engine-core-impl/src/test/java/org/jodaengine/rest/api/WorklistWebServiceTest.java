package org.jodaengine.rest.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jboss.resteasy.mock.MockHttpResponse;
import org.jodaengine.ServiceFactory;
import org.jodaengine.allocation.AbstractForm;
import org.jodaengine.allocation.PushPattern;
import org.jodaengine.deployment.Deployment;
import org.jodaengine.deployment.DeploymentBuilder;
import org.jodaengine.factory.worklist.CreationPatternFactory;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.jodaengine.process.instance.ProcessInstanceContextImpl;
import org.jodaengine.process.instance.ProcessInstanceImpl;
import org.jodaengine.process.token.TokenImpl;
import org.jodaengine.resource.AbstractParticipant;
import org.jodaengine.resource.allocation.FormImpl;
import org.jodaengine.resource.allocation.pattern.AllocateSinglePattern;
import org.jodaengine.resource.allocation.pattern.ConcreteResourcePattern;
import org.jodaengine.resource.worklist.AbstractWorklistItem;
import org.jodaengine.util.io.FileStreamSource;
import org.jodaengine.util.io.IoUtil;
import org.jodaengine.util.io.StringStreamSource;
import org.jodaengine.util.mock.MockUtils;
import org.jodaengine.util.testing.AbstractJsonServerTest;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the interaction with our WorklistWebService.
 */
public class WorklistWebServiceTest extends AbstractJsonServerTest {

    private ConcreteResourcePattern pattern = null;
    private AbstractParticipant jannik = null;
    private ProcessInstanceContext context = null;
    private static final String FORM_PATH = "src/test/resources/org/jodaengine/deployment/importer/form/";
    private static final String FORM_LOCATION = FORM_PATH + "testForm.html";
    private static final String RESOLVED_FORM_LOCATION = FORM_PATH + "testFormResolved.html";

    @Override
    protected Object getResourceSingleton() {

        return new WorklistWebService(jodaEngineServices);
    }

    /**
     * Sets the users and worklists up.
     */
    @BeforeMethod
    public void setUpUsersAndWorklists() {

        // We need to start the engine in order to start the WorklistManager who then gets the identityService
        // JodaEngine.start();
        pattern = CreationPatternFactory.createJannikServesGerardoCreator();

        // deploy definition and artifact
        ProcessDefinition definition = MockUtils.mockProcessDefinition();
        File formFile = new File(FORM_LOCATION);
        AbstractForm form = new FormImpl("form", new FileStreamSource(formFile));
        DeploymentBuilder builder = jodaEngineServices.getRepositoryService().getDeploymentBuilder();
        Deployment deployment = builder.addProcessDefinition(definition).addForm(form).buildDeployment();
        jodaEngineServices.getRepositoryService().deployInNewScope(deployment);

        // Whitebox.setInternalState(pattern, "form", new FormImpl(processArtifact));
        Whitebox.setInternalState(pattern, "formID", "form");

        TokenImpl token = mock(TokenImpl.class);
        context = new ProcessInstanceContextImpl();
        AbstractProcessInstance instance = mock(ProcessInstanceImpl.class);
        when(instance.getContext()).thenReturn(context);
        when(instance.getDefinition()).thenReturn(definition);
        when(token.getInstance()).thenReturn(instance);

        // ServiceFactory.getTaskDistribution().distribute(task, token);

        AbstractWorklistItem item = pattern.createWorklistItem(token, jodaEngineServices.getRepositoryService());
        PushPattern pushPattern = new AllocateSinglePattern();
        pushPattern.distributeWorkitem(ServiceFactory.getWorklistQueue(), item);

        // System.out.println(ServiceFactory.getIdentityService().getParticipants());
        jannik = (AbstractParticipant) pattern.getAssignedResources().iterator().next();
    }

    @AfterMethod
    public void tearDown() {

    }

    /**
     * Test get the worklist with one item in the worklist.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetWorklist()
    throws URISyntaxException, IOException {

        String json = makeGETRequestReturningJson("/worklist/items?id=" + jannik.getID());
        Assert.assertNotSame(json, "[]");

        AbstractWorklistItem[] items = this.mapper.readValue(json, AbstractWorklistItem[].class);

        Assert.assertNotNull(items);
        Assert.assertEquals(items.length, 1);
        // worklist item is a task, tasks don't have an ID so we decide to test it that way
        // compare against the values from the Factory
        Assert.assertEquals(items[0].getSubject(), CreationPatternFactory.SIMPLE_TASK_SUBJECT);
        Assert.assertEquals(items[0].getDescription(), CreationPatternFactory.SIMPLE_TASK_DESCRIPTION);
    }

    /**
     * Get the form for the worklistitem.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetForm()
    throws URISyntaxException, IOException {
        

        context.setVariable("claimPoint1", "hi");
        context.setVariable("claimPoint2", "ho");

        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        String json = makeGETRequestReturningJson("/worklist/items/" + item.getID() + "/form?participantId="
            + jannik.getID());

        Assert.assertEquals(json.trim(), readFile(RESOLVED_FORM_LOCATION).trim());
    }

    /**
     * Get the form for the worklist item with a false item id. An 404 Error should be returned.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetFormWithMissingWorkitem()
    throws URISyntaxException, IOException {

        UUID falseID = UUID.randomUUID();
        MockHttpResponse response = makeGETRequest(String.format("worklist/items/%s/form?participantId=%s", falseID,
            jannik.getID()));

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(),
            "the result should be a status code 404, that means, the request has failed.");
    }

    /**
     * Get the form for the worklist item with a wrong participant id. An 404 Error should be returned.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testGetFormWithMissingParticipantId()
    throws URISyntaxException, IOException {

        UUID falseID = UUID.randomUUID();
        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        MockHttpResponse response = makeGETRequest(String.format("worklist/items/%s/form?participantId=%s",
            item.getID(), falseID));

        Assert.assertEquals(response.getStatus(), HTTP_STATUS_FAIL.getStatusCode(),
            "the result should be a status code 404, that means, the request has failed.");
    }

    /**
     * Posts some example data for a form. The data should then be stored in the process context.
     * 
     * @throws URISyntaxException
     *             the uRI syntax exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void postForm()
    throws URISyntaxException, IOException {

        AbstractWorklistItem item = (AbstractWorklistItem) jannik.getWorklist().getWorklistItems().toArray()[0];
        Map<String, String> content = new HashMap<String, String>();

        // Simulate form data input
        content.put("claimPoint1", "checked");
        content.put("claimPoint2", "yes");

        MockHttpResponse response = makePOSTFormRequest(
            String.format("/worklist/items/%s/form?participantId=%s", item.getID(), jannik.getID()), content);
        Assert.assertEquals(response.getStatus(), HTTP_STATUS_OK.getStatusCode(),
            "the result should be OK, that means, the request should have suceeded.");
        Assert.assertEquals(context.getVariable("claimPoint1"), "checked");
        Assert.assertEquals(context.getVariable("claimPoint2"), "yes");

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
            fileContent = fileContent.concat(nextLine + "\n");
            nextLine = reader.readLine();
        }

        reader.close();
        input.close();

        return fileContent;
    }

}
