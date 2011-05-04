package de.hpi.oryxengine.factories.process;

import static org.testng.Assert.assertEquals;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.exception.ResourceNotAvailableException;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class HumanTaskDeployerTest extends AbstractProcessDeployerTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static int NUMBER_OF_PARTICIPANTS = 3;

    /**
     * {@inheritDoc}
     * @throws ResourceNotAvailableException 
     */
    @Override
    @BeforeMethod
    public void setUp()
    throws IllegalStarteventException, ResourceNotAvailableException {

        try {
            this.deployer = new HumanTaskProcessDeployer();
        } catch (SchedulerException e) {
            logger.error("Scheduling error when creating HumanTaskProcessDeplyoer", e);
        }
        this.uuid = deployer.deploy();
    }

    /**
     * Tests that the participants which should be created are really created. This test breaks if the number of
     * participants in the process deployer is changed.
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
