package org.jodaengine.ext.exception;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.jodaengine.exception.JodaEngineException;
import org.jodaengine.exception.JodaEngineRuntimeException;
import org.jodaengine.exception.NoValidPathException;
import org.jodaengine.ext.listener.AbstractExceptionHandler;
import org.jodaengine.navigator.Navigator;
import org.jodaengine.process.instance.AbstractProcessInstance;
import org.jodaengine.process.structure.Node;
import org.jodaengine.process.structure.Transition;
import org.jodaengine.process.token.TokenImpl;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * The Class ExceptionHandlerTest. Test whether the chain of responsibility pattern is implemented correctly.
 */
public class ExceptionHandlerTest {
    
    private AbstractExceptionHandler handler1, handler2;
    private TokenImpl testToken;
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
        
        testToken = new TokenImpl(mock(Node.class), mockInstance, mockNavigator);
        
    }

    /**
     * Tests that an attempt is made to cancel the process instance.
     */
    @Test
    public void testInstanceTermination() {
        handler1.processException(mock(JodaEngineRuntimeException.class), testToken);
        
        verify(mockNavigator).cancelProcessInstance(mockInstance);
    }
    
    /**
     * Tests the registration of {@link AbstractExceptionHandler}s and that navigating to zero transitions will
     * cause an exception.
     */
    @Test
    public void testExceptionHandlerRegistration() {
        AbstractExceptionHandler handlerSpy = Mockito.spy(this.handler1);
        
        this.testToken.registerExceptionHandlers(Arrays.asList(handlerSpy));
        this.testToken.navigateTo(Collections.<Transition>emptyList());
        
        ArgumentCaptor<JodaEngineException> exception = ArgumentCaptor.forClass(JodaEngineException.class);
        verify(handlerSpy).processException(exception.capture(), Mockito.eq(this.testToken));
        Assert.assertTrue(exception.getValue() instanceof NoValidPathException);
    }
}
