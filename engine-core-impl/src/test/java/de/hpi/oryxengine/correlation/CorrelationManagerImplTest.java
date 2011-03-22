package de.hpi.oryxengine.correlation;

//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.timeout;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;

/**
 * Tests the correlation manager, which should start a process instance after correlation.
 * 
 * @author Jan Rehwaldt
 */
public class CorrelationManagerImplTest {
//    public static final int TEST_TIMEOUT = 20;
//    
//    private Navigator navigator = null;
//    private CorrelationManagerImpl manager = null;
//    private List<InboundAdapter> adapters = null;
//   
//    /**
//     * Start a process instance after correlation was called.
//     * 
//     * @throws Exception if starting the instance fails
//     */
//    @Test
//    public void startProcessInstanceFromCorrelation() throws Exception {
//        for (InboundAdapter adapter: this.adapters) {
//            AdapterEvent event = mock(AdapterEvent.class);
//            when(event.getAdapterType()).thenReturn(adapter.getAdapterType());
//            this.manager.correlate(event);
//            
//            ArgumentCaptor<UUID> uuid = ArgumentCaptor.forClass(UUID.class);
//            verify(this.navigator, timeout(TEST_TIMEOUT).atLeastOnce()).startProcessInstance(uuid.capture());
//        }
//    }
//   
//    /**
//     * Setup.
//     */
//   @BeforeTest
//   public void beforeMethod() {
//      this.navigator = mock(NavigatorImpl.class);
//      this.manager = new CorrelationManagerImpl(this.navigator);
//      this.adapters = new ArrayList<InboundAdapter>();
//      
//      // fill adapters
//   }
}
