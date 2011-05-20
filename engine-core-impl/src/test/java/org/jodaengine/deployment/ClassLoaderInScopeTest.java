package org.jodaengine.deployment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
        File file = new File(CUSTOM_CLASSES_PATH + "test/Dummy.class");        
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        inputStream.read(data);
        inputStream.close();
        
        
        
        builder.addClass("test.Dummy", data);
        Deployment deployment = builder.buildDeployment();
        repository.deployInNewScope(deployment);
        Class<?> clazz;
        try {
            clazz = repository.getDeployedClass(definition.getID(), "test.Dummy");
            Object instance = clazz.newInstance();
            Method methodToInvoke = ReflectionUtil.getMethodFor(clazz, "getAString");
            String returnValue = (String) methodToInvoke.invoke(instance, new Object[] {});
            System.out.println(returnValue);
            Assert.assertEquals(returnValue, "A", "The test method should return the String 'A'");
        } catch (ClassNotFoundException e) {
            Assert.fail("The class was not found, so the test fails.");
        }
        
    }
}
