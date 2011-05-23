package org.jodaengine.eventmanagement.subscription.condition.complex;

import org.jodaengine.eventmanagement.AdapterEvent;
import org.jodaengine.eventmanagement.subscription.condition.complex.AndEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.FalseEventCondition;
import org.jodaengine.eventmanagement.subscription.condition.simple.TrueEventCondition;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the {@link AndEventCondition}.
 */
public class AndEventConditionTest {

    private AndEventCondition andEventCondition;
    private AdapterEvent adapterEventMock;

    @BeforeMethod
    public void setUp() {

        andEventCondition = new AndEventCondition();
        adapterEventMock = Mockito.mock(AdapterEvent.class);
    }

    @Test
    public void testAndEventConditionWithAllTrue() {

        andEventCondition.addEventCondition(new TrueEventCondition()).addEventCondition(new TrueEventCondition())
        .addEventCondition(new TrueEventCondition());
        Assert.assertTrue(andEventCondition.evaluate(adapterEventMock));
    }

    @Test
    public void testAndEventConditionWithOneFalse() {

        andEventCondition.addEventCondition(new TrueEventCondition()).addEventCondition(new FalseEventCondition())
        .addEventCondition(new TrueEventCondition());

        Assert.assertFalse(andEventCondition.evaluate(adapterEventMock));
    }

    @Test
    public void testAndEventConditionWithAllFalse() {

        andEventCondition.addEventCondition(new FalseEventCondition()).addEventCondition(new FalseEventCondition())
        .addEventCondition(new FalseEventCondition());

        Assert.assertFalse(andEventCondition.evaluate(adapterEventMock));
    }
}
