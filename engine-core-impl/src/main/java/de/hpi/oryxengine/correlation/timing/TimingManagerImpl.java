package de.hpi.oryxengine.correlation.timing;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * The Class TimingManagerImpl.
 */
public class TimingManagerImpl implements TimingManager, Runnable {
    @SuppressWarnings("unused")
    private Thread thread;
    
    private CorrelationManagerImpl correlation;
    
    /**
     * Default constructor.
     * 
     * @param correlation the correlation manager.
     */
    public TimingManagerImpl(@Nonnull CorrelationManagerImpl correlation) {
        this.correlation = correlation;
    }
    
    /**
     * Sets the timer thread.
     * 
     * @param thread its own thread
     */
    public void setThread(@Nonnull Thread thread) {
        this.thread = thread;
    }
    
    @Override
    public void run() {
        while (true) {
            for (PullingInboundAdapter adapter: this.correlation.getPullingAdapters()) {
                try {
                    
                    adapter.pull();
//                    System.out.println("Woooa2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
