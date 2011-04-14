package de.hpi.oryxengine.bootstrap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This is used in war-Files, to do engine's bootstrapping upon deployment.
 */
public class EngineServletListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

        
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        OryxEngine.start();
    }

}
