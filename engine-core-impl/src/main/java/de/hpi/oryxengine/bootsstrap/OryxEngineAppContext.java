package de.hpi.oryxengine.bootsstrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Wrapper to always return a reference to the Spring Application Context from within non-Spring enabled beans. All we
 * need is for this bean to be initialized during application startup.
 * 
 * Found here: http://sujitpal.blogspot.com/2007/03/accessing-spring-beans-from-legacy-code.html
 */
public class OryxEngineAppContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * This method is called from within the ApplicationContext once it is done starting up, it will stick a reference
     * to itself into this bean.
     * 
     * @param context
     *            {@link ApplicationContext} - a reference to the ApplicationContext.
     */
    @Override
    public void setApplicationContext(ApplicationContext context) {

        applicationContext = context;
    }

    /**
     * Retrieves the Singleton Instance of the Application Context.
     * 
     * @return context - a reference to the ApplicationContext.
     */
    public static ApplicationContext getAppContext() {

        return applicationContext;
    }

    /**
     * This is about the same as context.getBean("beanName"), except it has its own static handle to the Spring context,
     * so calling this method statically will give access to the beans by name in the Spring application context. As in
     * the context.getBean("beanName") call, the caller must cast to the appropriate target class. If the bean does not
     * exist, then a Runtime error will be thrown.
     * 
     * @param beanName
     *            - the name of the bean to get.
     * @return an Object reference to the named bean.
     */
    public static Object getBean(String beanName) {

        return applicationContext.getBean(beanName);
    }
}
