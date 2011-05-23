package org.jodaengine.util.testing;

import org.jodaengine.JodaEngineServices;
import org.jodaengine.bootstrap.JodaEngine;
import org.jodaengine.util.testing.SkipBuildingJodaEngine.JodaEngineTestSkipMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


/**
 * This is the abstract class all test classes should inherit from, if they need the application context to some extend.
 * <p>
 * Per default, this class instantiates the {@link JodaEngine} at the beginning of the test execution (before executing
 * any test method) and again before starting each test method. So per default, each test method has got it's new
 * {@link JodaEngine}, which is independent from the test methods executed previously.
 * </p>
 * <p>
 * The default behavior can be customized by {@link SkipBuildingJodaEngine}-Annotation.
 * </p>
 */
public abstract class AbstractJodaEngineTest {

    private static final String JODAENGINE_CFG_XML = "jodaengine.cfg.xml";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected JodaEngineServices jodaEngineServices;

    /**
     * Initializing the {@link JodaEngine} before any test method is executed.
     */
    @BeforeClass(alwaysRun = true)
    public void aBeforeClassJodaEngineSetUp() {

        // Starting the engine and storing the engineServices
        this.jodaEngineServices = JodaEngine.startWithConfig(JODAENGINE_CFG_XML);
    }

    /**
     * Initializing the {@link JodaEngine} before a test method is executed.
     */
    @BeforeMethod(alwaysRun = true)
    public void aBeforeMethodJodaEngineSetUp() {

        if (skippingForEachTestMethod()) {
            return;
        }

        this.jodaEngineServices = JodaEngine.startWithConfig(JODAENGINE_CFG_XML);
    }

    /**
     * Shuts down the {@link JodaEngine} after a test method has been executed.
     */
    @AfterMethod(alwaysRun = true)
    public void zAfterMethodJodaEngineTearDown() {

        if (skippingForEachTestMethod()) {
            return;
        }

        jodaEngineServices.stop();
    }

    /**
     * Shuts down the {@link JodaEngine} after each test method has been executed.
     */
    @AfterClass(alwaysRun = true)
    public void zAfterClassJodaEngineTearDown() {

        jodaEngineServices.stop();
    }

    /**
     * Checks whether the initialization of the {@link JodaEngine#start()} before running a test method should be
     * skipped.
     * 
     * @see SkipBuildingJodaEngine
     * 
     * @return a {@link Boolean}
     */
    private boolean skippingForEachTestMethod() {

        // Extracting a desired annotation
        SkipBuildingJodaEngine skipBuildingJodaEngine = this.getClass().getAnnotation(SkipBuildingJodaEngine.class);

        if (skipBuildingJodaEngine != null) {

            // Checking if it is the desired one
            if (JodaEngineTestSkipMode.FOR_EACH_TEST_METHOD.equals(skipBuildingJodaEngine.skippingMode())) {
                return true;
            }
        }

        return false;
    }
    
}
