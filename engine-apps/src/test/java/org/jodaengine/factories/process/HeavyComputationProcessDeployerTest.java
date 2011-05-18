package org.jodaengine.factories.process;

import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;
import org.testng.annotations.BeforeMethod;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class HeavyComputationProcessDeployerTest extends AbstractProcessDeployerTest {

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotAvailableException
     */
    @Override
    @BeforeMethod
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException {

        this.deployer = new HeavyComputationProcessDeployer();
        this.id = deployer.deploy(engineServices);
    }
}
