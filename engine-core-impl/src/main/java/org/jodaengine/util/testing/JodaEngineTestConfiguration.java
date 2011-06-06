package org.jodaengine.util.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation configures a test that uses the {@link AbstractJodaEngineTest}. For example you can specify the
 * configurationFile that should be used.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JodaEngineTestConfiguration {

    /**
     * The default configurationFile.
     */
    public final String JODAENGINE_CFG_XML = "jodaengine.cfg.xml";

    /**
     * Here you can specify which spring framework xml file should be used in order to start the jodaengine for the
     * test.
     * 
     * @return a String that specifies the file which contains the spring framework configurations
     */
    String configurationFile() default JODAENGINE_CFG_XML;
}
