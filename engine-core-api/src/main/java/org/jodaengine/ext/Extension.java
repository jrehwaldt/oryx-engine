package org.jodaengine.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jodaengine.bootstrap.Service;

/**
 * This annotation identifies the extension parts of this engine.
 * 
 * Those extensions are typically located in a different project and implement certain
 * listener APIs, such as {@link org.jodaengine.plugin.activity.ActivityLifecycleListener}.
 * 
 * @author Jan Rehwaldt
 * @since 2011-05-19
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extension {
    /**
     * The extension's name.
     */
    String value();
    
    /**
     * The extension's dependent web {@link Service}, optional.
     * 
     * If specified the {@link org.jodaengine.ext.service.ExtensionService} will
     * create those web services as singleton instances.
     */
    Class<? extends Service>[] webServices() default { };

}
