package org.jodaengine.bootstrap;



/**
 * Bootstrapper for starting the Joda Engine using a batch script.
 * 
 * It retrieves the parameter and consider them in order to start the {@link JodaEngine}.
 */
public final class JodaEngineBootstrapper {

    /**
     * Starts the {@link JodaEngine} considering arguments.
     *
     * @param args the arguments
     */
    public static void main(String... args) {

        JodaEngine.start();
    }
    
    /**
     * Hidden Constructor.
     */
    private JodaEngineBootstrapper() { }
}
