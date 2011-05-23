package org.jodaengine;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.navigator.NavigatorImpl;
import org.jodaengine.process.token.BPMNToken;

/**
 * The Class NavigatorImplMock. We need this class, as Mockito is not able to stub void methods and thus override their
 * beahviour, but we want to access the internal state of the navigator, such as the token that it currently works on.
 */
public class NavigatorImplMock extends NavigatorImpl {

    private List<BPMNToken> workQueue;

    /**
     * Instantiates a new navigator impl mock.
     */
    public NavigatorImplMock() {

        workQueue = new ArrayList<BPMNToken>();
    }

    @Override
    public void addWorkToken(BPMNToken t) {

        workQueue.add(t);

    }

    /**
     * Flush the work queue. In NavigatorImpl, Tokens are consumed by NavigatorThreads, but in tests we do that
     * manually.
     */
    public void flushWorkQueue() {

        workQueue.clear();
    }

    /**
     * Gets the work queue. Used to inspect the internal state of the navigator.
     * 
     * @return the work queue
     */
    public List<BPMNToken> getWorkQueue() {

        return workQueue;
    }

    /**
     * Consumes a token from the workQueue, as if a navigator thread fetches a token to execute a step.
     * 
     * @param t
     *            the t
     */
    public void consume(BPMNToken t) {

        workQueue.remove(t);
    }
}
