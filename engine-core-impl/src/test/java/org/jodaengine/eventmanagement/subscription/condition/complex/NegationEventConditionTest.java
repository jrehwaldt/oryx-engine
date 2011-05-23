package org.jodaengine.eventmanagement.subscription.condition.complex;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.jodaengine.eventmanagement.subscription.condition.complex.NegationEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.FalseEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the {@link NegationEventCondition}.
 */
public class NegationEventConditionTest {

    @Test
    public void testNegationForTrue() {
        EventCondition eventCondition = new NegationEventCondition(new TrueEventCondition());
        
        boolean evaluationResult = eventCondition.evaluate(Mockito.mock(AdapterEvent.class));
        Assert.assertFalse(evaluationResult);
    }

    @Test
    public void testNegationForFalse() {
        
        EventCondition eventCondition = new NegationEventCondition(new FalseEventCondition());
        
        boolean evaluationResult = eventCondition.evaluate(Mockito.mock(AdapterEvent.class));
        Assert.assertTrue(evaluationResult);
    }
}
