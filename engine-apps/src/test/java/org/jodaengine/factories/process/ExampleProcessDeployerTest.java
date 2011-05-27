package org.jodaengine.factories.process;

import java.lang.reflect.InvocationTargetException;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.testng.annotations.BeforeMethod;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class ExampleProcessDeployerTest extends AbstractProcessDeployerTest {

    /**
     * Execute deployer.
     * 
     * @throws IllegalStarteventException
     *             the illegal startevent exception
     * @throws ResourceNotAvailableException
     *             the resource not available exception
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception {@inheritDoc}
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    @BeforeMethod
    @Override
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException, InstantiationException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {

        this.deployer = new ExampleProcessDeployer();
        this.id = deployer.deploy(engineServices);
    }
}
