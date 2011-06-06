package org.jodaengine.deployment;

import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.io.FileUtils;
import org.jodaengine.node.activity.bpmn.AbstractJodaScript;
import org.jodaengine.process.instance.ProcessInstanceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for the {@link CustomClassLoader}.
 * 
 */
public class CustomClassLoaderTest {

    // don't use .class as an ending, as eclipse will index it.
    private static final String CLASS_FILE_LOCATION = "src/test/resources/org/jodaengine/deployment/script/TestScript";
    private static final String FULL_CLASS_NAME = "org.jodaengine.deployment.script.TestScript";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Test the loading of a class that extends {@link AbstractJodaScript}. The challenge for the class loader is to
     * also resolve the AbstractJodaScript-class and ProcessInstanceContext etc.
     * 
     * @throws IOException
     *             - if the test class file does not exist
     * @throws NoSuchMethodException
     *             - if there is not "execute"-Method in the TestScript
     * @throws InvocationTargetException
     *             -
     * @throws IllegalAccessException
     *             -
     */
    @Test
    public void testScriptClassLoading()
    throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        CustomClassLoader loader = new CustomClassLoader(Thread.currentThread().getContextClassLoader());
        File classFile = new File(CLASS_FILE_LOCATION);

        byte[] classData = FileUtils.readFileToByteArray(classFile);
        loader.addLoadableClass(FULL_CLASS_NAME, classData);

        // now try to instantiate the class and execute it.
        try {
            Class<AbstractJodaScript> scriptClass = (Class<AbstractJodaScript>) loader.loadClass(FULL_CLASS_NAME);
            Method executeMethod = scriptClass.getMethod("execute", ProcessInstanceContext.class);
            executeMethod.invoke(null, mock(ProcessInstanceContext.class));
        } catch (ClassNotFoundException e) {
            logger.error("", e);
            Assert.fail("All classes should be found, so no ClassNotFoundException should occur.");
        } catch (NoClassDefFoundError e) {
            logger.error("", e);
            Assert.fail("All classes should be defined.");
        }
    }
}
