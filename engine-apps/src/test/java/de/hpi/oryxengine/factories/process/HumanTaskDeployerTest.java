package de.hpi.oryxengine.factories.process;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.exception.IllegalStarteventException;

/**
 * Tests the EcampleProcessDeplyoer class.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class HumanTaskDeployerTest extends AbstractProcessDeployerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Sets the up.
     * If something gos wrong here something in the Process deplyoer is REALLY off.
     * @throws IllegalStarteventException 
     */
    @BeforeMethod
    public void setUp() throws IllegalStarteventException {

        try {
            this.deployer = new HumanTaskProcessDeployer();
        } catch (SchedulerException e) {
            logger.error("Scheduling error when creating HumanTaskProcessDeplyoer", e);
        }
        this.uuid = deployer.deploy();
    }
}
