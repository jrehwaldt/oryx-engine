package org.jodaengine.factories.process;

import org.testng.annotations.BeforeMethod;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class ExampleProcessDeployerTest extends AbstractProcessDeployerTest {

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotAvailableException
     */
    @BeforeMethod
    @Override
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException {

        this.deployer = new ExampleProcessDeployer();
        this.uuid = deployer.deploy(engineServices);
    }
}
