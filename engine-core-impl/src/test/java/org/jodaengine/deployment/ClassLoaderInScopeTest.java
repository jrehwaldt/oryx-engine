package org.jodaengine.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import org.jodaengine.RepositoryService;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.process.definition.ProcessDefinition;
import org.jodaengine.process.definition.ProcessDefinitionBuilder;
import org.jodaengine.process.definition.ProcessDefinitionID;
import org.jodaengine.process.instantiation.StartInstantiationPattern;
import org.jodaengine.util.ReflectionUtil;
import org.jodaengine.util.testing.AbstractJodaEngineTest;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the loading of classes in a {@link DeploymentScope}.
 */
public class ClassLoaderInScopeTest extends AbstractJodaEngineTest {

    private static final String CUSTOM_CLASSES_PATH = "src/test/resources/org/jodaengine/deployment/importer/classes/";

    private RepositoryService repository = null;
    private DeploymentBuilder builder = null;
    private ProcessDefinitionBuilder defBuilder = null;
    private ProcessDefinition definition = null;

    /**
     * Does Setup and creates a deployment ready to deploy.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @BeforeMethod
    public void setUpAndDeployProcess()
    throws IllegalStarteventException {

        repository = jodaEngineServices.getRepositoryService();
        builder = repository.getDeploymentBuilder();
        defBuilder = builder.getProcessDefinitionBuilder();

        ProcessDefinitionID id = new ProcessDefinitionID(UUID.randomUUID());
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));

        definition = defBuilder.buildDefinition();
        Whitebox.setInternalState(definition, "id", id);
        builder.addProcessDefinition(definition);
    }

    /**
     * Tests the deployment of a simple JAva class that has no references to other classes.
     * 
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Test
    public void testClassDeployment()
    throws InstantiationException, IllegalAccessException, IOException, InvocationTargetException {

        // read the file in and convert it to a byte[]
        byte[] data = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/simple/Dummy.class");

        builder.addClass("test.simple.Dummy", data);
        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);
        Class<?> clazz;
        try {
            clazz = repository.getDeployedClass(definition.getID(), "test.simple.Dummy");
            Object instance = clazz.newInstance();
            Method methodToInvoke = ReflectionUtil.getMethodFor(clazz, "getAString");
            String returnValue = (String) methodToInvoke.invoke(instance, new Object[] {});
            Assert.assertEquals(returnValue, "A", "The test method should return the String 'A'");
        } catch (ClassNotFoundException e) {
            Assert.fail("The class was not found, so the test fails.");
        }
    }

    /**
     * GetAString in A.class invokes the method GetBString from B.class.
     * So referenced custom classes have to be loaded.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    public void testCustomClassReferential()
    throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
    InvocationTargetException {

        byte[] dataDummy = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/reference/Dummy.class");
        byte[] dataAnotherDummy = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/reference/AnotherDummy.class");

        // add both classes to the deployment
        builder.addClass("test.reference.Dummy", dataDummy);
        builder.addClass("test.reference.AnotherDummy", dataAnotherDummy);
        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);

        Class<?> clazz;
        clazz = repository.getDeployedClass(definition.getID(), "test.reference.Dummy");
        Object instance = clazz.newInstance();
        Method methodToInvoke = ReflectionUtil.getMethodFor(clazz, "getAString");
        String returnValue = (String) methodToInvoke.invoke(instance, new Object[] {});
        Assert.assertEquals(returnValue, "B",
            "The test method should return the String 'B',as this is, what AnotherDummy#getBString() returns.");
    }

    /**
     * Try to access a class that was not deployed.
     */
    @Test
    public void testUndeployedClassAccess() {

        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);

        try {
            repository.getDeployedClass(definition.getID(), "test.reference.Dummy");
            Assert.fail("The class should not have been found.");
        } catch (ClassNotFoundException e) {
            // do nothing here, this is what we expect
        }
    }

    /**
     * Test that a deployed class is only visible in this scope and not in the scope of another deployed definition.
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     */
    @Test
    public void testClassIsolation()
    throws IOException, IllegalStarteventException {

        byte[] data = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/simple/Dummy.class");

        // deploy it once
        builder.addClass("test.simple.Dummy", data);
        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);

        // create another process definition to deploy
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        ProcessDefinition anotherDefinition = defBuilder.buildDefinition();

        builder.addProcessDefinition(anotherDefinition);
        Deployment anotherDeployment = builder.buildDeployment();
        repository.deployInNewScope(anotherDeployment);

        try {
            repository.getDeployedClass(anotherDefinition.getID(), "test.simple.Dummy");
            Assert.fail("The class should not have been found, as it only exists in the other scope.");
        } catch (ClassNotFoundException e) {
            // do nothing here, this is what we expect
        }
    }

    /**
     * Tests the deployment of the same class (name 'test.simple.Dummy') with two versions into two different scopes.
     * Version A#getAString returns "A", Version B#getAString returns "B".
     * 
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException 
     */
    @Test
    public void testTwoClassVersions()
    throws IOException, IllegalStarteventException, ClassNotFoundException, InstantiationException,
    IllegalAccessException, InvocationTargetException {

        byte[] dataA = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/simple/DummyA.class");
        byte[] dataB = getClassDataFromFile(CUSTOM_CLASSES_PATH + "test/simple/DummyB.class");

        // create the deployment
        builder.addClass("test.simple.Dummy", dataA);
        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);

        // create the second deployment
        defBuilder.addStartInstantiationPattern(Mockito.mock(StartInstantiationPattern.class));
        ProcessDefinition anotherDefinition = defBuilder.buildDefinition();
        builder.addProcessDefinition(anotherDefinition);
        builder.addClass("test.simple.Dummy", dataB);
        Deployment anotherDeployment = builder.buildDeployment();
        repository.deployInNewScope(anotherDeployment);

        Class<?> dummyA = repository.getDeployedClass(definition.getID(), "test.simple.Dummy");
        Class<?> dummyB = repository.getDeployedClass(anotherDefinition.getID(), "test.simple.Dummy");

        // call the method on dummyA
        Object instanceA = dummyA.newInstance();
        Method methodToInvokeA = ReflectionUtil.getMethodFor(dummyA, "getAString");
        String returnValueA = (String) methodToInvokeA.invoke(instanceA, new Object[] {});

        // call the method on dummyB
        Object instanceB = dummyB.newInstance();
        Method methodToInvokeB = ReflectionUtil.getMethodFor(dummyB, "getAString");
        String returnValueB = (String) methodToInvokeB.invoke(instanceB, new Object[] {});

        Assert.assertEquals(returnValueA, "A", "getAString on a DummyA-Object should have returned 'A'");
        Assert.assertEquals(returnValueB, "B", "getAString on a DummyA-Object should have returned 'B'");
    }

    /**
     * Reads data from a file and produces a byte[].
     * 
     * @param filePath
     *            the file path
     * @return the class data from file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private byte[] getClassDataFromFile(String filePath)
    throws IOException {

        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        inputStream.read(data);
        inputStream.close();
        return data;
    }
}
