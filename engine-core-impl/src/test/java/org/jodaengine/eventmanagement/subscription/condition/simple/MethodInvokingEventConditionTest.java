package org.jodaengine.eventmanagement.subscription.condition.simple;

import org.jodaengine.exception.JodaEngineRuntimeException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Testing the {@link MethodInvokingEventCondition}.
 */
public class MethodInvokingEventConditionTest {

    private static final String STRING_TO_MATCH = "This is a Test.";
    private static final int INT_TO_MATCH = 19;

    private DummyAdapterEvent dummyAdapterEvent;

    @BeforeMethod
    public void setUp() {

        this.dummyAdapterEvent = new DummyAdapterEvent(STRING_TO_MATCH, INT_TO_MATCH);
    }

    @Test
    public void testInvokingStringMethodAndEvaluate() {

        MethodInvokingEventCondition invokingEventCondition = new MethodInvokingEventCondition(DummyAdapterEvent.class,
            DummyAdapterEvent.METHOD_RETURNS_STRING, STRING_TO_MATCH);
        
        Assert.assertTrue(invokingEventCondition.evaluate(dummyAdapterEvent));
    }

    @Test
    public void testInvokingIntMethodAndEvaluate() {

        MethodInvokingEventCondition invokingEventCondition = new MethodInvokingEventCondition(DummyAdapterEvent.class,
            DummyAdapterEvent.METHOD_RETURNS_INT, INT_TO_MATCH);
        
        Assert.assertTrue(invokingEventCondition.evaluate(dummyAdapterEvent));
    }

    @Test
    public void testInvokingStringMethodAndEvaluateWithWrongValue() {
        
        // Calling the String-Method and it should be avaluated with an int, the result should be false
        MethodInvokingEventCondition invokingEventCondition = new MethodInvokingEventCondition(DummyAdapterEvent.class,
            DummyAdapterEvent.METHOD_RETURNS_STRING, INT_TO_MATCH);
        
        Assert.assertFalse(invokingEventCondition.evaluate(dummyAdapterEvent));
    }

    @Test(expectedExceptions = JodaEngineRuntimeException.class)
    public void testInvokingNonExistingMethodAndEvaluate() {

        new MethodInvokingEventCondition(DummyAdapterEvent.class, "get123", STRING_TO_MATCH);

        Assert.fail("An JodaEngineRuntimeException should already been raised.");
    }
}
