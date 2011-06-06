package org.jodaengine.util.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Gery
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JodaEngineTest {

    public final String JODAENGINE_CFG_XML = "jodaengine.cfg.xml";
    
    String configurationFile() default JODAENGINE_CFG_XML;
}
