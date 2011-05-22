package org.jodaengine.eventmanagement.subscription;


// TODO: Auto-generated Javadoc
/**
 * The Class StartEventSubscriptionTest.
 */
public class StartEventSubscriptionTest {
//    
//    /** The event manager. */
//    private EventManager eventManager;
//
//    /**
//     * Sets the up.
//     */
//    @BeforeMethod
//    private void setUp() {
//    
//        this.eventManager = new EventManager();
//        eventManager.start(Mockito.mock(JodaEngine.class));
//    }
//    
//    /**
//     * Tear down.
//     */
//    @AfterMethod
//    public void tearDown() {
//
//        eventManager.stop();
//    }
//
//    /**
//     * Test process start eventis in list of adapter.
//     */
//    @Test
//    public void testProcessStartEventisInListOfAdapter() {
//
//        
//        InboundMailAdapterConfiguration adapterConfiguration = Mockito.mock(InboundMailAdapterConfiguration.class);
//        ProcessStartEvent startEvent = Mockito.mock(StartEventImpl.class);
//        Mockito.when(startEvent.getAdapterConfiguration()).thenReturn(adapterConfiguration);
//        
//        eventManager.registerStartEvent(startEvent);
//        
//        Assert.assertEquals(eventManager.getEventAdapters().size(), 1);
//        InboundImapMailAdapter mailEventAdapter = (InboundImapMailAdapter) eventManager.getEventAdapters().values().iterator().next();
//        Assert.assertEquals(mailEventAdapter.getProcessEvents().size(), 1);
//        Assert.assertEquals(startEvent, mailEventAdapter.getProcessEvents().get(0));
//        
//        
//        // List höher
//    }
//
//    /**
//     * Test process event triggering.
//     */
//    @Test
//    public void testProcessEventTriggering() {
//        
//    }
}
