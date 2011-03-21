package de.hpi.oryxengine.correlation;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import de.hpi.oryxengine.correlation.adapter.InboundPullAdapter;

import de.hpi.oryxengine.navigator.Navigator;
import de.hpi.oryxengine.navigator.NavigatorImpl;


/**
 * Tests the correlation manager, which should start a process instance after correlation.
 * 
 * @author Jan Rehwaldt
 */
public class CorrelationManagerImplTest {
    private Navigator navigator = null;
    private CorrelationManagerImpl manager = null;
   
//    TODO JAN
//    /**
//     * Start a process instance after correlation was called.
//     * @throws Exception if starting the instance fails
//     */
//    @Test
//    public void startProcessInstanceFromCorrelation() throws Exception {
//        for (InboundPullAdapter adapter: manager.getPullingAdapters()) {
//            AdapterEvent event = mock(AdapterEvent.class);
//            when(event.getEventType()).thenReturn(adapter.getEventType());
//            manager.correlate(event);
//            
//            ArgumentCaptor<UUID> uuid = ArgumentCaptor.forClass(UUID.class);
//            // timeout(20)
//            verify(this.navigator, atLeastOnce()).startProcessInstance(uuid.capture());
//        }
//    }
   
    /**
     * Setup.
     */
   @BeforeMethod
   public void beforeMethod() {
      navigator = mock(NavigatorImpl.class);
      manager = new CorrelationManagerImpl(navigator);
      manager.registerCorrelationEvent();
   }

   /**
    * Cleanup.
    */
   @AfterMethod
   public void afterMethod() {
       this.navigator = null;
       this.manager = null;
   }

}
