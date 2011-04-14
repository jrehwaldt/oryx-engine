package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertEquals;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class HumanTaskDeployerTest extends AbstractProcessDeployerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static int NUMBER_OF_PARTICIPANTS = 3;

    /**
     * {@inheritDoc}
     */
    @Override
    @BeforeMethod
    public void setUp()
    throws IllegalStarteventException {

        try {
            this.deployer = new HumanTaskProcessDeployer();
        } catch (SchedulerException e) {
            logger.error("Scheduling error when creating HumanTaskProcessDeplyoer", e);
        }
        this.uuid = deployer.deploy();
    }

    /**
     * Tests that the participants which should be created are really created. This tests breaks if the number of
     * aprticipants in the process deployer is changed.
     */
    @Test
    public void testParticipantsCreated() {

        IdentityService identityService = ServiceFactory.getIdentityService();
        int actualNumberOfParticipants = identityService.getParticipants().size();
        assertEquals(actualNumberOfParticipants, NUMBER_OF_PARTICIPANTS, "Did anybody change the HumanTaskDeployer?");

    }

    /**
     * Stops the deployer so we don't get those pesky Quartz errors.
     */
    @AfterMethod
    public void shutDown() {

        this.deployer.stop();
    }
}
