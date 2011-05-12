package de.hpi.oryxengine.factory.resource;

import java.util.List;
import java.util.Random;

import org.mockito.Mockito;

import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.WorklistService;
import de.hpi.oryxengine.allocation.CreationPattern;
import de.hpi.oryxengine.allocation.TaskAllocation;
import de.hpi.oryxengine.factory.worklist.CreationPatternFactory;
import de.hpi.oryxengine.process.token.Token;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.worklist.AbstractWorklistItem;

/**
 * A little factory for creating {@link AbstractParticipant}s.
 */
public class ParticipantFactory extends ResourceFactory {
    
    private static final short NUMBER_OF_ITEMS_FOR_BUSY_PARTICIPANT = 100;

    /**
     * Returns a {@link AbstractParticipant} object with the following parameter
     *  name: Gerardo Navarro Suarez
     * 
     * @return the {@link AbstractParticipant} Gerardo
     */
    public static AbstractParticipant createGerardo() {

        return createParticipant("Gerardo Navarro Suarez");
    }

    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  name: Jannik Streek
     * 
     * @return the {@link AbstractParticipant} Jannik
     */
    public static AbstractParticipant createJannik() {

        return createParticipant("Jannik Streek");
    }
    
    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  name: Tobias Pfeiffer
     * 
     * @return the {@link Participant} Tobi
     */
    public static AbstractParticipant createTobi() {

        return createParticipant("Tobias Pfeiffer");
    }
    
    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  name: Tobias Metzke
     * 
     * @return the {@link Participant} Tobi2
     */
    public static AbstractParticipant createTobi2() {

        return createParticipant("Tobias Metzke");
    }

    
    /**
     * Returns a {@link AbstractParticipant} object with the the following parameter
     *  id: willi.joda
     *  name: Willi Joda
     * 
     * @return the {@link Participant} BusyWilli
     */
    public static AbstractParticipant createBusyWilli() {
        WorklistService worklistService = ServiceFactory.getWorklistService();
        TaskAllocation taskAllocationService = ServiceFactory.getWorklistQueue();
        Random rand = new Random();
        
        AbstractParticipant willi = createParticipant("Willi Joda");
        
        willi.setProperty("some-property", "some-value");
        willi.setProperty("another-property", "another-value");
        
        CreationPattern pattern = null;
        for (int i = 0; i < NUMBER_OF_ITEMS_FOR_BUSY_PARTICIPANT; i++) {
            pattern = CreationPatternFactory.createParticipantTask(willi);
            pattern.createWorklistItems(taskAllocationService, Mockito.mock(Token.class));
//            taskDistributionService.distribute(pattern, Mockito.mock(Token.class));
        }
        
        List<AbstractWorklistItem> items = worklistService.getWorklistItems(willi);
        for (AbstractWorklistItem item: items) {
            if (rand.nextBoolean()) {
                worklistService.claimWorklistItemBy(item, willi);
            }
        }
        
        return willi;
    }
    

}
