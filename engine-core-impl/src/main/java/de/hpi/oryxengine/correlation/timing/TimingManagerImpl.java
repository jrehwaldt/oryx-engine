package de.hpi.oryxengine.correlation.timing;

import java.util.List;

import javax.annotation.Nonnull;

import de.hpi.oryxengine.correlation.CorrelationManagerImpl;
import de.hpi.oryxengine.correlation.adapter.PullingInboundAdapter;

/**
 * 
 */
public class TimingManagerImpl implements TimingManager, Runnable {
    private Thread thread;
    
    private CorrelationManagerImpl correlation;
    
    public TimingManagerImpl(@Nonnull CorrelationManagerImpl correlation) {
        this.correlation = correlation;
    }
    
    public void setThread(@Nonnull Thread thread) {
        this.thread = thread;
    }
    
    @Override
    public void run() {
        while (true) {
            for (PullingInboundAdapter adapter: this.correlation.getPullingAdapters()) {
                try {
                    
                    adapter.pull();
                    System.out.println("Woooa2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
