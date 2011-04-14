package de.hpi.oryxengine.bootsstrap;



/**
 * Bootstrapper for starting the Dalmatina Engine using a batch script.
 * 
 * It retrieves the parameter and consider them in order to start the {@link OryxEngine}.
 */
public final class OryxEngineBootstrapper {

    /**
     * Starts the {@link OryxEngine} considering arguments.
     *
     * @param args the arguments
     */
    public static void main(String... args) {

        OryxEngine.start();
    }
    
    /**
     * Hidden Constructor.
     */
    private OryxEngineBootstrapper() { }
}
