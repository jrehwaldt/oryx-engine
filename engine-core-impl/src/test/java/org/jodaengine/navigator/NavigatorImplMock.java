package org.jodaengine.navigator;

import java.util.ArrayList;
import java.util.List;

import org.jodaengine.process.token.Token;


/**
 * The Class NavigatorImplMock. We need this class, as Mockito is not able to stub void methods and thus override their
 * beahviour, but we want to access the internal state of the navigator, such as the token that it currently works on.
 */
public class NavigatorImplMock extends NavigatorImpl {

    private List<Token> workQueue;

    /**
     * Instantiates a new navigator impl mock.
     */
    public NavigatorImplMock() {

        workQueue = new ArrayList<Token>();
    }

    @Override
    public void addWorkToken(Token t) {

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
    public List<Token> getWorkQueue() {

        return workQueue;
    }
    
   /**
    * Consumes a token from the workQueue, as if a navigator thread fetches a token to execute a step.
    *
    * @param t the t
    */
   public void consume(Token t) {
       workQueue.remove(t);
   }
}
