package de.hpi.oryxengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * This is the abstract class all test classes should inherit from, if they need the application context to some extend.
 */
@ContextConfiguration(locations = "/test.oryxengine.cfg.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractJodaEngineTest extends AbstractTestNGSpringContextTests {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
