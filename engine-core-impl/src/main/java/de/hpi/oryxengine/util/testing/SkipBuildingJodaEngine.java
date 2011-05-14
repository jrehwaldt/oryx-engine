package de.hpi.oryxengine.util.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testng.annotations.BeforeMethod;

import de.hpi.oryxengine.bootstrap.JodaEngine;

/**
 * This annotation works together with the {@link AbstractJodaEngineTest}. It provides the possibility to define at
 * which time the {@link JodaEngine} should be built.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipBuildingJodaEngine {

    /**
     * Enumeration of skipModes. A skipMode describes when does the initialization of the {@link JodaEngine#start()} can
     * be skipped.
     */
    static enum JodaEngineTestSkipMode {

        /**
         * It describe that the {@link JodaEngine#start()} will be instantiated only once. This would be at the
         * beginning of the test class execution, respectively {@link BeforeMethod}.
         */
        FOR_EACH_TEST_METHOD
    }

    /**
     * Tells which {@link JodaEngineTestSkipMode} the annotation has defined.
     */
    JodaEngineTestSkipMode skippingMode() default JodaEngineTestSkipMode.FOR_EACH_TEST_METHOD;
}
