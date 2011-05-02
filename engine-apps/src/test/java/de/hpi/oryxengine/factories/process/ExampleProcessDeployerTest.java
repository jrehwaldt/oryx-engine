package de.hpi.oryxengine.factories.process;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;

/**
 * Tests the EcampleProcessDeplyoer class.
 * {@inheritDoc}
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ExampleProcessDeployerTest extends AbstractProcessDeployerTest {


    /**
     * {@inheritDoc}
     * @throws ResourceNotAvailableException 
     */
    @BeforeMethod
    @Override
    public void setUp() throws IllegalStarteventException, ResourceNotAvailableException {

        this.deployer = new ExampleProcessDeployer();
        this.uuid = deployer.deploy();
    }
}
