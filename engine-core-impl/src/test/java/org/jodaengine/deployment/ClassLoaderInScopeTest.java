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

    private RepositoryService repository;
    private DeploymentBuilder builder;
    private ProcessDefinitionBuilder defBuilder;
    private ProcessDefinition definition;

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

    @Test
    public void testClassDeployment()
    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
    IOException {

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
            System.out.println(returnValue);
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
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    @Test
    public void testCustomClassReferential()
    throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
    IllegalArgumentException, InvocationTargetException {

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
        System.out.println(returnValue);
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
        
        Class<?> clazz;
        try {
            clazz = repository.getDeployedClass(definition.getID(), "test.reference.Dummy");
            Assert.fail("The class should not have been found.");
        } catch (ClassNotFoundException e) {
            
        }
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
