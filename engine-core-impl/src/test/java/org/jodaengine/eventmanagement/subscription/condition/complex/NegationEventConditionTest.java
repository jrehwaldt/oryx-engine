package org.jodaengine.eventmanagement.subscription.condition.complex;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.processevent.incoming.condition.complex.NegationEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.FalseEventCondition;
import org.jodaengine.eventmanagement.processevent.incoming.condition.simple.TrueEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.EventCondition;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the {@link NegationEventCondition}.
 */
public class NegationEventConditionTest {

    /**
     * Tests whether the NegationEventCondition evaluates the {@link TrueEventCondition} to false.
     */
    @Test
    public void testNegationForTrue() {

        EventCondition eventCondition = new NegationEventCondition(new TrueEventCondition());

        boolean evaluationResult = eventCondition.evaluate(Mockito.mock(AdapterEvent.class));
        Assert.assertFalse(evaluationResult);
    }

    /**
     * Tests whether the NegationEventCondition evaluates the {@link FalseEventCondition} to true.
     */
    @Test
    public void testNegationForFalse() {

        EventCondition eventCondition = new NegationEventCondition(new FalseEventCondition());

        boolean evaluationResult = eventCondition.evaluate(Mockito.mock(AdapterEvent.class));
        Assert.assertTrue(evaluationResult);
    }
}
