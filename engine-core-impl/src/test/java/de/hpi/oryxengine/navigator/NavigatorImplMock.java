package de.hpi.oryxengine.navigator;

import java.util.ArrayList;
import java.util.List;

import de.hpi.oryxengine.process.token.Token;

/**
 * The Class NavigatorImplMock.
 */
public class NavigatorImplMock extends NavigatorImpl {

    private List<Token> workQueue;
    
    public NavigatorImplMock() {
        workQueue = new ArrayList<Token>();
    }
    
    @Override
    public void addWorkToken(Token t) {

        workQueue.add(t);

    }
    
    public void flushWorkQueue() {
        workQueue.clear();
    }
    
    public List<Token> getWorkQueue() {
        return workQueue;
    }
}
