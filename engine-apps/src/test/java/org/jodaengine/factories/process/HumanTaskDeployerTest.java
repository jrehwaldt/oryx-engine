package org.jodaengine.factories.process;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.jodaengine.IdentityService;
import org.jodaengine.ServiceFactory;
import org.jodaengine.exception.IllegalStarteventException;
import org.jodaengine.exception.ResourceNotAvailableException;

/**
 * Tests the EcampleProcessDeplyoer class. {@inheritDoc}
 */
public class HumanTaskDeployerTest extends AbstractProcessDeployerTest {

    private final static int NUMBER_OF_PARTICIPANTS = 3;

    /**
     * {@inheritDoc}
     * 
     * @throws ResourceNotAvailableException
     */
    @Override
    @BeforeMethod
    public void executeDeployer()
    throws IllegalStarteventException, ResourceNotAvailableException {

        this.deployer = new HumanTaskProcessDeployer();
        this.id = deployer.deploy(engineServices);
    }

    /**
     * Tests that the participants which should be created are really created. This test breaks if the number of
     * participants in the {@link ProcessDeployer} is changed.
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
