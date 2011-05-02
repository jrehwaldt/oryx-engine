package de.hpi.oryxengine.rest.api;

import java.util.UUID;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.deployment.importer.BpmnXmlFileImporter;
import de.hpi.oryxengine.exception.DefinitionNotFoundException;
import de.hpi.oryxengine.exception.IllegalStarteventException;
import de.hpi.oryxengine.resource.IdentityBuilder;

/**
 * The Class DemoDataForWebservice generates some example data when called.
 */
public final class LoadDemoProcessAsXmlForWebservice {

    private static final String PATH_TO_XML = 
        "/Users/Gery/Entwicklung/BachelorprojektWorkspace/Oryx-Engine-Git/oryx_engine/engine-core-impl/src/test/resources/de/hpi/oryxengine/deployment/bpmn/SimpleUserTask.bpmn.xml";
    private static IdentityBuilder builder;
    private static boolean invoked = false;

    /**
     * Instantiates a new demo data for webservice.
     */
    private LoadDemoProcessAsXmlForWebservice() {
        
    }

    /**
     * Resets invoked, to be honest mostly for testign purposed after each method.
     */
    public synchronized static void resetInvoked() {

        invoked = false;
    }

    /**
     * Gets the builder.
     * 
     * @return the builder
     */
    private static IdentityBuilder getBuilder() {

        builder = ServiceFactory.getIdentityService().getIdentityBuilder();
        return builder;
    }

    /**
     * Generate example Participants.
     */
    public static synchronized void generate() {

        if (!invoked) {
            invoked = true;
            generateDemoParticipants();
            try {
                generateDemoWorklistItems();
            } catch (DefinitionNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalStarteventException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Generate demo participants.
     */
    private static void generateDemoParticipants() {

        getBuilder().createParticipant("Jannik3");
    }

    /**
     * Generate demo worklist items for our participants.
     * 
     * @throws IllegalStarteventException illegal model
     * @throws DefinitionNotFoundException no such definition found
     */
    private static void generateDemoWorklistItems()
    throws IllegalStarteventException, DefinitionNotFoundException {
        
        UUID processID = ServiceFactory.getRepositoryService()
                                       .getDeploymentBuilder()
                                       .deployProcessDefinition(new BpmnXmlFileImporter(PATH_TO_XML));
        
        ServiceFactory.getNavigatorService().startProcessInstance(processID);

    }
}
