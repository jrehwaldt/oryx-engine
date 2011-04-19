package de.hpi.oryxengine.rest.api;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class DemoDataForWebservice {
    
    private static IdentityBuilder builder;
    

    
    /**
     * Instantiates a new demo data for webservice.
     */
    private DemoDataForWebservice() {

    }

    /**
     * Gets the builder.
     *
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        if (builder == null) {
            builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        }
        return builder;
    }
    
    /**
     * Generate example Participants.
     */
    public static void generate() {
        generateDemoParticipants();

        
    }
    
    /**
     * Generate demo participants.
     */
    private static void generateDemoParticipants() {
        getBuilder().createParticipant("Peter");
        getBuilder().createParticipant("Pfeiffer");
        getBuilder().createParticipant("Kumpel von Pfeiffer");
        getBuilder().createParticipant("Pfeiffers Hund");
        getBuilder().createParticipant("Pfeiffers Mutter");
        getBuilder().createParticipant("Pfeiffers Vaddi");
    }

}
