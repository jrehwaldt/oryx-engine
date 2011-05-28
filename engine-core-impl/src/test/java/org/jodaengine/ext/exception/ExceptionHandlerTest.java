package org.jodaengine.ext.exception;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.token.BpmnToken;
import org.jodaengine.process.token.Token;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class ExceptionHandlerTest. Test whether the chain of responsibility pattern is implemented correctly.
 */
public class ExceptionHandlerTest {
    
    private AbstractExceptionHandler handler1, handler2;
    private Token testToken;
    private AbstractProcessInstance mockInstance;
    private Navigator mockNavigator;

    /**
     * Sets up the exception handlers and creates some mocks.
     */
    @BeforeMethod
    public void setUp() {
        handler1 = new LoggerExceptionHandler();
        handler2 = new InstanceTerminationHandler();
        handler1.setNext(handler2);
        
        mockInstance = mock(AbstractProcessInstance.class);
        mockNavigator = mock(Navigator.class);
        
        testToken = new BpmnToken(mock(Node.class), mockInstance, mockNavigator);
    }

    /**
     * Tests that an attempt is made to cancel the process instance.
     */
    @Test
    public void testInstanceTermination() {
        handler1.processException(mock(JodaEngineRuntimeException.class), testToken);
        
        verify(mockNavigator).cancelProcessInstance(mockInstance);
    }
}
